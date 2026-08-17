package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

public class DyeableArmorRecipe extends CustomRecipe {

    public static final RecipeSerializer<DyeableArmorRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(DyeableArmorRecipe::new);

    public DyeableArmorRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack armorStack = ItemStack.EMPTY;
        boolean hasDye = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (isDyeableItem(stack)) {
                    if (!armorStack.isEmpty()) return false;
                    armorStack = stack;
                } else if (stack.getItem() instanceof DyeItem) {
                    hasDye = true;
                } else {
                    return false;
                }
            }
        }

        return !armorStack.isEmpty() && hasDye;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {
        ItemStack armorStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && isDyeableItem(stack)) {
                armorStack = stack.copy();
                armorStack.setCount(1);
                break;
            }
        }

        if (armorStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int[] colorComponents = new int[3];
        int maxComponent = 0;
        int dyeCount = 0;

        if (DyeableItem.hasCustomColor(armorStack)) {
            int color = DyeableItem.getColor(armorStack);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            maxComponent += (int)(Math.max(r, Math.max(g, b)) * 255.0F);
            colorComponents[0] += (int)(r * 255.0F);
            colorComponents[1] += (int)(g * 255.0F);
            colorComponents[2] += (int)(b * 255.0F);
            dyeCount++;
        }

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof DyeItem dyeItem) {
                int c = dyeItem.getDyeColor().getTextureDiffuseColor();
                int r = (c >> 16) & 255;
                int g = (c >> 8) & 255;
                int b = c & 255;
                maxComponent += Math.max(r, Math.max(g, b));
                colorComponents[0] += r;
                colorComponents[1] += g;
                colorComponents[2] += b;
                dyeCount++;
            }
        }

        if (dyeCount == 0) {
            return ItemStack.EMPTY;
        }

        int r = colorComponents[0] / dyeCount;
        int g = colorComponents[1] / dyeCount;
        int b = colorComponents[2] / dyeCount;
        float maxAverage = (float)maxComponent / (float)dyeCount;
        float maxCurrent = (float)Math.max(r, Math.max(g, b));

        if (maxCurrent > 0) {
            r = (int)((float)r * maxAverage / maxCurrent);
            g = (int)((float)g * maxAverage / maxCurrent);
            b = (int)((float)b * maxAverage / maxCurrent);
        }

        int finalColor = (r << 16) | (g << 8) | b;
        DyeableItem.setColor(armorStack, finalColor);

        return armorStack;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof DyeItem) {
                remainingItems.set(i, ItemStack.EMPTY);
            }
        }

        return remainingItems;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private boolean isDyeableItem(ItemStack stack) {
        return stack.getItem() instanceof DyeableItem;
    }
}
