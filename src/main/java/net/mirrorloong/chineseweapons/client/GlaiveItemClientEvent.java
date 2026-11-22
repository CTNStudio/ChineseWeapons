package net.mirrorloong.chineseweapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;

import java.util.UUID;

public final class GlaiveItemClientEvent {
        private static final float ITEM_OFFSET = 3.0F / 16.0F;
        private static final float ROTATION = -30.0F;

        private GlaiveItemClientEvent() {
        }

        //old api @SubscribeEvent modifyItemRender(RenderHandEvent event) -> new api HandRenderEvents.Handler
                public static boolean modifyItemRender(PoseStack poseStack, Player player, InteractionHand hand,
                                               ItemStack stack, float partialTick) {
                        if (player == null) {
                return false;
            }

            UUID uuid = player.getUUID();
            float progress = GlaiveItemStopRunProcedure.getAnimationProgress(uuid);
            if (progress <= 0.0F) {
                return false;
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(ROTATION * progress));
            poseStack.translate(0, 0, ITEM_OFFSET * progress);
            return false;
        }
}
