package net.mirrorloong.chineseweapons.rules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Slot bookkeeping for the armour casting table GUI. These procedures only
 * touch vanilla container types, so the whole algorithm is shared.
 */
public final class CastingRules {

    private CastingRules() {
    }

    /** Consumes one item from every populated input slot (1-10) and syncs the menu. */
    public static void consumeInputs(Entity entity) {
        if (entity == null) {
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }
        if (!(player.containerMenu instanceof Supplier<?> supplier)) {
            return;
        }

        Object slotsObj = supplier.get();
        if (!(slotsObj instanceof Map<?, ?> slots)) {
            return;
        }

        for (int slotIndex = 1; slotIndex <= 10; slotIndex++) {
            Object slotObj = slots.get(slotIndex);
            if (!(slotObj instanceof Slot slot)) {
                continue;
            }

            ItemStack stack = slot.getItem();
            if (!stack.isEmpty() && stack.getItem() != Blocks.AIR.asItem()) {
                slot.remove(1);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    /** Clears the output slot (11) when it still holds something. */
    public static void clearOutput(Entity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (!(player.containerMenu instanceof Supplier<?> supplier)) {
            return;
        }
        if (!(supplier.get() instanceof Map<?, ?> slots)) {
            return;
        }

        Object slotObj = slots.get(11);
        if (!(slotObj instanceof Slot slot)) {
            return;
        }

        if (slot.getItem().getItem() == Blocks.AIR.asItem()) {
            return;
        }

        slot.set(ItemStack.EMPTY);
        player.containerMenu.broadcastChanges();
    }
}
