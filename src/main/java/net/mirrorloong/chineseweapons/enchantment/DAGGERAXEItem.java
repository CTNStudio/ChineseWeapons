package net.mirrorloong.chineseweapons.enchantment;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

public class DAGGERAXEItem {
    public static final EnchantmentCategory DAGGER_AXE = EnchantmentCategory.WEAPON;

    public static boolean isDaggerAxe(Item item) {
        return item == ChineseweaponsModItems.GOLDEN_DAGGER_AXE.get() ||
                item == ChineseweaponsModItems.DIAMOND_DAGGER_AXE.get() ||
                item == ChineseweaponsModItems.IRON_DAGGER_AXE.get() ||
                item == ChineseweaponsModItems.WOODEN_DAGGER_AXE.get() ||
                item == ChineseweaponsModItems.NETHERITE_DAGGER_AXE.get() ||
                item == ChineseweaponsModItems.STONE_DAGGER_AXE.get();
    }
}
