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

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Modelmiddle_tang_dynasty_ming_guang_armor<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("chineseweapons", "modelmiddle_tang_dynasty_ming_guang_armor"), "main");
    public final ModelPart bipedHead;
    public final ModelPart bipedBody;
    public final ModelPart bipedLeftArm;
    public final ModelPart bipedRightArm;
    public final ModelPart bipedLeft;
    public final ModelPart bipedLeftLeg;
    public final ModelPart bipedRight;
    public final ModelPart bipedRightLeg;

    public Modelmiddle_tang_dynasty_ming_guang_armor(ModelPart root) {
        this.bipedHead = root.getChild("bipedHead");
        this.bipedBody = root.getChild("bipedBody");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.bipedLeft = root.getChild("bipedLeft");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.bipedRight = root.getChild("bipedRight");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.9F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(36, 68).addBox(-4.0F, -11.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 7).addBox(-1.5F, -13.3F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-1.0F, -15.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition armorHead_r1 = bipedHead.addOrReplaceChild("armorHead_r1", CubeListBuilder.create().texOffs(23, 48).addBox(-6.0F, -2.0F, -3.0F, 12.0F, 4.0F, 6.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, -2.1631F, 5.18F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(48, 32).mirror().addBox(-4.5F, 2.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 32).addBox(2.4F, 2.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 42).addBox(-1.5F, 6.8F, -3.8F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -0.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F))
                .texOffs(24, 25).addBox(-4.0F, -0.1F, -2.0F, 8.0F, 7.0F, 4.0F, new CubeDeformation(1.5F))
                .texOffs(16, 36).addBox(-4.0F, -0.5F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(1.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(0.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition dontTouch_r1 = bipedLeftArm.addOrReplaceChild("dontTouch_r1", CubeListBuilder.create().texOffs(0, 68).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(1.6F, -1.7F, 0.0F, 0.0F, 0.0F, -0.48F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(32, 0).addBox(-3.9F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition dontTouch_r2 = bipedRightArm.addOrReplaceChild("dontTouch_r2", CubeListBuilder.create().texOffs(0, 68).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-1.3F, -1.7F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition bipedLeft = partdefinition.addOrReplaceChild("bipedLeft", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftBoots_r1 = bipedLeft.addOrReplaceChild("LeftBoots_r1", CubeListBuilder.create().texOffs(53, 58).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(3.0F, 8.9F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.86F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftLegArmor_r1 = bipedLeftLeg.addOrReplaceChild("LeftLegArmor_r1", CubeListBuilder.create().texOffs(40, 36).mirror().addBox(-2.5F, -4.0F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(0.6F, 3.5F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition bipedRight = partdefinition.addOrReplaceChild("bipedRight", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition RightBoots_r1 = bipedRight.addOrReplaceChild("RightBoots_r1", CubeListBuilder.create().texOffs(53, 58).mirror().addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 8.9F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.1F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.85F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightLegArmor_r1 = bipedRightLeg.addOrReplaceChild("RightLegArmor_r1", CubeListBuilder.create().texOffs(40, 36).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.9F)), PartPose.offsetAndRotation(-0.6F, 3.5F, 0.0F, 0.0F, 0.0F, 0.1745F));

        return LayerDefinition.create(meshdefinition, 80, 80);
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
        bipedRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
