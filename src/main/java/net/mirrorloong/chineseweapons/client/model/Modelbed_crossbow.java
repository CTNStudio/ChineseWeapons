package net.mirrorloong.chineseweapons.client.model;// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.animation.bed_crossbowAnimation;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import org.joml.Vector3f;

public class Modelbed_crossbow<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "bed_crossbow"), "main");
	private static final Vector3f ANIMATION_CACHE = new Vector3f();
	private final ModelPart root;
	private final ModelPart bow_root;
	private final ModelPart bow;
	private final ModelPart bow_front;
	private final ModelPart front_bow_right;
	private final ModelPart front_right;
	private final ModelPart sring_front_2;
	private final ModelPart front_bow_left;
	private final ModelPart front_left;
	private final ModelPart sring_front_1;
	private final ModelPart bow_main;
	private final ModelPart main_bow_right;
	private final ModelPart main_right;
	private final ModelPart string_right;
	private final ModelPart string_2;
	private final ModelPart main_bow_left;
	private final ModelPart main_left;
	private final ModelPart string_1;
	private final ModelPart string_left;
	private final ModelPart bow_back;
	private final ModelPart back_bow_right;
	private final ModelPart back_right;
	private final ModelPart back_bow_left;
	private final ModelPart back_left;
	private final ModelPart winch_axle;
	private final ModelPart string;

	public Modelbed_crossbow(ModelPart root) {
		this.root = root.getChild("root");
		this.bow_root = this.root.getChild("bow_root");
		this.bow = this.bow_root.getChild("bow");
		this.bow_front = this.bow.getChild("bow_front");
		this.front_bow_right = this.bow_front.getChild("front_bow_right");
		this.front_right = this.front_bow_right.getChild("front_right");
		this.sring_front_2 = this.front_right.getChild("sring_front_2");
		this.front_bow_left = this.bow_front.getChild("front_bow_left");
		this.front_left = this.front_bow_left.getChild("front_left");
		this.sring_front_1 = this.front_left.getChild("sring_front_1");
		this.bow_main = this.bow.getChild("bow_main");
		this.main_bow_right = this.bow_main.getChild("main_bow_right");
		this.main_right = this.main_bow_right.getChild("main_right");
		this.string_right = this.main_right.getChild("string_right");
		this.string_2 = this.main_right.getChild("string_2");
		this.main_bow_left = this.bow_main.getChild("main_bow_left");
		this.main_left = this.main_bow_left.getChild("main_left");
		this.string_1 = this.main_left.getChild("string_1");
		this.string_left = this.main_left.getChild("string_left");
		this.bow_back = this.bow.getChild("bow_back");
		this.back_bow_right = this.bow_back.getChild("back_bow_right");
		this.back_right = this.back_bow_right.getChild("back_right");
		this.back_bow_left = this.bow_back.getChild("back_bow_left");
		this.back_left = this.back_bow_left.getChild("back_left");
		this.winch_axle = this.root.getChild("winch_axle");
		this.string = this.root.getChild("string");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-8.4992F, -10.6355F, -38.3973F, 17.0F, 1.0F, 44.0F, new CubeDeformation(0.1F))
		.texOffs(27, 0).mirror().addBox(-11.4992F, -18.6355F, -3.3973F, 2.0F, 18.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(27, 0).addBox(9.5008F, -18.6355F, -3.3973F, 2.0F, 18.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(79, 0).addBox(9.5008F, -14.6355F, -38.3973F, 2.0F, 14.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(79, 0).mirror().addBox(-11.4992F, -14.6355F, -38.3973F, 2.0F, 14.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(28, 45).mirror().addBox(-10.4992F, -12.6355F, -40.3973F, 2.0F, 4.0F, 48.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(43, 61).addBox(-3.9992F, -19.7355F, -32.1973F, 8.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-2.9992F, -16.6355F, -6.4973F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(28, 45).addBox(8.5008F, -12.6355F, -40.3973F, 2.0F, 4.0F, 48.0F, new CubeDeformation(0.1F)), PartPose.offset(-0.0008F, 24.7768F, 15.1554F));

		PartDefinition bow_root = root.addOrReplaceChild("bow_root", CubeListBuilder.create(), PartPose.offset(0.0008F, -15.7628F, -5.675F));

		PartDefinition bow = bow_root.addOrReplaceChild("bow", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition bow_r1 = bow.addOrReplaceChild("bow_r1", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -6.789F, 24.8782F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-2.5F, -3.9587F, -9.7419F, 5.0F, 4.0F, 6.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 46).addBox(-2.5F, -3.9587F, -4.0419F, 5.0F, 6.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.014F, -26.4804F, -0.0873F, 0.0F, 0.0F));

		PartDefinition bow_r2 = bow.addOrReplaceChild("bow_r2", CubeListBuilder.create().texOffs(0, 5).addBox(-3.5F, -2.4F, -1.5F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.5273F, 0.7778F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bow_r3 = bow.addOrReplaceChild("bow_r3", CubeListBuilder.create().texOffs(0, 64).addBox(-2.5F, -1.9F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(0.0F, -2.4918F, -32.5285F, -0.4363F, 0.0F, 0.0F));

		PartDefinition bow_front = bow.addOrReplaceChild("bow_front", CubeListBuilder.create(), PartPose.offset(-0.0333F, -1.014F, -26.4804F));

		PartDefinition front_bow_right = bow_front.addOrReplaceChild("front_bow_right", CubeListBuilder.create(), PartPose.offset(-2.3004F, -0.752F, -1.8902F));

		PartDefinition bow_r4 = front_bow_right.addOrReplaceChild("bow_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-10.4F, -1.7587F, -3.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.3004F, 0.752F, 1.8902F, -0.0873F, 0.0F, 0.0F));

		PartDefinition front_right = front_bow_right.addOrReplaceChild("front_right", CubeListBuilder.create(), PartPose.offset(-7.9004F, 0.1556F, -0.2211F));

		PartDefinition bow_r5 = front_right.addOrReplaceChild("bow_r5", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-1.9F, -1.0F, -1.2F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.6814F, 0.1202F, 5.3742F, -0.0894F, -0.2173F, 0.0193F));

		PartDefinition bow_r6 = front_right.addOrReplaceChild("bow_r6", CubeListBuilder.create().texOffs(19, 71).mirror().addBox(-7.9F, -0.8F, -2.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(0, 59).mirror().addBox(-10.6F, -0.8F, -2.5F, 10.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(1.8008F, -0.4931F, 0.6589F, -0.1034F, 0.5648F, -0.0555F));

		PartDefinition sring_front_2 = front_right.addOrReplaceChild("sring_front_2", CubeListBuilder.create(), PartPose.offset(-4.046F, -0.7234F, 5.2131F));

		PartDefinition bow_r7 = sring_front_2.addOrReplaceChild("bow_r7", CubeListBuilder.create().texOffs(20, 20).mirror().addBox(-1.6F, 0.0F, -1.8F, 1.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -0.0872F, 0.9962F, -0.0876F, 0.0869F, -0.0076F));

		PartDefinition front_bow_left = bow_front.addOrReplaceChild("front_bow_left", CubeListBuilder.create(), PartPose.offset(2.3004F, -0.752F, -1.8902F));

		PartDefinition bow_r8 = front_bow_left.addOrReplaceChild("bow_r8", CubeListBuilder.create().texOffs(0, 0).addBox(2.4F, -1.7587F, -3.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3004F, 0.752F, 1.8902F, -0.0873F, 0.0F, 0.0F));

		PartDefinition front_left = front_bow_left.addOrReplaceChild("front_left", CubeListBuilder.create(), PartPose.offset(7.9004F, 0.1556F, -0.2211F));

		PartDefinition bow_r9 = front_left.addOrReplaceChild("bow_r9", CubeListBuilder.create().texOffs(0, 44).addBox(-2.1F, -1.0F, -1.2F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6814F, 0.1202F, 5.3742F, -0.0894F, 0.2173F, -0.0193F));

		PartDefinition bow_r10 = front_left.addOrReplaceChild("bow_r10", CubeListBuilder.create().texOffs(19, 71).addBox(6.9F, -0.8F, -2.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
		.texOffs(0, 59).addBox(0.6F, -0.8F, -2.5F, 10.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.8008F, -0.4931F, 0.6589F, -0.1034F, -0.5648F, 0.0555F));

		PartDefinition sring_front_1 = front_left.addOrReplaceChild("sring_front_1", CubeListBuilder.create(), PartPose.offset(4.1127F, -0.7234F, 5.2131F));

		PartDefinition bow_r11 = sring_front_1.addOrReplaceChild("bow_r11", CubeListBuilder.create().texOffs(20, 20).addBox(0.6F, 0.0F, -1.8F, 1.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -0.0872F, 0.9962F, -0.0876F, -0.0869F, 0.0076F));

		PartDefinition bow_main = bow.addOrReplaceChild("bow_main", CubeListBuilder.create(), PartPose.offset(-0.0333F, -1.014F, -18.4804F));

		PartDefinition main_bow_right = bow_main.addOrReplaceChild("main_bow_right", CubeListBuilder.create(), PartPose.offset(-2.3004F, -0.752F, -1.8902F));

		PartDefinition bow_r12 = main_bow_right.addOrReplaceChild("bow_r12", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-10.4F, -1.7587F, -3.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.3004F, 0.752F, 1.8902F, -0.0873F, 0.0F, 0.0F));

		PartDefinition main_right = main_bow_right.addOrReplaceChild("main_right", CubeListBuilder.create(), PartPose.offset(-7.9004F, 0.1556F, -0.2211F));

		PartDefinition bow_r13 = main_right.addOrReplaceChild("bow_r13", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-0.5F, -1.0F, -2.6F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.6814F, 0.1202F, 5.3742F, -0.0894F, -0.2173F, 0.0193F));

		PartDefinition bow_r14 = main_right.addOrReplaceChild("bow_r14", CubeListBuilder.create().texOffs(19, 71).mirror().addBox(-8.1F, -0.8F, -2.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(3.8008F, -0.5931F, -0.6411F, -0.1034F, 0.5648F, -0.0555F));

		PartDefinition bow_r15 = main_right.addOrReplaceChild("bow_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-8.6F, -0.8F, -2.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(1.8008F, -0.4931F, 0.6589F, -0.1034F, 0.5648F, -0.0555F));

		PartDefinition string_right = main_right.addOrReplaceChild("string_right", CubeListBuilder.create(), PartPose.offset(-2.5823F, -0.6422F, 3.2551F));

		PartDefinition bow_r16 = string_right.addOrReplaceChild("bow_r16", CubeListBuilder.create().texOffs(2, 2).mirror().addBox(1.4F, -0.3F, -3.0F, 1.0F, 0.0F, 23.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3467F, 0.5482F, 3.1439F, 0.0873F, 0.6109F, -0.028F));

		PartDefinition string_2 = main_right.addOrReplaceChild("string_2", CubeListBuilder.create(), PartPose.offset(-1.9291F, 0.1674F, 3.4105F));

		PartDefinition bow_r17 = string_2.addOrReplaceChild("bow_r17", CubeListBuilder.create().texOffs(19, 19).mirror().addBox(0.1F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -0.2615F, 2.9886F, -0.0876F, -0.0869F, 0.0076F));

		PartDefinition main_bow_left = bow_main.addOrReplaceChild("main_bow_left", CubeListBuilder.create(), PartPose.offset(2.3004F, -0.752F, -1.8902F));

		PartDefinition bow_r18 = main_bow_left.addOrReplaceChild("bow_r18", CubeListBuilder.create().texOffs(0, 0).addBox(2.4F, -1.7587F, -3.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3004F, 0.752F, 1.8902F, -0.0873F, 0.0F, 0.0F));

		PartDefinition main_left = main_bow_left.addOrReplaceChild("main_left", CubeListBuilder.create(), PartPose.offset(7.9004F, 0.1556F, -0.2211F));

		PartDefinition bow_r19 = main_left.addOrReplaceChild("bow_r19", CubeListBuilder.create().texOffs(0, 44).addBox(-3.4F, -1.0F, -2.6F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6814F, 0.1202F, 5.3742F, -0.0894F, 0.2173F, -0.0193F));

		PartDefinition bow_r20 = main_left.addOrReplaceChild("bow_r20", CubeListBuilder.create().texOffs(19, 71).addBox(7.1F, -0.8F, -2.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-3.8008F, -0.5931F, -0.6411F, -0.1034F, -0.5648F, 0.0555F));

		PartDefinition bow_r21 = main_left.addOrReplaceChild("bow_r21", CubeListBuilder.create().texOffs(0, 0).addBox(0.6F, -0.8F, -2.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.8008F, -0.4931F, 0.6589F, -0.1034F, -0.5648F, 0.0555F));

		PartDefinition string_1 = main_left.addOrReplaceChild("string_1", CubeListBuilder.create(), PartPose.offset(1.9957F, 0.1674F, 3.4105F));

		PartDefinition bow_r22 = string_1.addOrReplaceChild("bow_r22", CubeListBuilder.create().texOffs(19, 19).addBox(-1.1F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.2615F, 2.9886F, -0.0876F, 0.0869F, -0.0076F));

		PartDefinition string_left = main_left.addOrReplaceChild("string_left", CubeListBuilder.create(), PartPose.offset(2.649F, -0.6422F, 3.2551F));

		PartDefinition bow_r23 = string_left.addOrReplaceChild("bow_r23", CubeListBuilder.create().texOffs(2, 2).addBox(-2.3273F, 0.6963F, -3.0451F, 1.0F, 0.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3467F, -0.448F, 3.0568F, 0.0873F, -0.6109F, 0.0279F));

		PartDefinition bow_back = bow.addOrReplaceChild("bow_back", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.0333F, 0.4499F, -9.6084F, -0.1745F, 0.0F, 0.0F));

		PartDefinition back_bow_right = bow_back.addOrReplaceChild("back_bow_right", CubeListBuilder.create(), PartPose.offset(-2.3004F, -0.752F, 1.8902F));

		PartDefinition bow_r24 = back_bow_right.addOrReplaceChild("bow_r24", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-10.4F, -1.7587F, 0.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.3004F, 0.752F, -1.8902F, 0.0873F, 0.0F, 0.0F));

		PartDefinition back_right = back_bow_right.addOrReplaceChild("back_right", CubeListBuilder.create(), PartPose.offset(-7.9004F, 0.1556F, 0.2211F));

		PartDefinition bow_r25 = back_right.addOrReplaceChild("bow_r25", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-1.9F, -1.0F, -0.8F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.6814F, 0.1202F, -5.3742F, 0.0894F, 0.2173F, 0.0193F));

		PartDefinition bow_r26 = back_right.addOrReplaceChild("bow_r26", CubeListBuilder.create().texOffs(19, 71).addBox(18.7432F, -1.6968F, -21.8222F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(18.6008F, -1.9569F, 24.4692F, 0.1034F, 0.5648F, 0.0555F));

		PartDefinition bow_r27 = back_right.addOrReplaceChild("bow_r27", CubeListBuilder.create().texOffs(0, 59).mirror().addBox(-10.6F, -0.8F, -0.5F, 10.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(1.8008F, -0.4931F, -0.6589F, 0.1034F, -0.5648F, -0.0555F));

		PartDefinition back_bow_left = bow_back.addOrReplaceChild("back_bow_left", CubeListBuilder.create(), PartPose.offset(2.3004F, -0.752F, 1.8902F));

		PartDefinition bow_r28 = back_bow_left.addOrReplaceChild("bow_r28", CubeListBuilder.create().texOffs(0, 0).addBox(2.4F, -1.7587F, 0.2419F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3004F, 0.752F, -1.8902F, 0.0873F, 0.0F, 0.0F));

		PartDefinition back_left = back_bow_left.addOrReplaceChild("back_left", CubeListBuilder.create(), PartPose.offset(7.9004F, 0.1556F, 0.2211F));

		PartDefinition bow_r29 = back_left.addOrReplaceChild("bow_r29", CubeListBuilder.create().texOffs(0, 44).addBox(-2.1F, -1.0F, -0.8F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.6814F, 0.1202F, -5.3742F, 0.0894F, -0.2173F, -0.0193F));

		PartDefinition bow_r30 = back_left.addOrReplaceChild("bow_r30", CubeListBuilder.create().texOffs(19, 71).mirror().addBox(-19.7432F, -1.6968F, -21.8222F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-18.6008F, -1.9569F, 24.4692F, 0.1034F, -0.5648F, -0.0555F));

		PartDefinition bow_r31 = back_left.addOrReplaceChild("bow_r31", CubeListBuilder.create().texOffs(0, 59).addBox(0.6F, -0.8F, -0.5F, 10.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.8008F, -0.4931F, -0.6589F, 0.1034F, 0.5648F, 0.0555F));

		PartDefinition winch_axle = root.addOrReplaceChild("winch_axle", CubeListBuilder.create(), PartPose.offset(0.0008F, -14.7835F, -0.4453F));

		PartDefinition bow_r32 = winch_axle.addOrReplaceChild("bow_r32", CubeListBuilder.create().texOffs(0, 11).mirror().addBox(-16.5F, -2.9F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(0, 11).addBox(11.5F, -2.9F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 97).addBox(-18.5F, -2.4F, -1.5F, 37.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 31).addBox(-2.5F, -2.4F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 0.548F, 0.548F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bow_r33 = winch_axle.addOrReplaceChild("bow_r33", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(0, 36).addBox(27.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-14.0F, 0.2652F, 0.2652F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bow_r34 = winch_axle.addOrReplaceChild("bow_r34", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(0, 36).addBox(27.5F, -0.5F, -6.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-14.0F, 0.2652F, 0.2652F, -0.7854F, 0.0F, 0.0F));

		PartDefinition string = root.addOrReplaceChild("string", CubeListBuilder.create(), PartPose.offset(-0.0091F, -17.2345F, -0.4043F));

		PartDefinition bow_r35 = string.addOrReplaceChild("bow_r35", CubeListBuilder.create().texOffs(24, 20).addBox(-0.5F, -3.0F, -0.1F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0099F, -1.1729F, -1.5436F, 0.9116F, 0.0179F, -0.0077F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);

		if (!(entity instanceof BedCrossbowEntity crossbow)) {
			return;
		}

		int releaseTicks = crossbow.getSyncedReleaseTicks();
		if (releaseTicks > 0) {
			float seconds = (1.0F - releaseTicks / (float) BedCrossbowEntity.RELEASE_ANIM_TICKS) * 0.667F;
			KeyframeAnimations.animate(this, bed_crossbowAnimation.release,
					(long) (seconds * 1000.0F), 1.0F, ANIMATION_CACHE);
		} else if (crossbow.getCharge() > 0.0F) {
			KeyframeAnimations.animate(this, bed_crossbowAnimation.charge,
					(long) (crossbow.getCharge() * 2000.0F), 1.0F, ANIMATION_CACHE);
		}

		// bow_root 的俯仰：向上/向下各 10°；正角度对应低头（与实体 xRot 一致）
		this.bow_root.xRot = (float) Math.toRadians(crossbow.getAimPitch());
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}