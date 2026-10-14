package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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

public class SongStandingShieldRenderer extends EntityRenderer<SongStandingShieldEntity> {

    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield.png");
    public static final ResourceLocation TEXTURE_CRACKED_1 =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_1.png");
    public static final ResourceLocation TEXTURE_CRACKED_2 =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_2.png");
    public static final ResourceLocation TEXTURE_CRACKED_3 =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_3.png");
    public static final ResourceLocation TEXTURE_CRACKED_4 =
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_4.png");

    private final ModelSongStandingShield<SongStandingShieldEntity> model;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelSongStandingShield<>(ctx.bakeLayer(ModelSongStandingShield.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(SongStandingShieldEntity entity) {
        return getTextureFor(entity);
    }

    private static ResourceLocation getTextureFor(SongStandingShieldEntity shield) {
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
    public void render(SongStandingShieldEntity shield, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yaw = shield.getShieldYaw();
        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5D, 0.0D);

        ResourceLocation texture = getTextureFor(shield);

        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutout(texture)),
                packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
        super.render(shield, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}