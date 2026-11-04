package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
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
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fabric port of the Forge event subscriber. The two Forge damage events map to
 * Fabric's {@code ServerLivingEntityEvents.ALLOW_DAMAGE} (cancel-only) plus the
 * {@code LivingEntityMixin} (which mirrors {@code LivingHurtEvent}'s amount
 * adjustment). Ticking is driven from the Fabric server/world tick events
 * because Fabric has no per-entity tick event, and the tether state lives in a
 * server-side map instead of Forge's {@code Entity#getPersistentData()}.
 */
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
    private static final Map<UUID, Tether> TETHERS = new ConcurrentHashMap<>();
    private static final ThreadLocal<Boolean> REFLECTING_DAMAGE = ThreadLocal.withInitial(() -> false);

    private ChineseWeaponsCombatEvents() {
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

    /** Maps Forge's {@code LivingAttackEvent} (cancel-only rules). */
    public static boolean allowDamage(LivingEntity target, DamageSource source, float amount) {
        if (REFLECTING_DAMAGE.get()) {
            return true;
        }

        ArmorFamily targetFamily = primaryArmorFamily(target);

        if (targetFamily == ArmorFamily.FINE_SCALE
                && countPieces(target, targetFamily) == 4
                && source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return false;
        }

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

    /** Maps Forge's {@code LivingHurtEvent} (amount adjustment rules). */
    public static float modifyDamageAmount(LivingEntity wearer, DamageSource source, float amount) {
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

        if (family == ArmorFamily.FINE_SCALE && fullSet && source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return 0.0F;
        }

        switch (family) {
            case MOUNTAIN_CHARACTER -> {
                if (isSwordOrTrident(source)) {
                    amount = Math.max(0.0F, amount - 3.0F);
                }
            }
            case FINE_SCALE -> {
                if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    amount = amount * 0.80F;
                }
            }
            case BLACK_CHUI -> {
                if (roll(wearer, fullSet ? 0.40F : 0.20F)) {
                    coolDownAttackersWeapon(source, 3 * 20);
                }

                if (isMaceSmash(source)) {
                    if (roll(wearer, fullSet ? 1.00F : 0.50F)) {
                        amount = amount * 0.50F;
                    }
                } else if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.60F : 0.30F)) {
                    amount = amount * 0.60F;
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
                    amount = amount * 0.80F;
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 1.0F);
                }
            }
            case MIDDLE_MING_GUANG -> {
                if (isSwordOrTrident(source) && wearer.isShiftKeyDown()) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }
            }
            case LATE_MING_GUANG -> {
                if (isSwordOrTrident(source) && wearer.isShiftKeyDown()) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }

                if (fullSet
                        && source.getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow instanceof ThrownTrident)
                        && !isFireArrow(arrow)
                        && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }
            }
        }
        return amount;
    }

    /** Maps Forge's {@code TickEvent.PlayerTickEvent} (END phase, server side). */
    public static void playerTick(Player player) {
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

    /** Maps Forge's {@code LivingEvent.LivingTickEvent} for tethered entities. */
    public static void tickTethered(ServerLevel level) {
        if (TETHERS.isEmpty()) {
            return;
        }

        long now = level.getGameTime();
        for (Iterator<Map.Entry<UUID, Tether>> iterator = TETHERS.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<UUID, Tether> entry = iterator.next();
            Tether tether = entry.getValue();

            Entity targetEntity = level.getEntity(entry.getKey());
            if (!(targetEntity instanceof LivingEntity target)) {
                if (now >= tether.until()) {
                    iterator.remove();
                }
                continue;
            }

            if (now >= tether.until()) {
                iterator.remove();
                continue;
            }

            Entity ownerEntity = level.getEntity(tether.owner());
            if (!(ownerEntity instanceof Player playerOwner) || !ownerEntity.isAlive()) {
                iterator.remove();
                continue;
            }

            pullTowardsOwner(target, playerOwner);
        }
    }

    private static void pullTowardsOwner(LivingEntity target, Player playerOwner) {
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

        TETHERS.put(target.getUUID(),
                new Tether(owner.getUUID(), owner.level().getGameTime() + TETHER_DURATION));
        target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(),
                TETHER_DURATION, 0, false, true));
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return countPieces(entity, ArmorFamily.FINE_SCALE) == 4;
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
}
