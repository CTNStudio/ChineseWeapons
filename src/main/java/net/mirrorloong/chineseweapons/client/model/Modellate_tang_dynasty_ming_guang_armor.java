package net.mirrorloong.chineseweapons.client.model;

// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Modellate_tang_dynasty_ming_guang_armor<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("chineseweapons", "late_tang_dynasty_ming_guang_armor"), "main");
    public final ModelPart bipedHead;
    public final ModelPart bipedBody;
    public final ModelPart bipedRightArm;
    public final ModelPart bipedLeftArm;
    public final ModelPart LeftBoots;
    public final ModelPart bipedLeftLeg;
    public final ModelPart RightBoots;
    public final ModelPart bipedRightLeg;

	public Modellate_tang_dynasty_ming_guang_armor(ModelPart root) {
		this.bipedHead = root.getChild("bipedHead");
		this.bipedBody = root.getChild("bipedBody");
		this.bipedRightArm = root.getChild("bipedRightArm");
		this.bipedLeftArm = root.getChild("bipedLeftArm");
		this.LeftBoots = root.getChild("LeftBoots");
		this.bipedLeftLeg = root.getChild("bipedLeftLeg");
		this.RightBoots = root.getChild("RightBoots");
		this.bipedRightLeg = root.getChild("bipedRightLeg");
	}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.9F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(28, 17).addBox(-4.0F, -11.5F, -3.9F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(28, 58).addBox(-1.0F, -15.5F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r1 = bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 55).mirror().addBox(-2.5F, -4.5F, -2.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -3.5F, 0.5F, 0.0F, 0.0F, -0.2269F));

        PartDefinition cube_r2 = bipedHead.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 55).addBox(-0.5F, -4.5F, -2.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(4.5F, -3.5F, 0.5F, 0.0F, 0.0F, 0.2269F));

        PartDefinition cube_r3 = bipedHead.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 48).addBox(-1.5F, -0.9825F, -1.4322F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0305F, 0.239F, 1.2479F, 0.0F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -0.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F))
                .texOffs(40, 33).addBox(-4.0F, -0.1F, -2.0F, 8.0F, 7.0F, 4.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(32, 0).addBox(-3.9F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition dontTouch_r1 = bipedRightArm.addOrReplaceChild("dontTouch_r1", CubeListBuilder.create().texOffs(16, 42).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-1.1F, -0.7F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-0.1F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition dontTouch_r2 = bipedLeftArm.addOrReplaceChild("dontTouch_r2", CubeListBuilder.create().texOffs(16, 42).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(1.1F, -0.7F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition LeftBoots = partdefinition.addOrReplaceChild("LeftBoots", CubeListBuilder.create().texOffs(42, 55).addBox(-2.5F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)), PartPose.offset(2.5F, 12.0F, 0.0F));

        PartDefinition LeftBoots_r1 = LeftBoots.addOrReplaceChild("LeftBoots_r1", CubeListBuilder.create().texOffs(53, 4).addBox(-0.5F, -5.5F, -1.5F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(2.0F, 8.9F, 0.0F, -1.1519F, 0.0F, 0.0F));

        PartDefinition LeftBoots_r2 = LeftBoots.addOrReplaceChild("LeftBoots_r2", CubeListBuilder.create().texOffs(56, 47).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(2.5F, 8.9F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.86F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition dontTouch_r3 = bipedLeftLeg.addOrReplaceChild("dontTouch_r3", CubeListBuilder.create().texOffs(36, 44).mirror().addBox(-2.5F, -4.0F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(0.6F, 3.5F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition RightBoots = partdefinition.addOrReplaceChild("RightBoots", CubeListBuilder.create().texOffs(42, 55).mirror().addBox(-1.5F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.89F)).mirror(false), PartPose.offset(-2.5F, 12.0F, 0.0F));

        PartDefinition RightBoots_r1 = RightBoots.addOrReplaceChild("RightBoots_r1", CubeListBuilder.create().texOffs(53, 4).mirror().addBox(-0.5F, -5.5F, -1.5F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 8.9F, 0.0F, -1.1519F, 0.0F, 0.0F));

        PartDefinition RightBoots_r2 = RightBoots.addOrReplaceChild("RightBoots_r2", CubeListBuilder.create().texOffs(56, 47).mirror().addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 8.9F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.1F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.85F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition dontTouch_r4 = bipedRightLeg.addOrReplaceChild("dontTouch_r4", CubeListBuilder.create().texOffs(36, 44).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.9F)), PartPose.offsetAndRotation(-0.6F, 3.5F, 0.0F, 0.0F, 0.0F, 0.1745F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
		LeftBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}