package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OffhandClearProcedure {
    public static void _362(Player player) {
        if (player == null) return;
        if (player.level().isClientSide()) return;
        ItemStack offhand = player.getOffhandItem();
        if (offhand.isEmpty()) return;
        ItemStack toMove = offhand.copy();
        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        if (!player.addItem(toMove)) {
            player.drop(toMove, false);
        } else if (!toMove.isEmpty()) {
            player.drop(toMove, false);
        }
    }
}