package net.mirrorloong.chineseweapons.spi;

import net.minecraft.world.item.ItemStack;

/**
 * Version-specific half of the dyeable armour recipe. Where the colour is
 * stored and how vanilla dye colours are computed changed between 1.20.1,
 * 1.21.1 and 26.1, so each target supplies this and the mixing stays shared.
 */
public interface DyeableArmorSupport {

    boolean isDyeable(ItemStack stack);

    boolean hasCustomColor(ItemStack stack);

    int getColor(ItemStack stack);

    void setColor(ItemStack stack, int color);

    /** RGB components (0-255) of a vanilla dye stack, computed like the target's version. */
    int[] dyeRgb(ItemStack dyeStack);
}
