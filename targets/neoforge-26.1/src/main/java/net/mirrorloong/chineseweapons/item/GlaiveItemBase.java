package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemGetLostKillProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.OffhandClearProcedure;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

public abstract class GlaiveItemBase extends FourBlockReachSwordItem {
    protected GlaiveItemBase(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (GlaiveItemGetLostKillProcedure.tryStart(world, player, hand, itemStack)) {
            return InteractionResult.SUCCESS;
        }
        return super.use(world, player, hand);
    }

    public static boolean isGlaiveItem(ItemStack stack) {
        return stack.getItem() instanceof GlaiveItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (!(entity instanceof Player player)) {
            return;
        }
        if (!ItemStack.matches(player.getMainHandItem(), stack)) {
            return;
        }
        OffhandClearProcedure._362(player);
    }
}
