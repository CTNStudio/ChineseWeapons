package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.spi.DyeableArmorSupport;

public class DyeableArmorRecipe extends CustomRecipe {

    public static final RecipeSerializer<DyeableArmorRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(DyeableArmorRecipe::new);

    private static final DyeableArmorSupport DYE = new DyeableArmorSupport() {
        @Override
        public boolean isDyeable(ItemStack stack) {
            return stack.getItem() instanceof DyeableItem;
        }

        @Override
        public boolean hasCustomColor(ItemStack stack) {
            return DyeableItem.hasCustomColor(stack);
        }

        @Override
        public int getColor(ItemStack stack) {
            return DyeableItem.getColor(stack);
        }

        @Override
        public void setColor(ItemStack stack, int color) {
            DyeableItem.setColor(stack, color);
        }

        @Override
        public int[] dyeRgb(ItemStack dyeStack) {
            int color = ((DyeItem) dyeStack.getItem()).getDyeColor().getTextureDiffuseColor();
            return new int[]{(color >> 16) & 255, (color >> 8) & 255, color & 255};
        }
    };

    public DyeableArmorRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        return DyeableArmorCrafting.matches(container::getItem, container.size(), DYE);
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider registries) {
        return DyeableArmorCrafting.assemble(container::getItem, container.size(), DYE);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        return DyeableArmorCrafting.remainingItems(container::getItem, container.size());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return DyeableArmorCrafting.canCraftInDimensions(width, height);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
