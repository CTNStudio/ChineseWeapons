package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.model.FabricModelFactory;
import net.mirrorloong.chineseweapons.client.model.SpecEntityModel;
import net.mirrorloong.chineseweapons.client.render.EntityRenderData;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class SongStandingShieldRenderer
        extends EntityRenderer<SongStandingShieldEntity, SongStandingShieldRenderer.SongStandingShieldRenderState> {

    private final SpecEntityModel model;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new SpecEntityModel(FabricModelFactory.INSTANCE.bake(EntityModelSpecs.STANDING_SHIELD));
        this.shadowRadius = EntityRenderData.SHIELD_SHADOW_RADIUS;
    }

    @Override
    public SongStandingShieldRenderState createRenderState() {
        return new SongStandingShieldRenderState();
    }

    @Override
    public void extractRenderState(SongStandingShieldEntity entity, SongStandingShieldRenderState state,
            float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.shieldYaw = entity.getShieldYaw();
        state.texture = getTextureFor(entity);
    }

    private static Identifier getTextureFor(SongStandingShieldEntity shield) {
        return Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID,
                EntityRenderData.shieldTexturePath(shield.getHealth()));
    }

    @Override
    public void submit(SongStandingShieldRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.shieldYaw));
        poseStack.scale(EntityRenderData.SHIELD_SCALE_X, EntityRenderData.SHIELD_SCALE_Y,
                EntityRenderData.SHIELD_SCALE_Z);
        poseStack.translate(0.0D, EntityRenderData.SHIELD_TRANSLATE_Y, 0.0D);
        collector.order(0).submitModel(this.model, state, poseStack, state.texture,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class SongStandingShieldRenderState extends EntityRenderState {
        public float shieldYaw;
        public Identifier texture;
    }
}
