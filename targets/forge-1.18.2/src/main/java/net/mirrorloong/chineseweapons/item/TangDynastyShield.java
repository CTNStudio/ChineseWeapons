package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class TangDynastyShield extends ShieldItem {
    public TangDynastyShield() {
        super(new Item.Properties().durability(1456));
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemstack, ItemStack repairitem) {
        return Ingredient.of(new ItemStack(Items.OAK_PLANKS)).test(repairitem);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}
