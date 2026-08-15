package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class TangDynastyHorseItem extends Item {
    public TangDynastyHorseItem(int defense, ResourceLocation texture) {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(0));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return 0;
    }
}