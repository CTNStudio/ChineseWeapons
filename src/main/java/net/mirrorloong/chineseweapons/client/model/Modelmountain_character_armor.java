package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Modelmountain_character_armor<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("chineseweapons", "modelmountain_character_armor"), "main");
    public final ModelPart bipedHead;
    public final ModelPart bipedBody;
    public final ModelPart bipedLeftArm;
    public final ModelPart bipedRightArm;
    public final ModelPart bipedLeftLeg;
    public final ModelPart bipedLeft;
    public final ModelPart bipedRightLeg;
    public final ModelPart bipedRight;

    public Modelmountain_character_armor(ModelPart root) {
        this.bipedHead = root.getChild("bipedHead");
        this.bipedBody = root.getChild("bipedBody");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.bipedLeft = root.getChild("bipedLeft");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
        this.bipedRight = root.getChild("bipedRight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.9F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(48, 51).addBox(0.0F, -15.5564F, -2.3549F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(50, 7).addBox(-1.0F, -14.6F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone_r1 = bipedHead.addOrReplaceChild("bone_r1", CubeListBuilder.create().texOffs(48, 51).addBox(0.0F, -3.0F, -2.5F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.5564F, 0.1451F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone2_r1 = bipedHead.addOrReplaceChild("bone2_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -4.0F, -2.0F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9F, -11.5F, 2.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone2_r2 = bipedHead.addOrReplaceChild("bone2_r2", CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -2.5F, -4.5F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.4113F, 4.2016F, 0.0F, 0.1745F, -1.5708F));

        PartDefinition armorHead_r1 = bipedHead.addOrReplaceChild("armorHead_r1", CubeListBuilder.create().texOffs(50, 0).addBox(-4.0F, -4.0F, -0.5F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 4.1F, 5.6F, 0.48F, 0.0F, 0.0F));

        PartDefinition armorHead_r2 = bipedHead.addOrReplaceChild("armorHead_r2", CubeListBuilder.create().texOffs(32, 45).addBox(-5.5F, -2.5F, -0.5F, 11.0F, 5.0F, 1.0F, new CubeDeformation(0.68F)), PartPose.offsetAndRotation(4.518F, 1.7495F, 1.7F, 0.0F, 1.5708F, -0.5672F));

        PartDefinition armorHead_r3 = bipedHead.addOrReplaceChild("armorHead_r3", CubeListBuilder.create().texOffs(32, 45).mirror().addBox(-5.5F, -0.5F, 0.5F, 11.0F, 5.0F, 1.0F, new CubeDeformation(0.65F)).mirror(false), PartPose.offsetAndRotation(-2.6F, 0.6F, 1.7F, 0.0F, -1.5708F, 0.5672F));

        PartDefinition cube_r1 = bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(56, 34).addBox(-4.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -6.0F, -3.5F, 0.0F, 1.1345F, 0.0F));

        PartDefinition cube_r2 = bipedHead.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(56, 34).mirror().addBox(0.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.5F, -6.0F, -3.0F, 0.0F, -1.1345F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create()
                .texOffs(56, 41).addBox(-1.5F, 6.2F, -4.2F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(24, 16).addBox(-2.5F, 9.0F, -3.0F, 5.0F, 6.0F, 7.0F, new CubeDeformation(0.8F))
                .texOffs(32, 51).addBox(-3.5F, 0.5F, -3.0F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.8F))
                .texOffs(0, 16).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armo_r1 = bipedBody.addOrReplaceChild("armo_r1", CubeListBuilder.create().texOffs(56, 46).addBox(-1.5F, -1.5F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.8F, -3.2F, 0.0F, 0.0F, -0.3054F));

        PartDefinition armo_r2 = bipedBody.addOrReplaceChild("armo_r2", CubeListBuilder.create().texOffs(56, 46).mirror().addBox(-0.5F, -1.5F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 5.8F, -3.2F, 0.0F, 0.0F, 0.3054F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(16, 45).addBox(-0.7F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition armorLeftArm_r1 = bipedLeftArm.addOrReplaceChild("armorLeftArm_r1", CubeListBuilder.create().texOffs(56, 26).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.52F)), PartPose.offsetAndRotation(2.5F, -1.8F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition armorle_r1 = bipedLeftArm.addOrReplaceChild("armorle_r1", CubeListBuilder.create().texOffs(40, 29).mirror().addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(2.4F, 3.5F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(16, 45).mirror().addBox(-3.3F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition armorle_r2 = bipedRightArm.addOrReplaceChild("armorle_r2", CubeListBuilder.create().texOffs(40, 29).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-2.0F, 3.5F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition armorRightArm_r1 = bipedRightArm.addOrReplaceChild("armorRightArm_r1", CubeListBuilder.create().texOffs(56, 26).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.52F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -1.8F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(24, 29).addBox(-1.9F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.86F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftLegArmor_r1 = bipedLeftLeg.addOrReplaceChild("LeftLegArmor_r1", CubeListBuilder.create().texOffs(48, 14).mirror().addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(0.1F, 3.5F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition bipedLeft = partdefinition.addOrReplaceChild("bipedLeft", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftBoots_r1 = bipedLeft.addOrReplaceChild("LeftBoots_r1", CubeListBuilder.create().texOffs(16, 32).mirror().addBox(0.0F, -4.0F, 0.0F, 0.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.7F, -1.5F, 0.0F, 0.0873F, 0.0F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(24, 29).addBox(-2.1F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.85F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightLegArmor_r1 = bipedRightLeg.addOrReplaceChild("RightLegArmor_r1", CubeListBuilder.create().texOffs(48, 14).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.911F)), PartPose.offsetAndRotation(-0.1F, 3.5F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition bipedRight = partdefinition.addOrReplaceChild("bipedRight", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.9F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition RightBoots_r1 = bipedRight.addOrReplaceChild("RightBoots_r1", CubeListBuilder.create().texOffs(16, 32).addBox(0.0F, -4.0F, 0.0F, 0.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 8.7F, -1.5F, 0.0F, -0.0873F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}