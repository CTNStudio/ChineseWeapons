package net.mirrorloong.chineseweapons.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

public class DAGGERAXEItem implements net.minecraftforge.common.IExtensibleEnum {
    public static final EnchantmentCategory DAGGER_AXE = EnchantmentCategory.create("DAGGER_AXE",
			(item) -> item == ChineseweaponsModItems.GOLDEN_DAGGER_AXE.get() ||
					item == ChineseweaponsModItems.DIAMOND_DAGGER_AXE.get() ||
					item == ChineseweaponsModItems.IRON_DAGGER_AXE.get() ||
					item == ChineseweaponsModItems.WOODEN_DAGGER_AXE.get() ||
                    item == ChineseweaponsModItems.NETHERITE_DAGGER_AXE.get() ||
                    item == ChineseweaponsModItems.STONE_DAGGER_AXE.get()
    );
}
