package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GlaiveItemGetLostKillProcedure {
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
    private static final Map<UUID, ActiveSpin> ACTIVE_SPINS = new HashMap<>();
    private static final Map<UUID, Integer> LAST_TIP_TICK = new HashMap<>();
    private static final int TIP_THROTTLE_TICKS = 10;

    private GlaiveItemGetLostKillProcedure() {
    }

    public static boolean tryStart(Level world, Player player, InteractionHand hand, ItemStack itemStack) {
        if (hand != InteractionHand.MAIN_HAND || !player.isShiftKeyDown() || itemStack.isEmpty()) {
            return false;
        }

        if (player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            if (world.isClientSide()) {
                float remainTicks = player.getCooldowns().getCooldownPercent(itemStack.getItem(), 1.0F) * COOLDOWN_TICKS;
                double remainSec = remainTicks / 20.0D;
                int currentTick = player.tickCount;

                if (!LAST_TIP_TICK.containsKey(player.getUUID()) ||
                        currentTick - LAST_TIP_TICK.get(player.getUUID()) > TIP_THROTTLE_TICKS) {
                    LAST_TIP_TICK.put(player.getUUID(), currentTick);
                    String timeStr = String.format("%.2f", remainSec);
                    Component message = Component.translatable("item.chineseweapons.GlaiveItemGetLostKill", timeStr)
                            .withStyle(ChatFormatting.GOLD);
                    player.displayClientMessage(message, true);
                }
            }
            return false;
        }

        player.getCooldowns().addCooldown(itemStack.getItem(), COOLDOWN_TICKS);
        if (world.isClientSide()) {
            MinecraftForge.EVENT_BUS.post(new GlaiveSpinStartedEvent(player, itemStack.copy()));
            return true;
        }

        float totalDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - DAMAGE_BONUS;
        ActiveSpin activeSpin = new ActiveSpin(
                player.getInventory().selected,
                itemStack.getItem(),
                totalDamage,
                totalDamage / DAMAGE_PULSE_COUNT
        );
        ACTIVE_SPINS.put(player.getUUID(), activeSpin);
        damageTargetsInFront(world, player, activeSpin.damageForCurrentPulse());
        activeSpin.markPulseApplied();
        player.causeFoodExhaustion(EXHAUSTION_COST);
        itemStack.hurtAndBreak(DURABILITY_COST, player, owner -> owner.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        return true;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }

        Player player = event.player;
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
            damageTargetsInFront(player.level(), player, activeSpin.damageForCurrentPulse());
            activeSpin.markPulseApplied();
        }
        if (activeSpin.isFinished()) {
            ACTIVE_SPINS.remove(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        ACTIVE_SPINS.remove(uuid);
        LAST_TIP_TICK.remove(uuid);
    }

    private static void damageTargetsInFront(Level world, Player player, float damage) {
        Vec3 forward = player.getLookAngle().multiply(1.0D, 0.0D, 1.0D);
        if (forward.lengthSqr() < 1.0E-6D) {
            forward = Vec3.directionFromRotation(0.0F, player.getYRot()).multiply(1.0D, 0.0D, 1.0D);
        }
        forward = forward.normalize();

        AABB searchArea = player.getBoundingBox().inflate(ATTACK_LENGTH + ATTACK_HALF_WIDTH, ATTACK_HALF_HEIGHT, ATTACK_LENGTH + ATTACK_HALF_WIDTH);
        Vec3 attackForward = forward;
        List<LivingEntity> targets = world.getEntitiesOfClass(
                LivingEntity.class,
                searchArea,
                target -> target != player && target.isAlive() && isInsideAttackArea(player, target, attackForward)
        );

        for (LivingEntity target : targets) {
            target.hurt(player.damageSources().playerAttack(player), damage);
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
                    && player.getInventory().selected == this.startingSlot
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
