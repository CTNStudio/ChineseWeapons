package net.mirrorloong.chineseweapons.recipes;

import java.util.function.IntFunction;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.spi.DyeableArmorSupport;

/**
 * Dye mixing for the dyeable armour recipe. Colour storage and the exact dye
 * RGB values are version-specific and come from {@link DyeableArmorSupport};
 * the averaging arithmetic is shared.
 */
public final class DyeableArmorCrafting {
    public static final int MIN_CRAFT_SIZE = 2;

    private DyeableArmorCrafting() {
    }

    public static boolean matches(IntFunction<ItemStack> slots, int size, DyeableArmorSupport dye) {
        ItemStack armorStack = ItemStack.EMPTY;
        boolean hasDye = false;

        for (int i = 0; i < size; i++) {
            ItemStack stack = slots.apply(i);
            if (!stack.isEmpty()) {
                if (dye.isDyeable(stack)) {
                    if (!armorStack.isEmpty()) {
                        return false;
                    }
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

    public static ItemStack assemble(IntFunction<ItemStack> slots, int size, DyeableArmorSupport dye) {
        ItemStack armorStack = ItemStack.EMPTY;

        for (int i = 0; i < size; i++) {
            ItemStack stack = slots.apply(i);
            if (!stack.isEmpty() && dye.isDyeable(stack)) {
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

        if (dye.hasCustomColor(armorStack)) {
            int color = dye.getColor(armorStack);
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            maxComponent += (int) (Math.max(r, Math.max(g, b)) * 255.0F);
            colorComponents[0] += (int) (r * 255.0F);
            colorComponents[1] += (int) (g * 255.0F);
            colorComponents[2] += (int) (b * 255.0F);
            dyeCount++;
        }

        for (int i = 0; i < size; i++) {
            ItemStack stack = slots.apply(i);
            if (!stack.isEmpty() && stack.getItem() instanceof DyeItem) {
                int[] dyeColors = dye.dyeRgb(stack);
                int r = dyeColors[0];
                int g = dyeColors[1];
                int b = dyeColors[2];
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
        float maxAverage = (float) maxComponent / (float) dyeCount;
        float maxCurrent = (float) Math.max(r, Math.max(g, b));

        if (maxCurrent > 0) {
            r = (int) ((float) r * maxAverage / maxCurrent);
            g = (int) ((float) g * maxAverage / maxCurrent);
            b = (int) ((float) b * maxAverage / maxCurrent);
        }

        dye.setColor(armorStack, (r << 16) | (g << 8) | b);
        return armorStack;
    }

    public static NonNullList<ItemStack> remainingItems(IntFunction<ItemStack> slots, int size) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack stack = slots.apply(i);
            if (stack.getItem() instanceof DyeItem) {
                remainingItems.set(i, ItemStack.EMPTY);
            }
        }

        return remainingItems;
    }

    public static boolean canCraftInDimensions(int width, int height) {
        return width * height >= MIN_CRAFT_SIZE;
    }
}
