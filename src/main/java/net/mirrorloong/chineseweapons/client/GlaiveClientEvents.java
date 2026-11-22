package net.mirrorloong.chineseweapons.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;

public final class GlaiveClientEvents {
    private GlaiveClientEvents() {
    }

            public static void startGlaiveAnimation(GlaiveSpinStartedEvent event) {
        startGlaiveAnimation(event.getPlayer(), event.getItemStack());
    }

    public static void startGlaiveAnimation(Player player, ItemStack itemStack) {
        GlaiveSpinAnimationHandler.start(player, itemStack);
    }
}
