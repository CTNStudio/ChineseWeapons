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
import net.mirrorloong.chineseweapons.client.model.ModelSongJavelinModel;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;

/**
 * 26.1 renderers extract an immutable render state in {@code extractRenderState}
 * and submit geometry from {@code submit}; the old immediate-mode
 * {@code render(...)} entry point is gone.
 */
public class SongJavelinRenderer
        extends EntityRenderer<SongJavelinProjectile, SongJavelinRenderer.SongJavelinRenderState> {

    private final ModelSongJavelinModel<SongJavelinProjectile> model;

    public SongJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelSongJavelinModel<>(context.bakeLayer(ModelSongJavelinModel.LAYER_LOCATION));
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
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
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
