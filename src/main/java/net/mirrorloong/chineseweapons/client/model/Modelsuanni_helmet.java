package net.mirrorloong.chineseweapons.client.model;
// Made with Blockbench 5.1.6
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

public class Modelsuanni_helmet<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("chineseweapons", "suanni_helmet"), "main");
    public final ModelPart bipedHead;

    public Modelsuanni_helmet(ModelPart root) {
		this.bipedHead = root.getChild("bipedHead");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
		.texOffs(0, 30).addBox(-0.5F, -8.8F, -3.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.65F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorHead_r1 = bipedHead.addOrReplaceChild("armorHead_r1", CubeListBuilder.create().texOffs(30, 18).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -12.5F, 0.6F, 0.1745F, 0.0F, 0.0F));

		PartDefinition armorHead_r2 = bipedHead.addOrReplaceChild("armorHead_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.5F, -4.0F, -3.5F, 9.0F, 8.0F, 6.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, -6.5F, -1.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition armorHead_r3 = bipedHead.addOrReplaceChild("armorHead_r3", CubeListBuilder.create().texOffs(30, 26).addBox(-1.5F, 1.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, -9.4927F, -5.8042F, -0.3403F, 0.0F, 0.0F));

		PartDefinition armorHead_r4 = bipedHead.addOrReplaceChild("armorHead_r4", CubeListBuilder.create().texOffs(16, 30).addBox(-3.5F, -4.5F, -0.5F, 7.0F, 4.0F, 2.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, -5.1927F, -6.9042F, -0.5323F, 0.0F, 0.0F));

		PartDefinition armorHead_r5 = bipedHead.addOrReplaceChild("armorHead_r5", CubeListBuilder.create().texOffs(30, 16).addBox(-4.5F, -1.5F, -0.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, -9.4927F, -4.7042F, -0.5323F, 0.0F, 0.0F));

		PartDefinition armorHead_r6 = bipedHead.addOrReplaceChild("armorHead_r6", CubeListBuilder.create().texOffs(32, 0).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, -8.4927F, -4.8042F, -0.5323F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}