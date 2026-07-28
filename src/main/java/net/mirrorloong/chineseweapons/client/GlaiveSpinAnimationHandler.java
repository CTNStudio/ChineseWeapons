package net.mirrorloong.chineseweapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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
    private static final int ROTATION_DURATION_TICKS = 30;
    private static final GlaiveSpinAnimationHandler INSTANCE = new GlaiveSpinAnimationHandler();

    private static boolean registered;
    private static boolean rotating;
    private static int rotationTimer;
    private static UUID rotatingPlayerId;
    private static int startingSlot = -1;
    private static Item startingItem;
    private static ItemStack renderedGlaive = ItemStack.EMPTY;

    private GlaiveSpinAnimationHandler() {
    }

    public static void start(Player player, ItemStack itemStack) {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(INSTANCE);
            registered = true;
        }

        rotating = true;
        rotationTimer = ROTATION_DURATION_TICKS;
        rotatingPlayerId = player.getUUID();
        startingSlot = player.getInventory().selected;
        startingItem = itemStack.getItem();
        renderedGlaive = itemStack.copy();
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

        rotationTimer--;
        if (rotationTimer <= 0) {
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

        event.setCanceled(true);
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees((ROTATION_DURATION_TICKS - rotationTimer) * 36.0F));
        renderGlaive(player, poseStack, event.getMultiBufferSource(), event.getPackedLight());
        poseStack.popPose();
    }

    private static boolean isStillHoldingStartingGlaive(Player player) {
        return player != null
                && player.isAlive()
                && player.getUUID().equals(rotatingPlayerId)
                && player.getInventory().selected == startingSlot
                && !player.getMainHandItem().isEmpty()
                && player.getMainHandItem().getItem() == startingItem;
    }

    private static void renderGlaive(Player player, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemDisplayContext displayContext = player.getMainArm() == HumanoidArm.RIGHT
                ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        Minecraft.getInstance().getItemRenderer().renderStatic(
                player,
                renderedGlaive,
                displayContext,
                false,
                poseStack,
                buffer,
                player.level(),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                player.getId()
        );
    }

    private static void stop() {
        rotating = false;
        rotationTimer = 0;
        rotatingPlayerId = null;
        startingSlot = -1;
        startingItem = null;
        renderedGlaive = ItemStack.EMPTY;
    }
}
