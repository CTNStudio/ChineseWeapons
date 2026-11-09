package net.mirrorloong.chineseweapons.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;

@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class GlaiveClientEvents {
    private GlaiveClientEvents() {
    }

    @SubscribeEvent
    public static void startGlaiveAnimation(GlaiveSpinStartedEvent event) {
        GlaiveSpinAnimationHandler.start(event.getPlayer(), event.getItemStack());
    }
}
