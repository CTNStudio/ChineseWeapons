package net.mirrorloong.chineseweapons.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.item.FabricArmorItem;
import net.mirrorloong.chineseweapons.item.FabricArmorModels;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableArmorTextures;

/**
 * Registers a Fabric {@link ArmorRenderer} for every shared armor item that
 * used to provide its model through Forge's client item extensions. Vanilla's
 * armor layer only asks Fabric for a renderer after it has already run
 * {@code setupAnim} on the parent model, so the renderer mirrors
 * {@link HumanoidArmorLayer}: copy the parent pose, apply slot visibility and
 * draw the model with the item's (possibly dyed) texture.
 */
public final class FabricArmorRenderers {
    private FabricArmorRenderers() {
    }

    public static void register() {
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (key == null || !ChineseWeapons.MOD_ID.equals(key.getNamespace())) {
                continue;
            }
            if (!(item instanceof FabricArmorItem armorItem)) {
                continue;
            }
            ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) ->
                    render(armorItem, matrices, vertexConsumers, stack, entity, slot, light, contextModel), item);
        }
    }

    private static void render(FabricArmorItem armorItem,
            PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity,
            EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        if (armorItem.getEquipmentSlot() != slot) {
            return;
        }

        ArmorSlot armorSlot = switch (slot) {
            case HEAD -> ArmorSlot.HELMET;
            case CHEST -> ArmorSlot.CHESTPLATE;
            case LEGS -> ArmorSlot.LEGGINGS;
            case FEET -> ArmorSlot.BOOTS;
            default -> null;
        };
        if (armorSlot == null) {
            return;
        }

        HumanoidModel<LivingEntity> model = FabricArmorModels.create(
                armorItem.definition().modelId(), armorSlot, entity, contextModel);
        contextModel.copyPropertiesTo(model);
        setPartVisibility(model, slot);

        String texturePath = armorItem.definition().overlayTexture().isEmpty()
                ? armorItem.definition().baseTexture()
                : DyeableArmorTextures.getArmorTexture(stack, null,
                        armorItem.definition().baseTexture(), armorItem.definition().overlayTexture());
        ResourceLocation texture = ResourceLocation.tryParse(texturePath);
        if (texture == null) {
            return;
        }

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(texture));
        model.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if (stack.hasFoil()) {
            model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorEntityGlint()),
                    light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void setPartVisibility(HumanoidModel<LivingEntity> model, EquipmentSlot slot) {
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
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
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
