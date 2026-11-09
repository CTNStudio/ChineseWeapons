package net.mirrorloong.chineseweapons.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.model.ModelSongStandingShield;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class SongStandingShieldRenderer extends EntityRenderer<Entity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield.png");
    public static final ResourceLocation TEXTURE_CRACKED_1 =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_1.png");
    public static final ResourceLocation TEXTURE_CRACKED_2 =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_2.png");
    public static final ResourceLocation TEXTURE_CRACKED_3 =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_3.png");
    public static final ResourceLocation TEXTURE_CRACKED_4 =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/entity/song_standing_shield_cracked_4.png");

    private final ModelSongStandingShield<Entity> model;

    public SongStandingShieldRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelSongStandingShield<>(ctx.bakeLayer(ModelSongStandingShield.LAYER_LOCATION));
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return getTextureFor((SongStandingShieldEntity) entity);
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
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        SongStandingShieldEntity shield = (SongStandingShieldEntity) entity;
        float yaw = shield.getShieldYaw();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yaw));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5D, 0.0D);

        ResourceLocation texture = getTextureFor(shield);
        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(RenderType.entityCutout(texture)),
                packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}