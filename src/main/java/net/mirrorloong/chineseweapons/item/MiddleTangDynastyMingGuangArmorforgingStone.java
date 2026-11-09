package net.mirrorloong.chineseweapons.item;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class MiddleTangDynastyMingGuangArmorforgingStone extends Item {
    public MiddleTangDynastyMingGuangArmorforgingStone() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.chineseweapons.middle_tang_dynasty_ming_guang_armor_forging_stone.tile"));
    }
}
