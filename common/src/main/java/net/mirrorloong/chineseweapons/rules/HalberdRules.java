package net.mirrorloong.chineseweapons.rules;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

/**
 * Sky-piercing halberd hit rules: the sneak "fangtian hengzhan" knockback with
 * its bleed, durability, advancement and mounted-drag follow-up.
 */
public final class HalberdRules {

    public static final String ADVANCEMENT = "get_sky_piercing_halberd/use_fangtian_hengzhan";

    private static final double KNOCKBACK_STRENGTH = 2.0D;
    private static final int BLEED_TICKS = 5 * 20;
    private static final int DURABILITY_COST = 2;

    private HalberdRules() {
    }

    /** Runs after the target's {@code super.hurtEnemy} returned {@code true}. */
    public static void onHitEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(target))
                || !target.isAlive()) {
            return;
        }

        if (player.isShiftKeyDown()) {
            knockback(target, player);

            if (!ChineseWeapons.gameplay().hasEffect(target, ChineseWeaponsModEffects.BleedEffectSupplier)) {
                ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.BleedEffectSupplier,
                        BLEED_TICKS, 0, false, true);
            }

            ChineseWeapons.gameplay().damageItem(stack, DURABILITY_COST, player);

            if (player instanceof ServerPlayer serverPlayer) {
                ChineseWeapons.gameplay().grantAdvancement(serverPlayer, ADVANCEMENT);
            }
        }

        CombatRules.startMountedDrag(target, player);
    }

    private static void knockback(LivingEntity target, Player player) {
        double dx = player.getX() - target.getX();
        double dz = player.getZ() - target.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        if (distance > 0.01D) {
            dx /= distance;
            dz /= distance;
        } else {
            float yaw = player.getYRot();
            dx = -Math.sin(Math.toRadians(yaw));
            dz = Math.cos(Math.toRadians(yaw));
        }

        target.knockback(KNOCKBACK_STRENGTH, dx, dz);
    }
}
