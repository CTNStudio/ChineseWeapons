package net.mirrorloong.chineseweapons.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChineseweaponsModEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE.get(),
                SongJavelinRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.STANDING_SHIELD.get(),
                SongStandingShieldRenderer::new);
    }
}