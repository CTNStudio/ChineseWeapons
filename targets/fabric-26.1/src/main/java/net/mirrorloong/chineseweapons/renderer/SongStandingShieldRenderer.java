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
import net.mirrorloong.chineseweapons.client.model.ModelSongStandingShield;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class SongStandingShieldRenderer
        extends EntityRenderer<SongStandingShieldEntity, SongStandingShieldRenderer.SongStandingShieldRenderState> {

    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/song_standing_shield.png");
    public static final Identifier TEXTURE_CRACKED_1 =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/song_standing_shield_cracked_1.png");
    public static final Identifier TEXTURE_CRACKED_2 =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/song_standing_shield_cracked_2.png");
    public static final Identifier TEXTURE_CRACKED_3 =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/song_standing_shield_cracked_3.png");
    public static final Identifier TEXTURE_CRACKED_4 =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/song_standing_shield_cracked_4.png");

    private final ModelSongStandingShield<SongStandingShieldEntity> model;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelSongStandingShield<>(ctx.bakeLayer(ModelSongStandingShield.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
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
        float hp = shield.getHealth();
        if (hp < 400) {
            return TEXTURE_CRACKED_4;
        } else if (hp < 700) {
            return TEXTURE_CRACKED_3;
        } else if (hp < 1000) {
            return TEXTURE_CRACKED_2;
        } else if (hp < 1300) {
            return TEXTURE_CRACKED_1;
        }
        return TEXTURE;
    }

    @Override
    public void submit(SongStandingShieldRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.shieldYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5D, 0.0D);
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
