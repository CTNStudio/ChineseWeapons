package net.mirrorloong.chineseweapons.procedures.armor;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
//old api ForgeRegistries.ITEMS.getKey(item) -> new api BuiltInRegistries.ITEM.getKey(item)
//old api import net.minecraftforge.registries.ForgeRegistries -> new api net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ChineseWeaponsCombatEvents {
    private static final UUID MING_GUANG_SPEED_ID = UUID.fromString("1117a420-9d72-4fa4-89d0-825a318d3e17");
    private static final AttributeModifier MING_GUANG_SPEED = new AttributeModifier(
            MING_GUANG_SPEED_ID,
            "Ming Guang armour movement speed",
            0.20D,
            AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final UUID MOUNT_ARMOR_ID = UUID.fromString("8f2c4d7a-3b1e-4c9a-9f6d-2a7b5c8e1d34");
    private static final AttributeModifier MOUNT_ARMOR = new AttributeModifier(
            MOUNT_ARMOR_ID,
            "Ming Guang mount armour",
            2.0D,
            AttributeModifier.Operation.ADDITION
    );

    private static final int TETHER_DURATION = 5 * 20;
    private static final ThreadLocal<Boolean> REFLECTING_DAMAGE = ThreadLocal.withInitial(() -> false);
        private static final ThreadLocal<Boolean> APPLYING_REDUCED_DAMAGE = ThreadLocal.withInitial(() -> false);
        private static final Map<UUID, Tether> TETHERS = new ConcurrentHashMap<>();

    private static boolean registered;

    private ChineseWeaponsCombatEvents() {
    }

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;

                ServerLivingEntityEvents.ALLOW_DAMAGE.register(ChineseWeaponsCombatEvents::onAllowDamage);

                        ServerTickEvents.END_SERVER_TICK.register(ChineseWeaponsCombatEvents::onServerTick);
    }

    private record Tether(UUID owner, long until) {
    }

    private enum ArmorFamily {
        BLACK_CHUI("black_chui_armor_"),
        EARLY_MING_GUANG("early_tang_dynasty_ming_guang_armor_"),
        MIDDLE_MING_GUANG("middle_tang_dynasty_ming_guang_armor_"),
        LATE_MING_GUANG("late_tang_dynasty_ming_guang_armor_"),
        MOUNTAIN_CHARACTER("mountain_character_armor_"),
        FINE_SCALE("fine_scale_armor_");

        private final String itemPathMarker;

        ArmorFamily(String itemPathMarker) {
            this.itemPathMarker = itemPathMarker;
        }
    }

        public static boolean onAllowDamage(LivingEntity entity, DamageSource source, float amount) {
        if (APPLYING_REDUCED_DAMAGE.get()) {
            return true;
        }
        if (!allowTetheredAttacks(entity, source, amount)) {
            return false;
        }
        if (!allowExplosionDamage(entity, source)) {
            return false;
        }
                return applyArmorCombatRules(entity, source, amount);
    }

    private static boolean allowTetheredAttacks(LivingEntity target, DamageSource source, float amount) {
        if (REFLECTING_DAMAGE.get()) {
            return true;
        }

        ArmorFamily targetFamily = primaryArmorFamily(target);

        if (targetFamily != null
                && isMingGuang(targetFamily)
                && countPieces(target, targetFamily) == 4
                && source.getDirectEntity() instanceof AbstractArrow arrow
                && !(arrow instanceof ThrownTrident)) {

            if (isFireArrow(arrow)) {
                return true;
            }

            if (amount <= 3.0F) {
                arrow.setDeltaMovement(arrow.getDeltaMovement().scale(10.0D));
                arrow.hurtMarked = true;
                return false;
            }
            return true;
        }

        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker && isTethered(livingAttacker)) {
            return false;
        }
        return true;
    }

    private static boolean applyArmorCombatRules(LivingEntity wearer, DamageSource source, float amount) {
        float desired = reduceDamage(wearer, source, amount);
        if (desired <= 0.0F) {
            return false;
        }
        if (desired >= amount) {
            return true;
        }
                float nestedAmount = undoPlayerDifficultyScaling(wearer, source, desired);
        APPLYING_REDUCED_DAMAGE.set(true);
        try {
            wearer.hurt(source, nestedAmount);
        } finally {
            APPLYING_REDUCED_DAMAGE.set(false);
        }
        return false;
    }

            private static float undoPlayerDifficultyScaling(LivingEntity wearer, DamageSource source, float desired) {
        if (!(wearer instanceof Player) || !source.scalesWithDifficulty()) {
            return desired;
        }
        Difficulty difficulty = wearer.level().getDifficulty();
        if (difficulty == Difficulty.HARD) {
            return desired / 1.5F;
        }
        if (difficulty == Difficulty.EASY) {
                        return desired < 2.0F ? desired : 2.0F * (desired - 1.0F);
        }
        return desired;
    }

    private static float reduceDamage(LivingEntity wearer, DamageSource source, float amount) {
        if (wearer.level().isClientSide() || REFLECTING_DAMAGE.get()) {
            return amount;
        }

        ArmorFamily family = primaryArmorFamily(wearer);
        if (family == null) {
            return amount;
        }

        int pieceCount = countPieces(wearer, family);
        boolean fullSet = pieceCount == 4;
        float originalDamage = amount;
        float result = amount;

        if (family == ArmorFamily.FINE_SCALE && fullSet && source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return 0.0F;
        }

        switch (family) {
            case MOUNTAIN_CHARACTER -> {
                if (isSwordOrTrident(source)) {
                    result = Math.max(0.0F, result - 3.0F);
                }
            }
            case FINE_SCALE -> {
                if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    result = result * 0.80F;
                }
            }
            case BLACK_CHUI -> {
                if (roll(wearer, fullSet ? 0.40F : 0.20F)) {
                    coolDownAttackersWeapon(source, 3 * 20);
                }

                if (isMaceSmash(source)) {
                    if (roll(wearer, fullSet ? 1.00F : 0.50F)) {
                        result = result * 0.50F;
                    }
                } else if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.60F : 0.30F)) {
                    result = result * 0.60F;
                }

                if (roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    reflectDamage(wearer, source.getEntity(), originalDamage * 0.50F);
                }
            }
            case EARLY_MING_GUANG -> {
                if (roll(wearer, fullSet ? 0.20F : 0.10F)) {
                    coolDownAttackersWeapon(source, 2 * 20);
                }

                if (isMaceSmash(source) && roll(wearer, fullSet ? 0.80F : 0.30F)) {
                    result = result * 0.80F;
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && result > 3.0F) {
                    result = Math.max(0.0F, result - 1.0F);
                }
            }
            case MIDDLE_MING_GUANG -> {
                if (isSwordOrTrident(source) && wearer.isShiftKeyDown()) {
                    result = Math.max(0.0F, result - 2.0F);
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && result > 3.0F) {
                    result = Math.max(0.0F, result - 2.0F);
                }
            }
            case LATE_MING_GUANG -> {
                if (isSwordOrTrident(source) && wearer.isShiftKeyDown()) {
                    result = Math.max(0.0F, result - 2.0F);
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && result > 3.0F) {
                    result = Math.max(0.0F, result - 2.0F);
                }
            }
        }
        return result;
    }

        private static void onServerTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            applyArmorTickRules(player);
        }
        tickTethers(server);
    }

    private static void applyArmorTickRules(Player player) {
        if (player.level().isClientSide()) {
            return;
        }

        ArmorFamily family = primaryArmorFamily(player);
        boolean fullSet = family != null && countPieces(player, family) == 4;

        updateMingGuangSpeed(player, false);

        if (!fullSet) {
            return;
        }

        if (isMingGuang(family) && player.getVehicle() instanceof LivingEntity mount && player.tickCount % 10 == 0) {
            applyMountEffects(family, mount);
        }
    }

        private static void tickTethers(MinecraftServer server) {
        for (Map.Entry<UUID, Tether> entry : TETHERS.entrySet()) {
            Entity entity = null;
            for (ServerLevel level : server.getAllLevels()) {
                entity = level.getEntity(entry.getKey());
                if (entity != null) {
                    break;
                }
            }
            if (!(entity instanceof LivingEntity target)) {
                TETHERS.remove(entry.getKey());
                continue;
            }
            tickTetheredEntity(target, entry.getValue());
        }
    }

    private static void tickTetheredEntity(LivingEntity target, Tether tether) {
        if (!isTethered(target)) {
            return;
        }

                        if (target.getDeltaMovement().y > 0.25D && roll(target, 0.30F)) {
            clearTether(target);
            return;
        }

        if (!(target.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity owner = serverLevel.getEntity(tether.owner());
        if (!(owner instanceof Player playerOwner)
                || !owner.isAlive()
                || serverLevel.getGameTime() >= tether.until()) {
            clearTether(target);
            return;
        }

        Vec3 look = playerOwner.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0.0D, look.z);
        if (horizontalLook.lengthSqr() < 1.0E-4D) {
            horizontalLook = Vec3.directionFromRotation(0.0F, playerOwner.getYRot());
        }
        horizontalLook = horizontalLook.normalize();

        Vec3 anchor = playerOwner.position().subtract(horizontalLook.scale(1.5D));
        Vec3 horizontalDelta = new Vec3(anchor.x - target.getX(), 0.0D, anchor.z - target.getZ());

        if (horizontalDelta.lengthSqr() > 64.0D) {
            target.teleportTo(anchor.x, playerOwner.getY(), anchor.z);
        } else {
            double distance = horizontalDelta.length();
            Vec3 pull = distance < 0.20D
                    ? Vec3.ZERO
                    : horizontalDelta.normalize().scale(Math.min(0.75D, distance * 0.35D));
            target.setDeltaMovement(pull.x, target.getDeltaMovement().y, pull.z);
            target.hurtMarked = true;
        }

        target.setSprinting(false);
        if (target instanceof Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }
    }

    public static boolean tryStartMountedDrag(Player owner, LivingEntity target) {
        if (owner.level().isClientSide()
                || owner == target
                || owner.getVehicle() == null
                || target.getVehicle() != null
                || isFullFineScaleSet(target)) {
            return false;
        }

        TETHERS.put(target.getUUID(), new Tether(owner.getUUID(), owner.level().getGameTime() + TETHER_DURATION));
        target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(), TETHER_DURATION, 0, false, true));
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return countPieces(entity, ArmorFamily.FINE_SCALE) == 4;
    }

    private static void clearTether(LivingEntity entity) {
        TETHERS.remove(entity.getUUID());
    }

    public static boolean isTethered(LivingEntity entity) {
        return TETHERS.containsKey(entity.getUUID());
    }

    private static void updateMingGuangSpeed(Player player, boolean enabled) {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        AttributeModifier existing = movementSpeed.getModifier(MING_GUANG_SPEED_ID);
        if (enabled && existing == null) {
            movementSpeed.addTransientModifier(MING_GUANG_SPEED);
        } else if (!enabled && existing != null) {
            movementSpeed.removeModifier(MING_GUANG_SPEED_ID);
        }
    }

    private static void applyMountEffects(ArmorFamily family, LivingEntity mount) {
        AttributeInstance armor = mount.getAttribute(Attributes.ARMOR);
        if (armor == null) {
            return;
        }

        AttributeModifier existing = armor.getModifier(MOUNT_ARMOR_ID);
        if (existing == null) {
            armor.addTransientModifier(MOUNT_ARMOR);
        }
    }

    private static boolean groundIsWithinThreeBlocks(Player player) {
        Vec3 start = player.position();
        Vec3 end = start.add(0.0D, -3.25D, 0.0D);
        BlockHitResult hit = player.level().clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
        return hit.getType() != HitResult.Type.MISS && start.y - hit.getLocation().y <= 3.25D;
    }

    private static void coolDownAttackersWeapon(DamageSource source, int ticks) {
        if (source.getEntity() instanceof Player attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (!weapon.isEmpty()) {
                attacker.getCooldowns().addCooldown(weapon.getItem(), ticks);
            }
        }
    }

    private static void reflectDamage(LivingEntity defender, Entity attacker, float amount) {
        if (!(attacker instanceof LivingEntity livingAttacker) || attacker == defender || amount <= 0.0F) {
            return;
        }

        REFLECTING_DAMAGE.set(true);
        try {
            livingAttacker.hurt(defender.damageSources().thorns(defender), amount);
        } finally {
            REFLECTING_DAMAGE.set(false);
        }
    }

    private static boolean isSwordOrTrident(DamageSource source) {
        if (source.getDirectEntity() instanceof ThrownTrident) {
            return true;
        }

        if (source.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            return weapon.is(ItemTags.SWORDS)
                    || weapon.getItem() instanceof SwordItem
                    || weapon.getItem() instanceof TridentItem;
        }
        return false;
    }

    private static boolean isFireArrow(AbstractArrow arrow) {
        return arrow.isOnFire();
    }

    private static boolean isMaceSmash(DamageSource source) {
        String messageId = source.getMsgId().toLowerCase();
        if (messageId.contains("mace_smash") || messageId.contains("mace")) {
            return true;
        }

        if (source.getEntity() instanceof LivingEntity attacker && attacker.fallDistance > 0.0F) {

            ResourceLocation key = BuiltInRegistries.ITEM.getKey(attacker.getMainHandItem().getItem());
            if (key != null) {
                String path = key.getPath();
                return path.contains("mace") || path.contains("heavy_hammer");
            }
        }
        return false;
    }

    private static boolean isMingGuang(ArmorFamily family) {
        return family == ArmorFamily.EARLY_MING_GUANG
                || family == ArmorFamily.MIDDLE_MING_GUANG
                || family == ArmorFamily.LATE_MING_GUANG;
    }

    private static ArmorFamily primaryArmorFamily(LivingEntity entity) {
        ArmorFamily best = null;
        int bestCount = 0;
        for (ArmorFamily family : ArmorFamily.values()) {
            int count = countPieces(entity, family);
            if (count > bestCount) {
                best = family;
                bestCount = count;
            }
        }
        return best;
    }

    private static int countPieces(LivingEntity entity, ArmorFamily family) {
        int count = 0;
        for (ItemStack armor : entity.getArmorSlots()) {
            if (isArmorPiece(armor, family)) {
                count++;
            }
        }
        if (isMingGuang(family) && isMingGuangHelmetSubstitute(entity.getItemBySlot(EquipmentSlot.HEAD))) {
            count++;
        }
        return count;
    }

    private static boolean isArmorPiece(ItemStack stack, ArmorFamily family) {
        if (stack.isEmpty()) {
            return false;
        }

        if (!itemPath(stack).contains(family.itemPathMarker)) {
            return false;
        }

        return WeaponScriptSettings.getLogicChance(
                stack.getItem(),
                WeaponScriptSettings.Logic.ARMOR_GAIN,
                true
        );
    }

    private static boolean isMingGuangHelmetSubstitute(ItemStack helmet) {
        if (helmet.isEmpty()) {
            return false;
        }

        boolean isSubstitute = helmet.is(ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.IRON_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.DIAMOND_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.IRON_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get());

        if (!isSubstitute) {
            return false;
        }

        return WeaponScriptSettings.getLogicChance(
                helmet.getItem(),
                WeaponScriptSettings.Logic.ARMOR_GAIN,
                true
        );
    }

    private static String itemPath(ItemStack stack) {

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key == null ? "" : key.getPath();
    }

    private static boolean roll(LivingEntity entity, float probability) {
        return entity.getRandom().nextFloat() < probability;
    }

                private static boolean allowExplosionDamage(LivingEntity entity, DamageSource source) {
        if (!source.is(DamageTypes.EXPLOSION) && !source.is(DamageTypes.PLAYER_EXPLOSION)) {
            return true;
        }
        return !isInsideShieldSafeZone(entity);
    }

    private static boolean isInsideShieldSafeZone(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return false;
        }

        double behindCenter = WeaponScriptSettings.getArmorThreshold(
                null,
                WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, 4.0);

        List<SongStandingShieldEntity> shields = level.getEntitiesOfClass(
                SongStandingShieldEntity.class,
                entity.getBoundingBox().inflate(32.0D)
        );

        for (SongStandingShieldEntity shield : shields) {
            float yaw = shield.getShieldYaw();
            Vec3 forward = new Vec3(-Math.sin(Math.toRadians(yaw)), 0.0D, Math.cos(Math.toRadians(yaw))).normalize();
            Vec3 center = shield.position().subtract(forward.scale(behindCenter));
            AABB safeZone = new AABB(
                    center.x - 4, center.y - 4, center.z - 4,
                    center.x + 4, center.y + 4, center.z + 4
            );
            if (safeZone.contains(entity.position())) {
                return true;
            }
        }
        return false;
    }
}
