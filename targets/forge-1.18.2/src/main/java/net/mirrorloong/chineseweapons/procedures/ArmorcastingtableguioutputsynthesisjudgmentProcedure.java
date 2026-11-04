package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;
import java.util.Map;

public class ArmorcastingtableguioutputsynthesisjudgmentProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;

        if (!(entity instanceof Player player)) return;
        if (!(player.containerMenu instanceof Supplier<?> supplier)) return;

        Object slotsObj = supplier.get();
        if (!(slotsObj instanceof Map<?, ?> slots)) return;

        for (int slotIndex = 1; slotIndex <= 10; slotIndex++) {
            Object slotObj = slots.get(slotIndex);
            if (!(slotObj instanceof Slot slot)) continue;

            ItemStack stack = slot.getItem();
            if (!stack.isEmpty() && stack.getItem() != Blocks.AIR.asItem()) {
                slot.remove(1);
                player.containerMenu.broadcastChanges();
            }
        }
    }
}