package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.mirrorloong.chineseweapons.procedures.weapon.OffhandClearProcedure;

public abstract class DaggerAxeItemBase extends FiveBlockReachSwordItem {
    protected DaggerAxeItemBase(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties) {
        super(material, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (!(entity instanceof Player player)) {
            return;
        }
        if (player.getMainHandItem() != stack) {
            return;
        }
        OffhandClearProcedure._362(player);
    }
}
