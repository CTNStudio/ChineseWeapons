package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.model.ForgeModelFactory;
import net.mirrorloong.chineseweapons.client.render.EntityRenderData;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class SongStandingShieldRenderer extends EntityRenderer<Entity> {

    private final ModelPart root;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.root = ForgeModelFactory.INSTANCE.bake(EntityModelSpecs.STANDING_SHIELD);
        this.shadowRadius = EntityRenderData.SHIELD_SHADOW_RADIUS;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return getTextureFor((SongStandingShieldEntity) entity);
    }

    private static ResourceLocation getTextureFor(SongStandingShieldEntity shield) {
        return new ResourceLocation(ChineseWeapons.MOD_ID,
                EntityRenderData.shieldTexturePath(shield.getHealth()));
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        SongStandingShieldEntity shield = (SongStandingShieldEntity) entity;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-shield.getShieldYaw()));

        poseStack.scale(EntityRenderData.SHIELD_SCALE_X, EntityRenderData.SHIELD_SCALE_Y,
                EntityRenderData.SHIELD_SCALE_Z);
        poseStack.translate(0.0D, EntityRenderData.SHIELD_TRANSLATE_Y, 0.0D);

        this.root.render(poseStack,
                buffer.getBuffer(RenderType.entityCutout(getTextureFor(shield))),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
