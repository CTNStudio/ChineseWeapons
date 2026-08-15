package net.mirrorloong.chineseweapons.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class GlaiveClientEvents {
    private GlaiveClientEvents() {
    }

    @SubscribeEvent
    public static void startGlaiveAnimation(GlaiveSpinStartedEvent event) {
        GlaiveSpinAnimationHandler.start(event.getPlayer(), event.getItemStack());
    }
}
