package net.mirrorloong.chineseweapons.init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

public class ChineseweaponsModEvents {
    //old api @SubscribeEvent registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) -> new api EntityRendererRegistry.register(...)
    public static void registerEntityRenderers() {
                EntityRendererRegistry.register(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE.get(),
                SongJavelinRenderer::new);
                EntityRendererRegistry.register(ChineseweaponsModEntities.STANDING_SHIELD.get(),
                SongStandingShieldRenderer::new);
    }
}
