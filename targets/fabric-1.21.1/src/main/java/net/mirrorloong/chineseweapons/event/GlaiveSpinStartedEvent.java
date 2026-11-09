package net.mirrorloong.chineseweapons.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric has no event bus for this notification: the class is kept as a plain
 * data carrier for source compatibility, and the first-person glaive spin
 * animation that consumed it is not ported (see the port report).
 */
public final class GlaiveSpinStartedEvent {
    private final Player player;
    private final ItemStack itemStack;

    public GlaiveSpinStartedEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
