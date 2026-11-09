package net.mirrorloong.chineseweapons.fabric.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * The armor texture lookup that Forge declared on {@code ArmorItem}
 * ({@code getArmorTexture(ItemStack, Entity, EquipmentSlot, String)}) does not
 * exist in vanilla. The shared armor items already carry this method; this
 * interface gives {@link FabricArmorRenderers} a typed way to call it.
 */
public interface ArmorTextureProvider {
    String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);
}
