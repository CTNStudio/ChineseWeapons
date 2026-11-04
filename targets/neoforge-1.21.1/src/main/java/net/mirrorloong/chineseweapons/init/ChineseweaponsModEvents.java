package net.mirrorloong.chineseweapons.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

public class ChineseweaponsModEvents {

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ChineseweaponsModEntities.type(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE),
                SongJavelinRenderer::new);

        event.registerEntityRenderer(ChineseweaponsModEntities.type(ChineseweaponsModEntities.STANDING_SHIELD),
                SongStandingShieldRenderer::new);
    }
}
