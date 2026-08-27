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
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class Modeltang_dynasty_infantry_armor<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "tang_dynasty_infantry_armor"), "main");
	public final ModelPart bipedHead;
	public final ModelPart bipedRightArm;
	public final ModelPart bipedLeftArm;
	public final ModelPart bipedLeftLeg;
	public final ModelPart bipedBody;
	public final ModelPart bipedRightLeg;
	public final ModelPart LeftBoots;
	public final ModelPart RightBoots;

	public Modeltang_dynasty_infantry_armor(ModelPart root) {
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
		PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
				.texOffs(49, 14).addBox(-1.5F, -10.6F, -2.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 10).addBox(-3.5F, -1.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.1F, -2.1F, 0.4538F, 0.0F, 0.0F));
		bipedHead.addOrReplaceChild("armorHead_r1", CubeListBuilder.create().texOffs(39, 31).mirror().addBox(-3.8F, -2.3F, -2.2F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.7F)).mirror(false)
				.texOffs(39, 31).addBox(-4.2F, -2.3F, -2.2F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, -13.0F, 1.6F, 0.1745F, 0.0F, 0.0F));
		bipedHead.addOrReplaceChild("armorHead_r2", CubeListBuilder.create().texOffs(29, 20).addBox(-4.0F, -2.5F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -12.0F, 1.6F, 0.1745F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(19, 38).addBox(-4.4F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(19, 38).mirror().addBox(0.4F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.52F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
		bipedLeftLeg.addOrReplaceChild("LeftLegArmor_r1", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.91F)).mirror(false), PartPose.offsetAndRotation(0.6F, 3.5F, 0.0F, 0.0F, 0.0F, -0.1745F));
        partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create()
                .texOffs(0, 17).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
		bipedRightLeg.addOrReplaceChild("RightLegArmor_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.9F)), PartPose.offsetAndRotation(-0.6F, 3.5F, 0.0F, 0.0F, 0.0F, 0.1745F));
		partdefinition.addOrReplaceChild("LeftBoots", CubeListBuilder.create().texOffs(34, 55).addBox(-1.9F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("RightBoots", CubeListBuilder.create().texOffs(34, 55).mirror().addBox(-2.1F, 6.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.88F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));
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
