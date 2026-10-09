package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.weapon.OffhandClearProcedure;

public abstract class DaggerAxeItemBase extends FiveBlockReachSwordItem {
    protected DaggerAxeItemBase(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
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
