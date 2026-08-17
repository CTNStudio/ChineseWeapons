package net.mirrorloong.chineseweapons.item;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TangDynastyInfantryArmorforgingStone extends Item {
    public TangDynastyInfantryArmorforgingStone() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);
        list.add(Component.translatable("item.chineseweapons.tang_dynasty_infantry_armor_forging_stone.tile"));
    }
}
