package net.mirrorloong.chineseweapons.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class GlaiveSpinStartedEvent {
    private static final List<Consumer<GlaiveSpinStartedEvent>> LISTENERS = new CopyOnWriteArrayList<>();

    private final Player player;
    private final ItemStack itemStack;

    public GlaiveSpinStartedEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public static void register(Consumer<GlaiveSpinStartedEvent> listener) {
        LISTENERS.add(listener);
    }

        public static void fire(Player player, ItemStack itemStack) {
        GlaiveSpinStartedEvent event = new GlaiveSpinStartedEvent(player, itemStack);
        for (Consumer<GlaiveSpinStartedEvent> listener : LISTENERS) {
            listener.accept(event);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
