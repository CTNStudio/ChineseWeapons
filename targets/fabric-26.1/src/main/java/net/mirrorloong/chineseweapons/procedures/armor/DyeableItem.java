package net.mirrorloong.chineseweapons.procedures.armor;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

/**
 * Dye state for the mod's armour pieces.
 *
 * <p>26.1 no longer allows items to supply a generated armour texture: armour
 * is rendered from data-driven equipment assets. The stored colour therefore
 * moved from the legacy {@code display.Color} NBT tag to the vanilla
 * {@link DataComponents#DYED_COLOR} component, which keeps the dye recipe,
 * tooltip and cauldron cleaning working. The generated overlay textures that
 * the 1.20.1 implementation produced at runtime are not reproduced.</p>
 */
public interface DyeableItem {
    /**
     * Whether this item participates in dye recipes and cauldron cleaning.
     * The old iron black-chui armour merged dye textures without advertising
     * itself as dyeable, so the generic item keeps that distinction.
     */
    default boolean isDyeable() {
        return true;
    }

    static boolean hasCustomColor(ItemStack stack) {
        return !stack.isEmpty() && stack.has(DataComponents.DYED_COLOR);
    }

    static int getColor(ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, 0xFFFFFF);
    }

    static void setColor(ItemStack stack, int color) {
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
    }

    static void removeColor(ItemStack stack) {
        stack.remove(DataComponents.DYED_COLOR);
    }

    /**
     * Appends the colour line. The old overload took a mutable tooltip list;
     * 26.1 tooltips are written through a consumer.
     */
    static void addDyeTooltip(ItemStack stack, List<Component> tooltip) {
        addDyeTooltip(stack, tooltip::add);
    }

    static void addDyeTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (hasCustomColor(stack)) {
            tooltip.accept(net.mirrorloong.chineseweapons.rules.DyeRules.tooltipLine(getColor(stack)));
        }
    }
}
