package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public final class EnchantmentLevels {
    private EnchantmentLevels() {
    }

    public static int level(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantment)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack))
                .orElse(0);
    }
}
