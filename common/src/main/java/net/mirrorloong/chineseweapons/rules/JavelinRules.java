package net.mirrorloong.chineseweapons.rules;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;

/**
 * Song javelin throw behaviour: riptide gate, charge curve, durability,
 * cooldown, throw sound and the loyalty/channeling payload handed to the
 * target's projectile class.
 *
 * <p>The projectile entity itself stays in each target because its constructors
 * and {@code AbstractArrow} setters changed between versions; the target
 * supplies a {@link ProjectileSpawner} that applies the {@link Shot}.</p>
 */
public final class JavelinRules {

    public static final int USE_DURATION_TICKS = 72000;

    private static final int MIN_CHARGE_TICKS = 5;
    private static final float FULL_CHARGE_TICKS = 20.0F;
    private static final float CRITICAL_CHARGE = 0.9F;
    private static final int DURABILITY_COST = 2;
    private static final int THROW_COOLDOWN_TICKS = 8;
    private static final int RIPTIDE_COOLDOWN_TICKS = 20;
    private static final double RIPTIDE_BASE_FACTOR = 0.75D;

    private JavelinRules() {
    }

    public enum UseResult {
        FAIL,
        CONSUME
    }

    /**
     * @param baseDamage  projectile base damage (attack damage times charge)
     * @param velocity    launch velocity
     * @param critical    whether the charge crosses the crit threshold
     * @param loyalty     loyalty level read from the stack
     * @param channeling  whether channeling is present
     * @param pierce      piercing level read from the stack
     */
    public record Shot(float baseDamage, float velocity, boolean critical, int loyalty,
                       boolean channeling, int pierce) {
    }

    /** Applies the shared shot data to the target's concrete projectile. */
    public interface ProjectileSpawner {
        void spawn(Shot shot, ItemStack stack);
    }

    /** Shared right-click gate; the target maps the result to its result type. */
    public static UseResult use(Player player, InteractionHand hand, ItemStack stack) {
        Level level = ChineseWeapons.gameplay().level(player);
        if (ChineseWeapons.gameplay().enchantmentLevel(level, stack, GameplayCompat.Enchantment.RIPTIDE) > 0
                && !player.isInWaterOrRain()) {
            return UseResult.FAIL;
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return UseResult.FAIL;
        }

        player.startUsingItem(hand);
        return UseResult.CONSUME;
    }

    /**
     * Shared release behaviour.
     *
     * @return whether anything was released/used
     */
    public static boolean releaseUsing(Player player, ItemStack stack, int usedTicks,
                                       float attackDamage, float attackSpeed,
                                       ProjectileSpawner spawner) {
        if (usedTicks < MIN_CHARGE_TICKS) {
            return false;
        }

        Level level = ChineseWeapons.gameplay().level(player);
        int riptideLevel = ChineseWeapons.gameplay().enchantmentLevel(
                level, stack, GameplayCompat.Enchantment.RIPTIDE);
        if (riptideLevel > 0 && player.isInWaterOrRain()) {
            performRiptide(player, stack, riptideLevel);
            return true;
        }

        float charge = Math.min(usedTicks / FULL_CHARGE_TICKS, 1.0F);
        float velocity = attackSpeed * charge;

        if (!ChineseWeapons.gameplay().isClientSide(level)) {
            boolean infinite = player.getAbilities().instabuild;
            if (!infinite) {
                ChineseWeapons.gameplay().damageItemInUsedHand(stack, DURABILITY_COST, player);
            }

            Shot shot = new Shot(
                    attackDamage * charge,
                    velocity,
                    charge >= CRITICAL_CHARGE,
                    ChineseWeapons.gameplay().enchantmentLevel(
                            level, stack, GameplayCompat.Enchantment.LOYALTY),
                    ChineseWeapons.gameplay().enchantmentLevel(
                            level, stack, GameplayCompat.Enchantment.CHANNELING) > 0,
                    ChineseWeapons.gameplay().enchantmentLevel(
                            level, stack, GameplayCompat.Enchantment.PIERCING)
            );
            spawner.spawn(shot, stack.copy());

            BlockPos soundPos = player.blockPosition();
            ChineseWeapons.gameplay().playSound(level,
                    soundPos.getX() + 0.5D, soundPos.getY() + 0.5D, soundPos.getZ() + 0.5D,
                    GameplayCompat.Sound.TRIDENT_THROW, 1.0F, 1.0F);

            ChineseWeapons.gameplay().addCooldown(player, stack, THROW_COOLDOWN_TICKS);
            if (!infinite) {
                stack.shrink(1);
            }
        }

        return true;
    }

    private static void performRiptide(Player player, ItemStack stack, int riptideLevel) {        Level level = ChineseWeapons.gameplay().level(player);
        if (ChineseWeapons.gameplay().isClientSide(level)) {
            return;
        }

        Vec3 look = player.getLookAngle();
        Vec3 currentVelocity = player.getDeltaMovement();
        double factor = RIPTIDE_BASE_FACTOR * (riptideLevel + 1);
        player.setDeltaMovement(look.scale(factor).add(currentVelocity));
        player.hurtMarked = true;

        ChineseWeapons.gameplay().damageItemInUsedHand(stack, DURABILITY_COST, player);

        GameplayCompat.Sound sound = switch (riptideLevel) {
            case 1 -> GameplayCompat.Sound.TRIDENT_RIPTIDE_1;
            case 2 -> GameplayCompat.Sound.TRIDENT_RIPTIDE_2;
            default -> GameplayCompat.Sound.TRIDENT_RIPTIDE_3;
        };
        BlockPos soundPos = player.blockPosition();
        ChineseWeapons.gameplay().playSound(level,
                soundPos.getX() + 0.5D, soundPos.getY() + 0.5D, soundPos.getZ() + 0.5D,
                sound, 1.0F, 1.0F);

        ChineseWeapons.gameplay().addCooldown(player, stack, RIPTIDE_COOLDOWN_TICKS);
    }

}
