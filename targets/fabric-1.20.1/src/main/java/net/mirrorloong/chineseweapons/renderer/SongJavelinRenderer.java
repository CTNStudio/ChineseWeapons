package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mirrorloong.chineseweapons.client.model.ModelSongJavelinModel;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;

public class SongJavelinRenderer extends EntityRenderer<SongJavelinProjectile> {

    private final ModelSongJavelinModel<SongJavelinProjectile> model;

    public SongJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelSongJavelinModel<>(context.bakeLayer(ModelSongJavelinModel.LAYER_LOCATION));
    }

    @Override
    public void render(SongJavelinProjectile entity, float yaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        // 旋转指向飞行方向
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);

        VertexConsumer vertex = buffer.getBuffer(this.model.renderType(entity.getTextureLocation()));
        this.model.renderToBuffer(poseStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SongJavelinProjectile entity) {
        return entity.getTextureLocation();
    }
}