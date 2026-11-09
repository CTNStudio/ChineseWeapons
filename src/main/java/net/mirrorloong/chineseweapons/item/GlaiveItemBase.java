package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemGetLostKillProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.OffhandClearProcedure;

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

    public static boolean isGlaiveItem(ItemStack stack) {
        return stack.getItem() == ChineseweaponsModItems.DIAMOND_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.GOLDEN_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.GREENLOONG_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.IRON_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.WOODEN_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.NETHERITE_GLAIVE.get()
                || stack.getItem() == ChineseweaponsModItems.STONE_GLAIVE.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide()) return;
        if (!(entity instanceof Player player)) return;
        if (player.getMainHandItem() != stack) return;
        OffhandClearProcedure._362(player);
    }
}
