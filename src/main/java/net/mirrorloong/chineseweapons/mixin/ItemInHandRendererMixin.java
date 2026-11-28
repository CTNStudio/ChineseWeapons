package net.mirrorloong.chineseweapons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.client.event.HandRenderEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(
            method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chineseweapons$renderHand(AbstractClientPlayer player, float frameInterp, float pitch,
                                           InteractionHand hand, float swingProgress, ItemStack stack,
                                           float equipProgress, PoseStack poseStack,
                                           MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (player.isScoping()) {
            return;
        }
        float partialTick = Minecraft.getInstance().getFrameTime();
        if (HandRenderEvents.invokeAll(poseStack, player, hand, stack, partialTick, buffer, combinedLight)) {
            ci.cancel();
        }
    }
}
