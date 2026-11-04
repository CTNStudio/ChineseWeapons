package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GlaiveItemStopRunProcedure {
    private static final Map<UUID, Integer> animationTicks = new HashMap<>();
    private static final int COOLDOWN_TICKS = 30;
    private static final int DH_TICK = 46;
    private static final Random RANDOM = new Random();

    public static void execute(Entity target, Entity source, ItemStack itemstack) {
        if (target == null || source == null || itemstack == null) return;

        if (source.isShiftKeyDown()) return;

        UUID sourceUUID = source.getUUID();

        if (source instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
                return;
            }

            player.getCooldowns().addCooldown(itemstack.getItem(), COOLDOWN_TICKS);

            if (itemstack.isDamageableItem()) {
                itemstack.hurtAndBreak(2, player, (p) -> {
                    p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                });
            }
        }

        animationTicks.put(sourceUUID, 0);

        boolean shouldApplyEffect = false;

        if (target instanceof Player player) {
            if (player.isSprinting()) {
                shouldApplyEffect = RANDOM.nextDouble() < 0.5;
            }
        } else {
            shouldApplyEffect = RANDOM.nextDouble() < 0.3;
        }

        if (shouldApplyEffect) {
            if (target instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 255, false, true));
            }
        }
    }

    public static float getAnimationProgress(UUID uid) {
        Integer currentTick = animationTicks.get(uid);
        if (currentTick == null) return 0.0F;

        currentTick++;
        animationTicks.put(uid, currentTick);

        if (currentTick >= DH_TICK) {
            animationTicks.remove(uid);
            return 0.0F;
        }

        if (currentTick <= 8) {
            return (float) currentTick / 8;
        }
        if (currentTick <= 38) {
            return 1.0F;
        }

        int retractPass = currentTick - 38;
        return 1.0F - ((float) retractPass / 8);
    }
}