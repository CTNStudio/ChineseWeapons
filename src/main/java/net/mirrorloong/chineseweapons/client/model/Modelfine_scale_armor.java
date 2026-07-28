package net.mirrorloong.chineseweapons.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Modelfine_scale_armor<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("chineseweapons", "fine_scale_armor"), "main");
	public final ModelPart bipedHead;
	public final ModelPart bipedRightArm;
	public final ModelPart bipedLeftArm;
	public final ModelPart bipedLeftLeg;
	public final ModelPart bipedBody;
	public final ModelPart bipedRightLeg;
	public final ModelPart LeftBoots;
	public final ModelPart RightBoots;

	public Modelfine_scale_armor(ModelPart root) {
		this.bipedHead = root.getChild("bipedHead");
		this.bipedRightArm = root.getChild("bipedRightArm");
		this.bipedLeftArm = root.getChild("bipedLeftArm");
		this.bipedLeftLeg = root.getChild("bipedLeftLeg");
		this.bipedBody = root.getChild("bipedBody");
		this.bipedRightLeg = root.getChild("bipedRightLeg");
		this.LeftBoots = root.getChild("LeftBoots");
		this.RightBoots = root.getChild("RightBoots");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
				.texOffs(30, 24).addBox(-2.5F, -9.0F, -4.3F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.4F)).texOffs(35, 36).addBox(0.0F, -21.5564F, -2.3549F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(48, 23).addBox(-1.5F, -14.0F, -1.3F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		bipedHead.addOrReplaceChild("bone_r1", CubeListBuilder.create().texOffs(35, 36).addBox(0.0F, -8.0F, -2.5F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.5564F, 0.1451F, 0.0F, 1.5708F, 0.0F));
		bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(23, 56).addBox(-4.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -7.0F, -3.5F, 0.0F, 1.1345F, 0.0F));
		bipedHead.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(23, 56).mirror().addBox(0.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.5F, -7.0F, -3.0F, 0.0F, -1.1345F, 0.0F));

		PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(19, 38).addBox(-4.4F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		bipedRightArm.addOrReplaceChild("armorRightArm_r1", CubeListBuilder.create().texOffs(7, 54).mirror().addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.2F, -2.9F, 0.0F, 0.0F, 0.0F, 1.3701F));

		PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(19, 38).mirror().addBox(0.4F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
		bipedLeftArm.addOrReplaceChild("armorLeftArm_r1", CubeListBuilder.create().texOffs(7, 54).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2F, -2.9F, 0.0F, 0.0F, 0.0F, -1.3701F));

		PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(48, 43).addBox(-1.9F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.86F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		bipedLeftLeg.addOrReplaceChild("LeftLegArmor_r1", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(0.6F, 3.5F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -0.5F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).texOffs(32, 0).addBox(-4.5F, -0.5F, -2.0F, 9.0F, 7.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		bipedBody.addOrReplaceChild("RightLegArmor_r1", CubeListBuilder.create().texOffs(0, 52).addBox(1.5F, 1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.91F)), PartPose.offsetAndRotation(-5.573F, 9.7776F, 0.0F, 0.0F, 0.0F, 0.384F));
		bipedBody.addOrReplaceChild("LeftLegArmor_r2", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-2.5F, 1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(5.573F, 9.7776F, 0.0F, 0.0F, 0.0F, -0.384F));

		PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(48, 43).addBox(-2.1F, -0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.85F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		bipedRightLeg.addOrReplaceChild("RightLegArmor_r2", CubeListBuilder.create().texOffs(0, 33).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.9F)), PartPose.offsetAndRotation(-0.6F, 3.5F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition leftBoots = partdefinition.addOrReplaceChild("LeftBoots", CubeListBuilder.create().texOffs(34, 55).addBox(-1.9F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		leftBoots.addOrReplaceChild("LeftBoots_r1", CubeListBuilder.create().texOffs(46, 33).mirror().addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.7F)).mirror(false), PartPose.offsetAndRotation(0.5F, 8.8F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition rightBoots = partdefinition.addOrReplaceChild("RightBoots", CubeListBuilder.create().texOffs(34, 55).mirror().addBox(-2.1F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));
		rightBoots.addOrReplaceChild("RightBoots_r1", CubeListBuilder.create().texOffs(46, 33).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-0.5F, 8.8F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedRightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedLeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightBoots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
