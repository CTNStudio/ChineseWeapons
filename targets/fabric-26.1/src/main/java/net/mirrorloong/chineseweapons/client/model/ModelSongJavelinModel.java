package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.ChineseWeapons;

public class ModelSongJavelinModel<T extends Entity> extends EntityModel<net.minecraft.client.renderer.entity.state.EntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "song_javelin"), "main");

    private final ModelPart group;

    public ModelSongJavelinModel(ModelPart root) {
        super(root);
        this.group = root.getChild("group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 17.0959F, -0.4971F, 1.0F, 17.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -18.9041F, -0.4971F, 1.0F, 36.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 9).addBox(0.0F, -16.2041F, -1.9971F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-1.5F, -7.4041F, -1.4971F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 7.4041F, -0.0029F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 9).addBox(-0.5F, -6.3131F, 4.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(-0.5F, -1.291F, -0.4971F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}