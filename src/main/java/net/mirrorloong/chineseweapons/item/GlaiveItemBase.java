package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.GlaiveItemGetLostKillProcedure;

public abstract class GlaiveItemBase extends FourBlockReachSwordItem {
    protected GlaiveItemBase(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (GlaiveItemGetLostKillProcedure.tryStart(world, player, hand, itemStack)) {
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
        }
        return super.use(world, player, hand);
    }
}
