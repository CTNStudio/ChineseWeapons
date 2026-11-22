package net.mirrorloong.chineseweapons.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class CustomArmorFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private final EntityModelSet modelSet;

    public CustomArmorFeatureRenderer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
        this.modelSet = modelSet;
    }

    public EntityModelSet getModelSet() {
        return modelSet;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof CustomArmorModel provider)) {
                continue;
            }
            if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() != slot) {
                continue;
            }
            renderPiece(poseStack, buffer, light, entity, slot, stack, provider,
                    limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void renderPiece(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, EquipmentSlot slot,
                             ItemStack stack, CustomArmorModel provider,
                             float limbSwing, float limbSwingAmount, float ageInTicks,
                             float netHeadYaw, float headPitch) {
                HumanoidModel armorModel = provider.getHumanoidArmorModel(entity, stack, slot, this.getParentModel());
        if (armorModel == null) {
            return;
        }
        this.getParentModel().copyPropertiesTo(armorModel);
        setPartVisibility(armorModel, slot);
        armorModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        //old api ArmorItem#getArmorTexture -> new api CustomArmorModel#getArmorTexture
                String texture = provider.getArmorTexture(stack, entity, slot, null);
        if (texture == null) {
            return;
        }
        ResourceLocation textureLocation = new ResourceLocation(texture);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
        armorModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if (stack.hasFoil()) {
            VertexConsumer glint = buffer.getBuffer(RenderType.armorEntityGlint());
            armorModel.renderToBuffer(poseStack, glint, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void setPartVisibility(HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD -> {
                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                model.body.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> {
            }
        }
    }
}
