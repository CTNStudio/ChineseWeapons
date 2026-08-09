package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

public class DyeableArmorRecipe extends CustomRecipe {

    public static final RecipeSerializer<DyeableArmorRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(DyeableArmorRecipe::new);

    public DyeableArmorRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack armorStack = ItemStack.EMPTY;
        boolean hasDye = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
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
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack armorStack = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
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

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof DyeItem dyeItem) {
                float[] dyeColors = dyeItem.getDyeColor().getTextureDiffuseColors();
                int r = (int)(dyeColors[0] * 255.0F);
                int g = (int)(dyeColors[1] * 255.0F);
                int b = (int)(dyeColors[2] * 255.0F);
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
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack stack = container.getItem(i);
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