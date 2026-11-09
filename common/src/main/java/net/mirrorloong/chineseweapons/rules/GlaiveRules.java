package net.mirrorloong.chineseweapons.rules;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Glaive spin skill ("get lost kill"): cooldown, damage pulses, exhaustion,
 * durability and the advancement are shared; targets only feed in the tick
 * event and post their client-side animation event.
 */
public final class GlaiveRules {

    public enum StartResult {
        REJECTED,
        /** Cooldown applied, client should start its animation. */
        CLIENT,
        /** Server accepted and the spin state machine is running. */
        SERVER
    }

    private static final int SKILL_DURATION_TICKS = 30;
    private static final int DAMAGE_PULSE_INTERVAL_TICKS = 11;
    private static final int DAMAGE_PULSE_COUNT = 3;
    private static final int COOLDOWN_TICKS = 20;
    private static final int DURABILITY_COST = 3;
    private static final float EXHAUSTION_COST = 5.0F;
    private static final float DAMAGE_BONUS = 2.0F;
    private static final double ATTACK_LENGTH = 3.0D;
    private static final double ATTACK_HALF_WIDTH = 1.5D;
    private static final double ATTACK_HALF_HEIGHT = 1.5D;
    private static final int TIP_THROTTLE_TICKS = 10;

    private static final Map<UUID, ActiveSpin> ACTIVE_SPINS = new HashMap<>();
    private static final Map<UUID, Integer> LAST_TIP_TICK = new HashMap<>();

    private GlaiveRules() {
    }

    public static StartResult tryStart(Level world, Player player, boolean mainHand, boolean shiftDown, ItemStack itemStack) {
        if (!mainHand || !shiftDown || itemStack.isEmpty()) {
            return StartResult.REJECTED;
        }

        if (ChineseWeapons.gameplay().isOnCooldown(player, itemStack)) {
            if (ChineseWeapons.gameplay().isClientSide(world)) {
                float remainTicks = ChineseWeapons.gameplay().cooldownPercent(player, itemStack, 1.0F) * COOLDOWN_TICKS;
                double remainSec = remainTicks / 20.0D;
                int currentTick = player.tickCount;

                Integer lastTip = LAST_TIP_TICK.get(player.getUUID());
                if (lastTip == null || currentTick - lastTip > TIP_THROTTLE_TICKS) {
                    LAST_TIP_TICK.put(player.getUUID(), currentTick);
                    String timeStr = String.format("%.2f", remainSec);
                    Component message = ChineseWeapons.gameplay().translatableWithStyle(
                            "item.chineseweapons.GlaiveItemGetLostKill", ChatFormatting.GOLD, timeStr);
                    ChineseWeapons.gameplay().sendOverlayMessage(player, message);
                }
            }
            return StartResult.REJECTED;
        }

        ChineseWeapons.gameplay().addCooldown(player, itemStack, COOLDOWN_TICKS);
        if (ChineseWeapons.gameplay().isClientSide(world)) {
            return StartResult.CLIENT;
        }

        float totalDamage = (float) ChineseWeapons.gameplay().attributeValue(player,
                net.mirrorloong.chineseweapons.spi.GameplayCompat.AttributeKind.ATTACK_DAMAGE) - DAMAGE_BONUS;
        ActiveSpin activeSpin = new ActiveSpin(
                ChineseWeapons.gameplay().selectedSlot(player),
                itemStack.getItem(),
                totalDamage,
                totalDamage / DAMAGE_PULSE_COUNT
        );
        ACTIVE_SPINS.put(player.getUUID(), activeSpin);
        damageTargetsInFront(world, player, activeSpin.damageForCurrentPulse());
        activeSpin.markPulseApplied();
        player.causeFoodExhaustion(EXHAUSTION_COST);
        ChineseWeapons.gameplay().damageItem(itemStack, DURABILITY_COST, player);

        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            ChineseWeapons.gameplay().grantAdvancement(serverPlayer, "get_glaive/use_gs");
        }

        return StartResult.SERVER;
    }

    /** Server-side player tick for an active spin. */
    public static void tick(Player player) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(player))) {
            return;
        }

        ActiveSpin activeSpin = ACTIVE_SPINS.get(player.getUUID());
        if (activeSpin == null) {
            return;
        }
        if (!activeSpin.isStillUsingStartingGlaive(player)) {
            ACTIVE_SPINS.remove(player.getUUID());
            return;
        }

        activeSpin.advanceTick();
        if (activeSpin.shouldApplyPulse()) {
            damageTargetsInFront(ChineseWeapons.gameplay().level(player), player, activeSpin.damageForCurrentPulse());
            activeSpin.markPulseApplied();
        }
        if (activeSpin.isFinished()) {
            ACTIVE_SPINS.remove(player.getUUID());
        }
    }

    public static void onLogout(UUID uuid) {
        ACTIVE_SPINS.remove(uuid);
        LAST_TIP_TICK.remove(uuid);
    }

    private static void damageTargetsInFront(Level world, Player player, float damage) {
        Vec3 forward = player.getLookAngle().multiply(1.0D, 0.0D, 1.0D);
        if (forward.lengthSqr() < 1.0E-6D) {
            forward = Vec3.directionFromRotation(0.0F, player.getYRot()).multiply(1.0D, 0.0D, 1.0D);
        }
        forward = forward.normalize();

        AABB searchArea = player.getBoundingBox().inflate(
                ATTACK_LENGTH + ATTACK_HALF_WIDTH, ATTACK_HALF_HEIGHT, ATTACK_LENGTH + ATTACK_HALF_WIDTH);
        Vec3 attackForward = forward;
        List<LivingEntity> targets = world.getEntitiesOfClass(
                LivingEntity.class,
                searchArea,
                target -> target != player && target.isAlive() && isInsideAttackArea(player, target, attackForward)
        );

        for (LivingEntity target : targets) {
            ChineseWeapons.gameplay().hurt(target, player, damage);
        }
    }

    private static boolean isInsideAttackArea(Player player, LivingEntity target, Vec3 forward) {
        Vec3 offset = target.position().subtract(player.position());
        double forwardDistance = offset.x * forward.x + offset.z * forward.z;
        double lateralDistance = Math.abs(offset.x * -forward.z + offset.z * forward.x);
        double verticalDistance = Math.abs(offset.y);
        return forwardDistance >= 0.0D
                && forwardDistance <= ATTACK_LENGTH
                && lateralDistance <= ATTACK_HALF_WIDTH
                && verticalDistance <= ATTACK_HALF_HEIGHT;
    }

    private static final class ActiveSpin {
        private final int startingSlot;
        private final Item startingItem;
        private final float totalDamage;
        private final float regularPulseDamage;
        private int elapsedTicks;
        private int appliedPulses;

        private ActiveSpin(int startingSlot, Item startingItem, float totalDamage, float regularPulseDamage) {
            this.startingSlot = startingSlot;
            this.startingItem = startingItem;
            this.totalDamage = totalDamage;
            this.regularPulseDamage = regularPulseDamage;
        }

        private boolean isStillUsingStartingGlaive(Player player) {
            return player.isAlive()
                    && ChineseWeapons.gameplay().selectedSlot(player) == this.startingSlot
                    && !player.getMainHandItem().isEmpty()
                    && player.getMainHandItem().getItem() == this.startingItem;
        }

        private void advanceTick() {
            this.elapsedTicks++;
        }

        private boolean shouldApplyPulse() {
            return this.appliedPulses < DAMAGE_PULSE_COUNT
                    && this.elapsedTicks == this.appliedPulses * DAMAGE_PULSE_INTERVAL_TICKS;
        }

        private float damageForCurrentPulse() {
            if (this.appliedPulses == DAMAGE_PULSE_COUNT - 1) {
                return this.totalDamage - this.regularPulseDamage * (DAMAGE_PULSE_COUNT - 1);
            }
            return this.regularPulseDamage;
        }

        private void markPulseApplied() {
            this.appliedPulses++;
        }

        private boolean isFinished() {
            return this.elapsedTicks >= SKILL_DURATION_TICKS;
        }
    }
}
