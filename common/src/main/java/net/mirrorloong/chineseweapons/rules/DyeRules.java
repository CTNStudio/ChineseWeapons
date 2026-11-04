package net.mirrorloong.chineseweapons.rules;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Colour helpers shared by the dyeable-armour implementations. Where the raw
 * colour is stored (legacy {@code display.Color} NBT vs
 * {@code DataComponents.DYED_COLOR} vs {@code CUSTOM_DATA}) stays in the target;
 * only the presentation is shared.
 */
public final class DyeRules {

    public static final int DEFAULT_COLOR = 0xFFFFFF;

    private DyeRules() {
    }

    public static String hex(int color) {
        return String.format("#%06X", color);
    }

    public static Component tooltipLine(int color) {
        return ChineseWeapons.gameplay().translatableWithStyle(
                "text.chineseweapons.color", ChatFormatting.GRAY, hex(color));
    }
}
