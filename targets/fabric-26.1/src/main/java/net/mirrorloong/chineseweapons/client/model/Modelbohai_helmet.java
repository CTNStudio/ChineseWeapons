package net.mirrorloong.chineseweapons.client.model;
// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseWeapons;

public class Modelbohai_helmet<T extends Entity> extends EntityModel<net.minecraft.client.renderer.entity.state.EntityRenderState> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "bohai_helmet"), "main");
    public final ModelPart bipedHead;

	public Modelbohai_helmet(ModelPart root) {
        super(root);
		this.bipedHead = root.getChild("bipedHead");
	}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -1.9F, -5.5F, 11.0F, 5.0F, 11.0F, new CubeDeformation(0.3F))
                .texOffs(0, 32).addBox(-4.0F, -11.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 16).addBox(-1.5F, -16.4F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -8.9F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(32, 22).addBox(-1.0F, -13.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}