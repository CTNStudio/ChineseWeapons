package net.mirrorloong.chineseweapons.init;

import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.renderer.BedCrossbowBoltRenderer;
import net.mirrorloong.chineseweapons.renderer.BedCrossbowRenderer;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT)
public class ChineseweaponsModEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE.get(),
                SongJavelinRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.STANDING_SHIELD.get(),
                SongStandingShieldRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.BED_CROSSBOW.get(),
                BedCrossbowRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.BED_CROSSBOW_BOLT.get(),
                BedCrossbowBoltRenderer::new);
    }
}
