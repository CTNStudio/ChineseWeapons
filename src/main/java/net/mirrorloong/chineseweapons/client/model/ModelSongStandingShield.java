package net.mirrorloong.chineseweapons.client.model;

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

public class ModelSongStandingShield<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "song_standing_shield"), "main");
    private final ModelPart root;

    public ModelSongStandingShield(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-6.4494F, -21.1258F, -2.1342F, 13.02F, 39.02F, 2.02F, new CubeDeformation(0.0F))
                .texOffs(31, 11).addBox(-6.4494F, -10.1058F, -2.1342F, 13.02F, 0.0F, 2.02F, new CubeDeformation(0.0F)), PartPose.offset(-0.0606F, 5.1158F, -5.8758F));

        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(31, 38).addBox(5.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(31, 38).addBox(12.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(31, 38).addBox(-2.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8899F, 16.4904F, 6.2758F, 0.0F, 0.3927F, -1.5708F));

        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(31, 0).addBox(-1.0F, -6.0F, -1.0F, 9.0F, 9.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5687F, -15.5095F, -1.1242F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(31, 13).addBox(0.0F, -10.0F, -1.0F, 4.0F, 12.0F, 2.0F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(1.7313F, -10.3096F, -1.1242F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r4 = root.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(31, 13).addBox(-4.0F, -10.0F, -1.0F, 4.0F, 12.0F, 2.0F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(-1.6101F, -10.3096F, -1.1242F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r5 = root.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(31, 38).mirror().addBox(-5.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.2313F, 16.4904F, -1.1242F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r6 = root.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(31, 38).addBox(-2.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1101F, 16.4904F, -1.1242F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
