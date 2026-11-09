package net.mirrorloong.chineseweapons.fabric.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/**
 * The armor texture lookup that NeoForge adds to {@code ArmorItem} does not
 * exist in vanilla 1.21.1. The shared armor items already carry a method with
 * this signature (the 1.21.1 NeoForge hook); this interface gives
 * {@link FabricArmorRenderers} a typed way to call it.
 */
public interface ArmorTextureProvider {
    ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
            ArmorMaterial.Layer layer, boolean innerModel);
}
