package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.procedures.weapon.OffhandClearProcedure;

public abstract class DaggerAxeItemBase extends FiveBlockReachSwordItem {
    protected DaggerAxeItemBase(Item.Properties properties) {
        super(properties);
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
