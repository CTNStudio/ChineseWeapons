package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.model.Modelbed_crossbow;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

public class BedCrossbowRenderer extends EntityRenderer<BedCrossbowEntity> {

    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/bed_crossbow.png");

    private final Modelbed_crossbow<BedCrossbowEntity> model;

    public BedCrossbowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new Modelbed_crossbow<>(context.bakeLayer(Modelbed_crossbow.LAYER_LOCATION));
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(BedCrossbowEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        LocalPlayer player = Minecraft.getInstance().player;
        float yaw = player != null && entity.isOperating(player)
                ? player.getYRot()
                : Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5D, 0.0D);

        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTick, 0.0F, 0.0F);
        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BedCrossbowEntity entity) {
        return TEXTURE;
    }
}
