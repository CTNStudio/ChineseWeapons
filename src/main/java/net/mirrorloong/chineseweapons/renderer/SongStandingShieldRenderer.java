package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.model.ModelSongStandingShield;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class SongStandingShieldRenderer extends EntityRenderer<Entity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield.png");

    private final ModelSongStandingShield<Entity> model;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelSongStandingShield<>(ctx.bakeLayer(ModelSongStandingShield.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEXTURE;
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yaw = ((SongStandingShieldEntity) entity).getShieldYaw();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yaw));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5D, 0.0D);

        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutout(TEXTURE)),
                packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}