package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Adapts the casting table's {@link Container} to the 1.21 recipe input type.
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
