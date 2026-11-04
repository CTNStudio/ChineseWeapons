package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.spi.StandingShieldItem;

public class SongStandingShield extends ShieldItem implements StandingShieldItem {

    public SongStandingShield() {
        super(new Item.Properties().durability(1670));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return Ingredient.of(new ItemStack(Items.OAK_PLANKS)).test(repair);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
}
