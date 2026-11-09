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

public class Modelface_guard<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ChineseweaponsMod.MODID, "face_guard"), "main");
    public final ModelPart bipedHead;

    public Modelface_guard(ModelPart root) {
        this.bipedHead = root.getChild("bipedHead");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bipedHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.5F, -4.4F, -4.4F, -0.2007F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bipedHead.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, -6.8F, -4.2F, -0.1571F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bipedHead.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, -4.0F, -4.4F, -0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}