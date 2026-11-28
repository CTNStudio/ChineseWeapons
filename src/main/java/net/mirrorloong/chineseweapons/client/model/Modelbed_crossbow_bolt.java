package net.mirrorloong.chineseweapons.client.model;// Made with Blockbench 5.2.1
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
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class Modelbed_crossbow_bolt<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "bed_crossbow_bolt"), "main");
	private final ModelPart bolt;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart vane;

	public Modelbed_crossbow_bolt(ModelPart root) {
		this.bolt = root.getChild("bolt");
		this.bone = this.bolt.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.vane = this.bolt.getChild("vane");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bolt = partdefinition.addOrReplaceChild("bolt", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4667F, -0.0667F, -18.5F, 1.0F, 1.0F, 35.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-0.0333F, 1.734F, -8.0717F, -0.1745F, 0.0F, 0.0F));

		PartDefinition bone = bolt.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(48, 36).addBox(-0.5F, 0.4F, 2.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0333F, -0.4667F, -13.5F));

		PartDefinition bow_r1 = bone.addOrReplaceChild("bow_r1", CubeListBuilder.create().texOffs(0, 51).addBox(-0.5F, 0.0144F, -3.3679F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.7174F, 0.7F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bow_r2 = bone.addOrReplaceChild("bow_r2", CubeListBuilder.create().texOffs(0, 51).addBox(-0.5F, -1.0144F, -3.3679F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.7F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bow_r3 = bone.addOrReplaceChild("bow_r3", CubeListBuilder.create().texOffs(48, 53).addBox(-0.5F, -0.2144F, -1.4679F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.8679F, -0.1965F, 0.0F, 0.0F, 1.5708F));

		PartDefinition bow_r4 = bone2.addOrReplaceChild("bow_r4", CubeListBuilder.create().texOffs(0, 51).addBox(-0.5F, 0.0144F, -3.3679F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.8495F, 0.8965F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bow_r5 = bone2.addOrReplaceChild("bow_r5", CubeListBuilder.create().texOffs(0, 51).addBox(-0.5F, -1.0144F, -3.3679F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8679F, 0.8965F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bow_r6 = bone2.addOrReplaceChild("bow_r6", CubeListBuilder.create().texOffs(48, 53).addBox(-0.5F, -0.2144F, -1.4679F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -0.8679F, 0.1965F, 0.7854F, 0.0F, 0.0F));

		PartDefinition vane = bolt.addOrReplaceChild("vane", CubeListBuilder.create().texOffs(0, 36).addBox(0.1F, -3.1F, -4.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0667F, 0.0333F, 15.0F));

		PartDefinition bow_r7 = vane.addOrReplaceChild("bow_r7", CubeListBuilder.create().texOffs(32, 36).mirror().addBox(0.0F, -3.5F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition bow_r8 = vane.addOrReplaceChild("bow_r8", CubeListBuilder.create().texOffs(32, 36).addBox(0.0F, -3.5F, -4.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition bow_r9 = vane.addOrReplaceChild("bow_r9", CubeListBuilder.create().texOffs(16, 36).addBox(0.0F, -3.5F, -4.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.4F, 0.0F, 0.0F, 0.0F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bolt.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}