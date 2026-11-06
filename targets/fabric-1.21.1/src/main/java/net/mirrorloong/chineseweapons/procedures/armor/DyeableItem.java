package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public interface DyeableItem {

    String TAG_COLOR = "Color";
    String TAG_DISPLAY = "display";

    private static CompoundTag customTag(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    static boolean hasCustomColor(ItemStack stack) {
        if (stack.isEmpty()) return false;
        CompoundTag tag = customTag(stack).getCompound(TAG_DISPLAY);
        return tag.contains(TAG_COLOR);
    }

    static int getColor(ItemStack stack) {
        CompoundTag tag = customTag(stack).getCompound(TAG_DISPLAY);
        if (tag.contains(TAG_COLOR)) {
            return tag.getInt(TAG_COLOR);
        }
        return 0xFFFFFF;
    }

    static void setColor(ItemStack stack, int color) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            CompoundTag display = tag.getCompound(TAG_DISPLAY);
            display.putInt(TAG_COLOR, color);
            tag.put(TAG_DISPLAY, display);
        });
    }

    static void removeColor(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            CompoundTag display = tag.getCompound(TAG_DISPLAY);
            display.remove(TAG_COLOR);
            if (display.isEmpty()) {
                tag.remove(TAG_DISPLAY);
            } else {
                tag.put(TAG_DISPLAY, display);
            }
        });
    }

    static void addDyeTooltip(ItemStack stack, List<Component> tooltip) {
        if (hasCustomColor(stack)) {
            tooltip.add(net.mirrorloong.chineseweapons.rules.DyeRules.tooltipLine(getColor(stack)));
        }
    }
}