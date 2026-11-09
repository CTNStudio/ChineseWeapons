package net.mirrorloong.chineseweapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public final class GlaiveSpinAnimationHandler {
    private static final int ANIMATION_DURATION_TICKS = 30;

    // The action is intentionally split into readable phases so the first-person
    // pose can be tuned without changing the server-side skill timing.
    private static final float ADVANCE_END = 0.18F;
    private static final float DOWNWARD_END = 0.30F;
    private static final float SPIN_END = 0.86F;
    private static final float FORWARD_OFFSET = -0.28F;
    private static final float DOWNWARD_OFFSET = -0.12F;
    private static final float DOWNWARD_ROTATION = -45.0F;
    private static final float FULL_SPIN = -360.0F;
    private static final float SPIN_SCALE = 1.25F;
    private static final float HAND_PIVOT_X = 0.0F;
    private static final float HAND_PIVOT_Y = -0.24F;
    private static final float HAND_PIVOT_Z = 0.10F;
    private static final GlaiveSpinAnimationHandler INSTANCE = new GlaiveSpinAnimationHandler();

    private static boolean registered;
    private static boolean rotating;
    private static int elapsedTicks;
    private static UUID rotatingPlayerId;
    private static int startingSlot = -1;
    private static Item startingItem;

    private GlaiveSpinAnimationHandler() {
    }

    public static void start(Player player, ItemStack itemStack) {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(INSTANCE);
            registered = true;
        }

        rotating = true;
        elapsedTicks = 0;
        rotatingPlayerId = player.getUUID();
        startingSlot = player.getInventory().selected;
        startingItem = itemStack.getItem();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !rotating) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (!isStillHoldingStartingGlaive(player)) {
            stop();
            return;
        }

        elapsedTicks++;
        if (elapsedTicks >= ANIMATION_DURATION_TICKS) {
            stop();
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (!rotating || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (!isStillHoldingStartingGlaive(player)) {
            stop();
            return;
        }

        // Keep vanilla hand and item rendering. The event's pose stack is used by
        // the vanilla renderer immediately after this callback.
        applyAnimation(event.getPoseStack(), player,
                Mth.clamp((elapsedTicks + event.getPartialTick()) / ANIMATION_DURATION_TICKS, 0.0F, 1.0F));
    }

    private static boolean isStillHoldingStartingGlaive(Player player) {
        return player != null
                && player.isAlive()
                && player.getUUID().equals(rotatingPlayerId)
                && player.getInventory().selected == startingSlot
                && !player.getMainHandItem().isEmpty()
                && player.getMainHandItem().getItem() == startingItem;
    }

    private static void applyAnimation(PoseStack poseStack, Player player, float progress) {
        float advance = smoothStep(progress / ADVANCE_END);
        float downward = smoothStep((progress - ADVANCE_END) / (DOWNWARD_END - ADVANCE_END));
        float spin = smoothStep((progress - DOWNWARD_END) / (SPIN_END - DOWNWARD_END));
        float recover = smoothStep((progress - SPIN_END) / (1.0F - SPIN_END));
        float handSign = player.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;

        float moveProgress = advance - recover;
        float downwardRotation = DOWNWARD_ROTATION * downward * (1.0F - recover);
        float spinRotation = FULL_SPIN * spin * handSign;
        float scaleProgress = spin * (1.0F - recover);
        float scale = Mth.lerp(scaleProgress, 1.0F, SPIN_SCALE);

        poseStack.translate(0.0D, DOWNWARD_OFFSET * moveProgress, FORWARD_OFFSET * moveProgress);

        // Rotate around the hand instead of the camera origin. This keeps the
        // glaive attached to the grip while the blade sweeps through the view.
        poseStack.translate(HAND_PIVOT_X * handSign, HAND_PIVOT_Y, HAND_PIVOT_Z);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(downwardRotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(spinRotation));
        poseStack.translate(-HAND_PIVOT_X * handSign, -HAND_PIVOT_Y, -HAND_PIVOT_Z);
    }

    private static float smoothStep(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private static void stop() {
        rotating = false;
        elapsedTicks = 0;
        rotatingPlayerId = null;
        startingSlot = -1;
        startingItem = null;
    }
}
