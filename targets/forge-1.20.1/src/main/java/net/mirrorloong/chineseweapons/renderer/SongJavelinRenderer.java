package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mirrorloong.chineseweapons.client.model.ForgeModelFactory;
import net.mirrorloong.chineseweapons.client.render.EntityRenderData;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;

public class SongJavelinRenderer extends EntityRenderer<SongJavelinProjectile> {

    private final ModelPart root;

    public SongJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.root = ForgeModelFactory.INSTANCE.bake(EntityModelSpecs.JAVELIN);
    }

    @Override
    public void render(SongJavelinProjectile entity, float yaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        // 旋转指向飞行方向
        poseStack.mulPose(Axis.YP.rotationDegrees(
                Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) + EntityRenderData.JAVELIN_YAW_OFFSET));
        poseStack.mulPose(Axis.ZP.rotationDegrees(
                Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + EntityRenderData.JAVELIN_PITCH_OFFSET));
        poseStack.scale(EntityRenderData.JAVELIN_SCALE, EntityRenderData.JAVELIN_SCALE,
                EntityRenderData.JAVELIN_SCALE);

        VertexConsumer vertex = buffer.getBuffer(RenderType.entityCutoutNoCull(entity.getTextureLocation()));
        this.root.render(poseStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SongJavelinProjectile entity) {
        return entity.getTextureLocation();
    }
}
