package net.mirrorloong.chineseweapons.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
