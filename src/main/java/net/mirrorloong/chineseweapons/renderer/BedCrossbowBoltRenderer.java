package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mirrorloong.chineseweapons.client.model.Modelbed_crossbow_bolt;
import net.mirrorloong.chineseweapons.entity.BedCrossbowBoltEntity;

public class BedCrossbowBoltRenderer extends EntityRenderer<BedCrossbowBoltEntity> {

    private final Modelbed_crossbow_bolt<BedCrossbowBoltEntity> model;

    public BedCrossbowBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new Modelbed_crossbow_bolt<>(context.bakeLayer(Modelbed_crossbow_bolt.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(BedCrossbowBoltEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float yaw = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -0.135D, 1.156D);

        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(entity.getTextureLocation())),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BedCrossbowBoltEntity entity) {
        return entity.getTextureLocation();
    }
}
