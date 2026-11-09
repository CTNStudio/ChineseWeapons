package net.mirrorloong.chineseweapons.enchantment;

import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Blocks;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import java.util.Set;

public class CavalryBusterStar extends Enchantment implements net.minecraftforge.common.IExtensibleEnum {
    public CavalryBusterStar() {
        super(Rarity.UNCOMMON,
                    DAGGERAXEItem.DAGGER_AXE,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }


    @Override
    public int getMinCost(int level) {
        return 1 + level * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return 6 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
