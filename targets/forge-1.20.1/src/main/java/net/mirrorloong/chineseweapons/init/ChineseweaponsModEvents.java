package net.mirrorloong.chineseweapons.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChineseweaponsModEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ChineseweaponsModEntities.type(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE),
                SongJavelinRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.type(ChineseweaponsModEntities.STANDING_SHIELD),
                SongStandingShieldRenderer::new);
    }
}