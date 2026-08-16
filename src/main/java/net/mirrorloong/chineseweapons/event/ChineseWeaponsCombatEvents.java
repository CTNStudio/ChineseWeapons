package net.mirrorloong.chineseweapons.event;

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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

/**
 * Server-side combat rules shared by the armour sets and the sky-piercing halberd.
 * Full-set-only effects deliberately require four matching armour-family pieces;
 * mixed material tiers of the same family still count as one complete set, and
 * the documented Suanni and Bohai helmets can replace a Ming Guang helmet.
 */
@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ChineseWeaponsCombatEvents {
    private static final UUID MING_GUANG_SPEED_ID = UUID.fromString("1117a420-9d72-4fa4-89d0-825a318d3e17");
    private static final AttributeModifier MING_GUANG_SPEED = new AttributeModifier(
            MING_GUANG_SPEED_ID,
            "Ming Guang armour movement speed",
            0.20D,
            AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final String TETHER_OWNER = "chineseweapons:tether_owner";
    private static final String TETHER_UNTIL = "chineseweapons:tether_until";
    private static final int TETHER_DURATION = 5 * 20;
    private static final ThreadLocal<Boolean> REFLECTING_DAMAGE = ThreadLocal.withInitial(() -> false);

    private ChineseWeaponsCombatEvents() {
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventTetheredAttacks(LivingAttackEvent event) {
        if (REFLECTING_DAMAGE.get()) {
            return;
        }

        LivingEntity target = event.getEntity();
        ArmorFamily targetFamily = primaryArmorFamily(target);
        if (targetFamily != null
                && isMingGuang(targetFamily)
                && countPieces(target, targetFamily) == 4
                && event.getSource().getDirectEntity() instanceof AbstractArrow arrow
                && !(arrow instanceof ThrownTrident)) {
            // AbstractArrow reverses rejected hits at 10% speed. Pre-scaling here
            // makes the resulting reflected arrow retain its incoming speed.
            arrow.setDeltaMovement(arrow.getDeltaMovement().scale(10.0D));
            arrow.hurtMarked = true;
            event.setCanceled(true);
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity livingAttacker && isTethered(livingAttacker)) {
            event.setCanceled(true);
        }
    }

    /*
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColor dyeableColor = (stack, tintIndex) -> {
            if (tintIndex == 1 && DyeableItem.hasCustomColor(stack)) {
                return DyeableItem.getColor(stack);
            }
            return 0xFFFFFF;
        };

        event.register(dyeableColor,
                ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONBLACKCHUIARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONBLACKCHUIARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONBLACKCHUIARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONBLACKCHUIARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_BOOTS.get(),
                ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_LEGGINGS.get(),

                ChineseweaponsModItems.DIAMOND_BOHAI_HELMET.get(),
                ChineseweaponsModItems.IRON_BOHAI_HELMET.get(),
                ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get(),
                ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get(),


                ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get(),

                ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_BOOTS.get(),
                ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_BOOTS.get(),
                ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_BOOTS.get(),

                ChineseweaponsModItems.IRONFOOTMENARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONFOOTMENARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONFOOTMENARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONFOOTMENARMOR_BOOTS.get(),
                ChineseweaponsModItems.GOLDENFOOTMENARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENFOOTMENARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENFOOTMENARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENFOOTMENARMOR_BOOTS.get(),
                ChineseweaponsModItems.DIAMONDFOOTMENARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDFOOTMENARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDFOOTMENARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDFOOTMENARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEFOOTMENARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEFOOTMENARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEFOOTMENARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEFOOTMENARMOR_BOOTS.get(),

                ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),

                ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),
                ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get(),

                ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_HELMET.get(),
                ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_BOOTS.get(),
                ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_HELMET.get(),
                ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_BOOTS.get(),
                ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_HELMET.get(),
                ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_BOOTS.get(),
                ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_HELMET.get(),
                ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_CHESTPLATE.get(),
                ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_LEGGINGS.get(),
                ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_BOOTS.get()
        );
    }
    */

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void applyArmorCombatRules(LivingHurtEvent event) {
        LivingEntity wearer = event.getEntity();
        if (wearer.level().isClientSide() || REFLECTING_DAMAGE.get()) {
            return;
        }

        ArmorFamily family = primaryArmorFamily(wearer);
        if (family == null) {
            return;
        }

        int pieceCount = countPieces(wearer, family);
        boolean fullSet = pieceCount == 4;
        DamageSource source = event.getSource();
        float originalDamage = event.getAmount();

        if (family == ArmorFamily.FINE_SCALE && fullSet && source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            event.setCanceled(true);
            return;
        }

        switch (family) {
            case MOUNTAIN_CHARACTER, FINE_SCALE -> {
                if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    event.setAmount(event.getAmount() * 0.80F);
                }
            }
            case BLACK_CHUI -> {
                if (roll(wearer, fullSet ? 0.40F : 0.20F)) {
                    coolDownAttackersWeapon(source, 3 * 20);
                }

                if (isMaceSmash(source)) {
                    if (roll(wearer, fullSet ? 1.00F : 0.50F)) {
                        event.setAmount(event.getAmount() * 0.50F);
                    }
                } else if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.60F : 0.30F)) {
                    event.setAmount(event.getAmount() * 0.60F);
                }

                if (roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    reflectDamage(wearer, source.getEntity(), originalDamage * 0.50F);
                }
            }
            case EARLY_MING_GUANG -> {
                if (roll(wearer, fullSet ? 0.20F : 0.10F)) {
                    coolDownAttackersWeapon(source, 4 * 20);
                }
                if (isMaceSmash(source) && roll(wearer, fullSet ? 0.80F : 0.30F)) {
                    event.setAmount(event.getAmount() * 0.80F);
                }
            }
            case MIDDLE_MING_GUANG, LATE_MING_GUANG -> {
                if (isSwordOrTrident(source) && roll(wearer, fullSet ? 0.60F : 0.30F)) {
                    event.setAmount(event.getAmount() * 0.60F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void applyArmorTickRules(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || player.level().isClientSide()) {
            return;
        }

        ArmorFamily family = primaryArmorFamily(player);
        boolean fullSet = family != null && countPieces(player, family) == 4;
        updateMingGuangSpeed(player, fullSet && isMingGuang(family));

        if (!fullSet) {
            return;
        }

        if (family == ArmorFamily.MOUNTAIN_CHARACTER
                && player.fallDistance > 3.0F
                && player.getDeltaMovement().y < 0.0D
                && !player.isFallFlying()
                && groundIsWithinThreeBlocks(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false));
        }

        if (isMingGuang(family) && player.getVehicle() instanceof LivingEntity mount && player.tickCount % 10 == 0) {
            applyMountEffects(family, mount);
        }
    }

    @SubscribeEvent
    public static void tickTetheredEntity(LivingEvent.LivingTickEvent event) {
        LivingEntity target = event.getEntity();
        if (!(target.level() instanceof ServerLevel serverLevel) || !isTethered(target)) {
            return;
        }

        CompoundTag data = target.getPersistentData();
        if (!data.hasUUID(TETHER_OWNER)) {
            clearTether(target);
            return;
        }

        Entity owner = serverLevel.getEntity(data.getUUID(TETHER_OWNER));
        if (!(owner instanceof Player playerOwner)
                || !owner.isAlive()
                || serverLevel.getGameTime() >= data.getLong(TETHER_UNTIL)) {
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

    @SubscribeEvent
    public static void tryEscapeTether(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide() && isTethered(entity) && roll(entity, 0.30F)) {
            clearTether(entity);
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

        CompoundTag data = target.getPersistentData();
        data.putUUID(TETHER_OWNER, owner.getUUID());
        data.putLong(TETHER_UNTIL, owner.level().getGameTime() + TETHER_DURATION);
        target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(), TETHER_DURATION, 0, false, true));
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return countPieces(entity, ArmorFamily.FINE_SCALE) == 4;
    }

    private static void clearTether(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        data.remove(TETHER_OWNER);
        data.remove(TETHER_UNTIL);
    }

    private static boolean isTethered(LivingEntity entity) {
        return entity.getPersistentData().contains(TETHER_UNTIL);
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
        mount.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 1, true, false));

        switch (family) {
            case EARLY_MING_GUANG -> mount.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, true, false));
            case MIDDLE_MING_GUANG -> {
                mount.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, true, false));
                mount.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 1, true, false));
            }
            case LATE_MING_GUANG -> {
                mount.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 2, true, false));
                mount.addEffect(new MobEffectInstance(MobEffects.JUMP, 30, 1, true, false));
            }
            default -> {
            }
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

    /**
     * Minecraft 1.20.1 has no vanilla mace. This recognizes the 1.21 damage id
     * and common backport/mod item ids without introducing a hard dependency.
     */
    private static boolean isMaceSmash(DamageSource source) {
        String messageId = source.getMsgId().toLowerCase();
        if (messageId.contains("mace_smash") || messageId.contains("mace")) {
            return true;
        }

        if (source.getEntity() instanceof LivingEntity attacker && attacker.fallDistance > 0.0F) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(attacker.getMainHandItem().getItem());
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
            if (itemPath(armor).contains(family.itemPathMarker)) {
                count++;
            }
        }
        if (isMingGuang(family) && isMingGuangHelmetSubstitute(entity.getItemBySlot(EquipmentSlot.HEAD))) {
            count++;
        }
        return count;
    }

    private static boolean isMingGuangHelmetSubstitute(ItemStack helmet) {
        return helmet.is(ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.IRON_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.DIAMOND_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.IRON_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get())
                || helmet.is(ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get());
    }

    private static String itemPath(ItemStack stack) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key == null ? "" : key.getPath();
    }

    private static boolean roll(LivingEntity entity, float probability) {
        return entity.getRandom().nextFloat() < probability;
    }
}
