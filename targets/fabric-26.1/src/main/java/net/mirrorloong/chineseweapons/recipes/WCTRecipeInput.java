package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * The casting table exposes a ten-slot {@link Container}; 26.1 recipes are
 * typed over {@link RecipeInput} instead of {@code Container}.
 */
public record WCTRecipeInput(Container container) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return container.getItem(index);
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }
}
