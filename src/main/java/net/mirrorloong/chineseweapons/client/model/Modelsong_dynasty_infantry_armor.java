package net.mirrorloong.chineseweapons.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class Modelsong_dynasty_infantry_armor<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "song_dynasty_infantry_armor"), "main");
    public final ModelPart bipedHead;
    public final ModelPart bipedLeftLeg;
    public final ModelPart bipedBody;
    public final ModelPart bipedRightLeg;
    public final ModelPart bipedRightArm;
    public final ModelPart bipedLeftArm;
    public final ModelPart RightBoots;
    public final ModelPart LeftBoots;

    public Modelsong_dynasty_infantry_armor(ModelPart root) {
        this.bipedHead = root.getChild("bipedHead");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.bipedBody = root.getChild("bipedBody");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.RightBoots = root.getChild("RightBoots");
        this.LeftBoots = root.getChild("LeftBoots");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.61F))
                .texOffs(32, 33).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.7F))
                .texOffs(0, 49).addBox(-7.0F, -5.05F, -5.0F, 14.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -10.7F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 50).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -8.7F, 1.2F, -0.2094F, 0.0F, 0.0F));

        PartDefinition armorHead_r1 = bipedHead.addOrReplaceChild("armorHead_r1", CubeListBuilder.create().texOffs(26, 18).addBox(-7.0F, -2.25F, -3.3F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.55F, -7.3F, -0.3491F, 0.0F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.3F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.82F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(24, 25).addBox(-3.4F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(24, 25).mirror().addBox(-0.6F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create(), PartPose.offset(-1.9F, 13.0F, 0.0F));

        PartDefinition RightLegArmor_r1 = bipedRightLeg.addOrReplaceChild("RightLegArmor_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-1.8054F, -0.0608F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.84F)), PartPose.offsetAndRotation(-0.1F, -0.5F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create(), PartPose.offset(1.9F, 13.0F, 0.0F));

        PartDefinition LeftLegArmor_r1 = bipedLeftLeg.addOrReplaceChild("LeftLegArmor_r1", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-3.1946F, -0.0608F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.85F)).mirror(false), PartPose.offsetAndRotation(0.1F, -0.5F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition RightBoots = partdefinition.addOrReplaceChild("RightBoots", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0F, 6.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.8F)).mirror(false), PartPose.offset(-2.0F, 12.5F, 0.0F));

        PartDefinition LeftBoots = partdefinition.addOrReplaceChild("LeftBoots", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.offset(2.0F, 12.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}