package net.mirrorloong.chineseweapons.rules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * Armour combat rules (damage cancellation, damage adjustment, the ming-guang
 * movement/armour modifiers and the mounted-drag tether pull). Targets only
 * translate their loader's event into {@link IncomingDamage}.
 */
public final class CombatRules {

    public static final int TETHER_DURATION_TICKS = 5 * 20;

    private static final UUID MING_GUANG_SPEED_ID = UUID.fromString("1117a420-9d72-4fa4-89d0-825a318d3e17");
    private static final String MING_GUANG_SPEED_KEY = "ming_guang_armour_speed";
    private static final UUID MOUNT_ARMOR_ID = UUID.fromString("8f2c4d7a-3b1e-4c9a-9f6d-2a7b5c8e1d34");
    private static final String MOUNT_ARMOR_KEY = "ming_guang_mount_armour";

    private static final ThreadLocal<Boolean> REFLECTING_DAMAGE = ThreadLocal.withInitial(() -> false);

    private CombatRules() {
    }

    /**
     * Version-neutral view of a damage event. {@code arrow} already excludes
     * thrown tridents, matching the original {@code AbstractArrow} checks.
     */
    public record IncomingDamage(float amount,
                                 LivingEntity attacker,
                                 boolean arrow,
                                 boolean fireArrow,
                                 boolean swordOrTrident,
                                 boolean maceSmash,
                                 boolean sweetBerryBush,
                                 Runnable deflectArrow) {
    }

    /** Result of {@link #adjustDamage}: new amount plus whether to cancel outright. */
    public record Adjustment(float amount, boolean cancel) {
    }

    public static boolean isReflectingDamage() {
        return REFLECTING_DAMAGE.get();
    }

    public static boolean isMaceSmash(String messageId, LivingEntity attacker) {
        if (messageId == null) {
            return false;
        }
        String lower = messageId.toLowerCase();
        if (lower.contains("mace_smash") || lower.contains("mace")) {
            return true;
        }

        if (attacker != null && attacker.fallDistance > 0.0F) {
            String path = ChineseWeapons.gameplay().itemPath(attacker.getMainHandItem().getItem());
            return path.contains("mace") || path.contains("heavy_hammer");
        }
        return false;
    }

    public static boolean isSwordOrTrident(LivingEntity attacker, boolean thrownTrident) {
        if (thrownTrident) {
            return true;
        }
        return attacker != null && ChineseWeapons.gameplay().isSword(attacker.getMainHandItem());
    }

    /**
     * Cancel-side rules (Forge {@code LivingAttackEvent}, Fabric
     * {@code ALLOW_DAMAGE}, NeoForge {@code LivingIncomingDamageEvent}).
     *
     * <p>{@code includeFineScaleBerry} is true for the targets that cancel the
     * sweet-berry-bush hit at this layer; the Forge 1.18.2-1.20.1 targets keep
     * that cancel in {@link #adjustDamage} as before.</p>
     */
    public static boolean allowDamage(LivingEntity target,
                                      IncomingDamage damage,
                                      Predicate<ItemStack> armorGain,
                                      Predicate<LivingEntity> tethered,
                                      boolean includeFineScaleBerry) {
        if (isReflectingDamage()) {
            return true;
        }

        ArmorRules.Family targetFamily = ArmorRules.primaryFamily(target, armorGain);

        if (includeFineScaleBerry
                && targetFamily == ArmorRules.Family.FINE_SCALE
                && ArmorRules.countPieces(target, targetFamily, armorGain) == 4
                && damage.sweetBerryBush()) {
            return false;
        }

        if (targetFamily != null
                && ArmorRules.isMingGuang(targetFamily)
                && ArmorRules.countPieces(target, targetFamily, armorGain) == 4
                && damage.arrow()) {
            if (damage.fireArrow()) {
                return true;
            }

            if (damage.amount() <= 3.0F) {
                if (damage.deflectArrow() != null) {
                    damage.deflectArrow().run();
                }
                return false;
            }
            return true;
        }

        return damage.attacker() == null || !tethered.test(damage.attacker());
    }

    /**
     * Amount-adjustment rules (Forge {@code LivingHurtEvent}, NeoForge
     * {@code LivingDamageEvent.Pre}, Fabric {@code LivingEntityMixin}).
     */
    public static Adjustment adjustDamage(LivingEntity wearer,
                                          IncomingDamage damage,
                                          Predicate<ItemStack> armorGain) {
        float amount = damage.amount();
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(wearer)) || isReflectingDamage()) {
            return new Adjustment(amount, false);
        }

        ArmorRules.Family family = ArmorRules.primaryFamily(wearer, armorGain);
        if (family == null) {
            return new Adjustment(amount, false);
        }

        boolean fullSet = ArmorRules.countPieces(wearer, family, armorGain) == 4;
        float originalDamage = amount;

        if (family == ArmorRules.Family.FINE_SCALE && fullSet && damage.sweetBerryBush()) {
            return new Adjustment(0.0F, true);
        }

        switch (family) {
            case MOUNTAIN_CHARACTER -> {
                if (damage.swordOrTrident()) {
                    amount = Math.max(0.0F, amount - 3.0F);
                }
            }
            case FINE_SCALE -> {
                if (damage.swordOrTrident() && roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    amount = amount * 0.80F;
                }
            }
            case BLACK_CHUI -> {
                if (roll(wearer, fullSet ? 0.40F : 0.20F)) {
                    coolDownAttackersWeapon(damage.attacker(), 3 * 20);
                }

                if (damage.maceSmash()) {
                    if (roll(wearer, fullSet ? 1.00F : 0.50F)) {
                        amount = amount * 0.50F;
                    }
                } else if (damage.swordOrTrident() && roll(wearer, fullSet ? 0.60F : 0.30F)) {
                    amount = amount * 0.60F;
                }

                if (roll(wearer, fullSet ? 0.30F : 0.10F)) {
                    reflectDamage(wearer, damage.attacker(), originalDamage * 0.50F);
                }
            }
            case EARLY_MING_GUANG -> {
                if (roll(wearer, fullSet ? 0.20F : 0.10F)) {
                    coolDownAttackersWeapon(damage.attacker(), 2 * 20);
                }

                if (damage.maceSmash() && roll(wearer, fullSet ? 0.80F : 0.30F)) {
                    amount = amount * 0.80F;
                }

                if (fullSet && damage.arrow() && !damage.fireArrow() && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 1.0F);
                }
            }
            case MIDDLE_MING_GUANG -> {
                if (damage.swordOrTrident() && wearer.isShiftKeyDown()) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }

                if (fullSet && damage.arrow() && !damage.fireArrow() && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }
            }
            case LATE_MING_GUANG -> {
                if (damage.swordOrTrident() && wearer.isShiftKeyDown()) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }

                if (fullSet && damage.arrow() && !damage.fireArrow() && amount > 3.0F) {
                    amount = Math.max(0.0F, amount - 2.0F);
                }
            }
        }

        return new Adjustment(amount, false);
    }

    /** Server-side player tick: ming-guang speed bookkeeping and mount armour. */
    public static void playerTick(Player player, Predicate<ItemStack> armorGain) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(player))) {
            return;
        }

        ArmorRules.Family family = ArmorRules.primaryFamily(player, armorGain);
        boolean fullSet = family != null && ArmorRules.countPieces(player, family, armorGain) == 4;

        ChineseWeapons.gameplay().removeModifier(player, GameplayCompat.AttributeKind.MOVEMENT_SPEED,
                MING_GUANG_SPEED_KEY, MING_GUANG_SPEED_ID);

        if (!fullSet) {
            return;
        }

        if (ArmorRules.isMingGuang(family)
                && player.getVehicle() instanceof LivingEntity mount
                && player.tickCount % 10 == 0) {
            ChineseWeapons.gameplay().addModifier(mount, GameplayCompat.AttributeKind.ARMOR,
                    MOUNT_ARMOR_KEY, MOUNT_ARMOR_ID, 2.0D, GameplayCompat.ModifierOperation.ADD);
        }
    }

    public static boolean canStartTether(Player owner, LivingEntity target, Predicate<ItemStack> armorGain) {
        return !ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(owner))
                && owner != target
                && owner.getVehicle() != null
                && target.getVehicle() == null
                && !ArmorRules.isFullFineScaleSet(target, armorGain);
    }

    /**
     * Starts the mounted-drag tether when a mounted attacker hits a rider-less
     * target. Never re-tethers an entity that already is.
     */
    public static void startMountedDrag(LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(target))
                || !target.isAlive()) {
            return;
        }
        if (player.getVehicle() != null
                && target.getVehicle() == null
                && !ChineseWeapons.gameplay().isTethered(target)) {
            ChineseWeapons.gameplay().tryStartMountedDrag(player, target);
        }
    }

    /** Pulls a tethered entity to the anchor point behind its owner. */
    public static void pullTowardsOwner(LivingEntity target, Player playerOwner) {
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
        if (target instanceof net.minecraft.world.entity.Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }
    }

    private static void coolDownAttackersWeapon(LivingEntity attacker, int ticks) {
        if (attacker instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (!weapon.isEmpty()) {
                ChineseWeapons.gameplay().addCooldown(player, weapon, ticks);
            }
        }
    }

    private static void reflectDamage(LivingEntity defender, LivingEntity attacker, float amount) {
        if (attacker == null || attacker == defender || amount <= 0.0F) {
            return;
        }

        REFLECTING_DAMAGE.set(true);
        try {
            ChineseWeapons.gameplay().hurtThorns(attacker, defender, amount);
        } finally {
            REFLECTING_DAMAGE.set(false);
        }
    }

    private static boolean roll(LivingEntity entity, float probability) {
        return entity.getRandom().nextFloat() < probability;
    }
}
