package net.mirrorloong.chineseweapons.item;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class ForgingStoneItem extends Item {
    private final String tooltipKey;

    public ForgingStoneItem(String tooltipKey) {
        super(ItemIds.apply(new Properties()));
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable(tooltipKey));
    }
}
