package net.mirrorloong.chineseweapons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.item.GuDuoItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Deque;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Unique
    private final Deque<Integer> chineseweapons$squashedEntities = new ArrayDeque<>();

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
    )
    private void chineseweapons$squashStart(LivingEntity entity, float yaw, float tickDelta, PoseStack poseStack,
                                            MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        Long start = GuDuoItem.Client.pollStart(entity.getId());
        if (start == null) {
            return;
        }
        float scaleY = GuDuoItem.scaleY(System.currentTimeMillis() - start);
        if (scaleY >= 0.999F) {
            return;
        }
        poseStack.pushPose();
        poseStack.scale(1.0F, scaleY, 1.0F);
        this.chineseweapons$squashedEntities.push(entity.getId());
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("RETURN")
    )
    private void chineseweapons$squashEnd(LivingEntity entity, float yaw, float tickDelta, PoseStack poseStack,
                                          MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (!this.chineseweapons$squashedEntities.isEmpty()
                && this.chineseweapons$squashedEntities.peek().intValue() == entity.getId()) {
            this.chineseweapons$squashedEntities.pop();
            poseStack.popPose();
        }
    }
}
