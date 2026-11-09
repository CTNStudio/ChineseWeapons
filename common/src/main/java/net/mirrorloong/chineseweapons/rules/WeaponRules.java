package net.mirrorloong.chineseweapons.rules;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/** Shared dagger-axe / glaive swing helpers that are not tied to a loader event. */
public final class WeaponRules {

    private static final Random HIT_RANDOM = new Random();
    private static final Random STOP_RUN_RANDOM = new Random();

    private static final int STOP_RUN_COOLDOWN_TICKS = 30;
    private static final int STOP_RUN_ANIMATION_TICKS = 46;

    private static final Map<UUID, Integer> STOP_RUN_ANIMATION = new HashMap<>();

    private WeaponRules() {
    }

    public static boolean isGlaive(ItemStack stack) {
        return stack.getItem() instanceof GlaiveItem;
    }

    /** Moves the offhand stack into the inventory, dropping the remainder. */
    public static void clearOffhand(Player player) {
        if (player == null) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(player))) {
            return;
        }
        ItemStack offhand = player.getOffhandItem();
        if (offhand.isEmpty()) {
            return;
        }
        ItemStack toMove = offhand.copy();
        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        if (!player.addItem(toMove)) {
            player.drop(toMove, false);
        } else if (!toMove.isEmpty()) {
            player.drop(toMove, false);
        }
    }

    /**
     * Dagger-axe / glaive hit effect: rolls the {@code HIT_EFFECT} chance, applies
     * the cuts effect and awards the hit advancement.
     *
     * @param chance target-resolved script chance (0.80 fallback)
     */
    public static void applyHitEffect(Entity entity, ItemStack weapon, float chance) {
        if (entity == null) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(entity))) {
            return;
        }

        if (HIT_RANDOM.nextFloat() < chance && entity instanceof LivingEntity livingEntity) {
            ChineseWeapons.gameplay().applyEffect(livingEntity, ChineseWeaponsModEffects.CusEffectSupplier,
                    120, 0, true, true);

            if (livingEntity instanceof ServerPlayer hitPlayer) {
                ChineseWeapons.gameplay().grantAdvancement(hitPlayer,
                        "get_dagger_axe/dagger_axe_hook_has_cuts_xg");
            }
        }
    }

    /** Dagger-axe air slash: knockback, slow, chip damage, durability and cooldown. */
    public static void airSlash(LivingEntity attacker, LivingEntity victim) {
        if (attacker == null || victim == null || !victim.isAlive()) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(victim))) {
            return;
        }
        if (ChineseWeapons.gameplay().isOnGround(attacker)) {
            return;
        }

        if (!(attacker instanceof Player player)) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.isEmpty() || ChineseWeapons.gameplay().isOnCooldown(player, mainHand)) {
            return;
        }

        Vec3 lookVec = attacker.getLookAngle();
        victim.push(lookVec.x, 0.5, lookVec.z);
        ChineseWeapons.gameplay().applyEffect(victim, GameplayCompat.Effect.SLOWNESS, 60, 1, false, true);

        ChineseWeapons.gameplay().hurt(victim, player, 1.0F);

        ChineseWeapons.gameplay().damageItem(mainHand, 2, player);

        ChineseWeapons.gameplay().addCooldown(player, mainHand, 40);
    }

    /** Glaive right-click "stop run": cooldown, durability and the slow roll. */
    public static void stopRun(Entity target, Entity source, ItemStack itemStack) {
        if (target == null || source == null || itemStack == null) {
            return;
        }

        if (source.isShiftKeyDown()) {
            return;
        }

        if (source instanceof Player player) {
            if (ChineseWeapons.gameplay().isOnCooldown(player, itemStack)) {
                return;
            }

            ChineseWeapons.gameplay().addCooldown(player, itemStack, STOP_RUN_COOLDOWN_TICKS);

            if (itemStack.isDamageableItem()) {
                ChineseWeapons.gameplay().damageItem(itemStack, 2, player);
            }
        }

        STOP_RUN_ANIMATION.put(source.getUUID(), 0);

        boolean shouldApplyEffect;
        if (target instanceof Player player) {
            shouldApplyEffect = player.isSprinting() && STOP_RUN_RANDOM.nextDouble() < 0.5;
        } else {
            shouldApplyEffect = STOP_RUN_RANDOM.nextDouble() < 0.3;
        }

        if (shouldApplyEffect
                && target instanceof LivingEntity livingEntity
                && !ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(livingEntity))) {
            ChineseWeapons.gameplay().applyEffect(livingEntity, GameplayCompat.Effect.SLOWNESS, 40, 255, false, true);
        }
    }

    /** Advances and samples the stop-run animation curve for a player. */
    public static float stopRunAnimationProgress(UUID uuid) {
        Integer currentTick = STOP_RUN_ANIMATION.get(uuid);
        if (currentTick == null) {
            return 0.0F;
        }

        currentTick++;
        STOP_RUN_ANIMATION.put(uuid, currentTick);

        if (currentTick >= STOP_RUN_ANIMATION_TICKS) {
            STOP_RUN_ANIMATION.remove(uuid);
            return 0.0F;
        }

        return AnimationCurve.swingProgress(currentTick, STOP_RUN_ANIMATION_TICKS);
    }

    /** Shared 46-tick swing curve: 8 ticks out, hold, 8 ticks back. */
    public static final class AnimationCurve {
        private AnimationCurve() {
        }

        public static float swingProgress(int tick, int totalTicks) {
            if (tick <= 8) {
                return (float) tick / 8;
            }
            if (tick <= 38) {
                return 1.0F;
            }
            int retract = tick - 38;
            return 1.0F - ((float) retract / 8);
        }
    }
}
