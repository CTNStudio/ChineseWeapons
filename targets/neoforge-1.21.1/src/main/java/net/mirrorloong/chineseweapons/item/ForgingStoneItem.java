package net.mirrorloong.chineseweapons.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ForgingStoneItem extends Item {
    private final String tooltipKey;

    public ForgingStoneItem(String tooltipKey) {
        super(new Properties());
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);
        list.add(Component.translatable(tooltipKey));
    }
}
