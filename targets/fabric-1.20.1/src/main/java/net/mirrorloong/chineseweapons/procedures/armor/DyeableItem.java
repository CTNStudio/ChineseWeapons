package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface DyeableItem {

    String TAG_COLOR = "Color";
    String TAG_DISPLAY = "display";

    /**
     * Whether this item participates in dye recipes and cauldron cleaning.
     * The old iron black-chui armour merged dye textures without advertising
     * itself as dyeable, so the generic item keeps that distinction.
     */
    default boolean isDyeable() {
        return true;
    }

    static boolean hasCustomColor(ItemStack stack) {
        if (stack.isEmpty()) return false;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        return tag != null && tag.contains(TAG_COLOR);
    }

    static int getColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        if (tag != null && tag.contains(TAG_COLOR)) {
            return tag.getInt(TAG_COLOR);
        }
        return 0xFFFFFF;
    }

    static void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, color);
    }

    static void removeColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        if (tag != null) {
            tag.remove(TAG_COLOR);
            if (tag.isEmpty()) {
                stack.removeTagKey(TAG_DISPLAY);
            }
        }
    }

    static void addDyeTooltip(ItemStack stack, List<Component> tooltip) {
        if (hasCustomColor(stack)) {
            tooltip.add(net.mirrorloong.chineseweapons.rules.DyeRules.tooltipLine(getColor(stack)));
        }
    }
}