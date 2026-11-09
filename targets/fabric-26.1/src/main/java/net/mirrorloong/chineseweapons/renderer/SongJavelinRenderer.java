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
import net.mirrorloong.chineseweapons.client.model.FabricModelFactory;
import net.mirrorloong.chineseweapons.client.model.SpecEntityModel;
import net.mirrorloong.chineseweapons.client.render.EntityRenderData;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;

/**
 * 26.1 renderers extract an immutable render state in {@code extractRenderState}
 * and submit geometry from {@code submit}; the old immediate-mode
 * {@code render(...)} entry point is gone.
 */
public class SongJavelinRenderer
        extends EntityRenderer<SongJavelinProjectile, SongJavelinRenderer.SongJavelinRenderState> {

    private final SpecEntityModel model;

    public SongJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpecEntityModel(FabricModelFactory.INSTANCE.bake(EntityModelSpecs.JAVELIN));
    }

    @Override
    public SongJavelinRenderState createRenderState() {
        return new SongJavelinRenderState();
    }

    @Override
    public void extractRenderState(SongJavelinProjectile entity, SongJavelinRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = entity.getYRot(partialTick);
        state.xRot = entity.getXRot(partialTick);
        state.texture = entity.getTextureLocation();
    }

    @Override
    public void submit(SongJavelinRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + EntityRenderData.JAVELIN_YAW_OFFSET));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + EntityRenderData.JAVELIN_PITCH_OFFSET));
        poseStack.scale(EntityRenderData.JAVELIN_SCALE, EntityRenderData.JAVELIN_SCALE,
                EntityRenderData.JAVELIN_SCALE);
        collector.order(0).submitModel(this.model, state, poseStack, state.texture,
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public static class SongJavelinRenderState extends EntityRenderState {
        public float yRot;
        public float xRot;
        public Identifier texture;
    }
}
