package net.mirrorloong.chineseweapons.rules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.ModSounds;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.network.SquashPacket;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gu-duo (battle mace) hit rules: the ground/sneak/air attack split, bleed and
 * blindness, riding knock-off, the squash packet and its client-side timing
 * curve.
 *
 * <p>The numeric thresholds come from the target's script settings; they are
 * passed in as {@link Settings} so this class stays version-neutral.</p>
 */
public final class GuDuoRules {

    public static final long SQUASH_RISE_MS = 300L;
    public static final long SQUASH_TOTAL_MS = 800L;
    public static final float SQUASH_MIN_SCALE = 0.6F;

    /** Client-side squash start times, keyed by entity id. */
    public static final Map<Integer, Long> SQUASH = new ConcurrentHashMap<>();

    private static final int BLEED_TICKS = 60;
    private static final int BLINDNESS_TICKS = 60;
    private static final int SLOWNESS_TICKS = 100;
    private static final int RIDING_KNOCKBACK_COOLDOWN_TICKS = 60;
    private static final float SOUND_VOLUME = 0.2F;
    private static final float SOUND_PITCH = 1.0F;
    private static final double SQUASH_BROADCAST_RADIUS_SQR = 4096.0D;
    private static final double FALL_DOWN_MIN = 0.3D;
    private static final double FALL_DOWN_MIN_RANGE = 0.1D;

    private GuDuoRules() {
    }

    /**
     * @param armorBlindnessThreshold armour value at or below which the bleed
     *                                blinds (4.0 fallback)
     * @param downwardThreshold       upper bound of the extra downward push
     *                                (1.0 fallback)
     * @param hurtCooldownTicks       cooldown after the air attack (10 fallback)
     * @param stopRidingChance        chance to knock a rider off (0.30 fallback)
     */
    public record Settings(double armorBlindnessThreshold, double downwardThreshold,
                           int hurtCooldownTicks, float stopRidingChance) {
    }

    /** Runs the hit behaviour of one gu-duo strike (server side only). */
    public static void onHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                   Settings settings) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(attacker))) {
            return;
        }
        if (!(attacker instanceof Player player)
                || ChineseWeapons.gameplay().isOnCooldown(player, stack)) {
            return;
        }

        if (!ChineseWeapons.gameplay().isOnGround(attacker)) {
            airAttack(stack, target, attacker, settings);
        } else if (attacker.isShiftKeyDown()) {
            stopRidingAttack(stack, target, attacker, settings);
        } else {
            bleedAttack(stack, target, attacker, settings);
        }
    }

    private static void bleedAttack(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                    Settings settings) {
        ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.BleedEffectSupplier,
                BLEED_TICKS, 0, false, true);

        double threshold = settings.armorBlindnessThreshold();
        if (ChineseWeapons.gameplay().attributeValue(attacker, GameplayCompat.AttributeKind.ARMOR) <= threshold) {
            ChineseWeapons.gameplay().applyEffect(target, GameplayCompat.Effect.BLINDNESS,
                    BLINDNESS_TICKS, 0, false, true);
        }
    }

    private static void airAttack(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                  Settings settings) {
        Level level = ChineseWeapons.gameplay().level(attacker);
        BlockPos soundPos = attacker.blockPosition();
        ChineseWeapons.gameplay().playSound(level,
                soundPos.getX() + 0.5D, soundPos.getY() + 0.5D, soundPos.getZ() + 0.5D,
                ModSounds.GUDUO, SOUND_VOLUME, SOUND_PITCH);

        float weaponDamage = attacker instanceof Player player
                ? (float) ChineseWeapons.gameplay().attributeValue(player,
                        GameplayCompat.AttributeKind.ATTACK_DAMAGE)
                : 2.0F;
        ChineseWeapons.gameplay().hurtMob(target, attacker, weaponDamage + 2.0F);

        ChineseWeapons.gameplay().applyEffect(target, GameplayCompat.Effect.SLOWNESS,
                SLOWNESS_TICKS, 0, false, true);

        if (target.isInWater()) {
            target.setAirSupply(target.getAirSupply() - 3);
            target.push(0.0D, -4.0D, 0.0D);
        } else {
            Vec3 direction = target.position().subtract(attacker.position());
            Vec3 back = new Vec3(direction.x, 0.0D, direction.z).normalize().scale(1.0D);

            target.teleportTo(target.getX() + back.x, target.getY() + 1.5D, target.getZ() + back.z);

            double downwardRange = settings.downwardThreshold();
            double downward = FALL_DOWN_MIN
                    + ChineseWeapons.gameplay().nextDouble(level) * (downwardRange - FALL_DOWN_MIN_RANGE);
            target.push(0.0D, -downward, 0.0D);

            broadcastSquash(target);
        }

        if (attacker instanceof Player player) {
            ChineseWeapons.gameplay().addCooldown(player, stack, settings.hurtCooldownTicks());
        }
    }

    private static void broadcastSquash(LivingEntity target) {
        if (!(ChineseWeapons.gameplay().level(target) instanceof ServerLevel serverLevel)) {
            return;
        }
        SquashPacket packet = new SquashPacket(target.getId());
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(target) < SQUASH_BROADCAST_RADIUS_SQR) {
                ChineseWeapons.network().sendToPlayer(player, packet);
            }
        }
    }

    private static void stopRidingAttack(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                         Settings settings) {
        if (!target.isPassenger()) {
            return;
        }

        Level level = ChineseWeapons.gameplay().level(attacker);
        if (ChineseWeapons.gameplay().nextFloat(level) >= settings.stopRidingChance()) {
            return;
        }

        target.stopRiding();

        float damage = attacker instanceof Player player
                ? (float) ChineseWeapons.gameplay().attributeValue(player,
                        GameplayCompat.AttributeKind.ATTACK_DAMAGE)
                : 1.0F;
        ChineseWeapons.gameplay().hurtMob(target, attacker, damage);

        if (attacker instanceof Player player) {
            ChineseWeapons.gameplay().addCooldown(player, stack, RIDING_KNOCKBACK_COOLDOWN_TICKS);
        }

        if (target.getVehicle() instanceof LivingEntity vehicle) {
            ChineseWeapons.gameplay().hurtMob(vehicle, attacker, damage);
        }
    }

    /** Client squash curve: ease-out rise, then ease-in return to full scale. */
    public static float squashScaleY(long elapsed) {
        if (elapsed < SQUASH_RISE_MS) {
            float progress = elapsed / (float) SQUASH_RISE_MS;
            float ease = 1.0F - (float) Math.pow(1.0F - progress, 3);
            return 1.0F - (1.0F - SQUASH_MIN_SCALE) * ease;
        }
        float progress = (elapsed - SQUASH_RISE_MS) / (float) (SQUASH_TOTAL_MS - SQUASH_RISE_MS);
        return SQUASH_MIN_SCALE + (1.0F - SQUASH_MIN_SCALE) * (progress * progress * progress);
    }
}
