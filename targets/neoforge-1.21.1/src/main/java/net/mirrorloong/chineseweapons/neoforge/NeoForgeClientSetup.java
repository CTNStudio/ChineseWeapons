package net.mirrorloong.chineseweapons.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.mirrorloong.chineseweapons.client.ChineseWeaponsClientEvents;
import net.mirrorloong.chineseweapons.client.GlaiveItemClientEvent;
import net.mirrorloong.chineseweapons.client.GlaiveSpinAnimationHandler;
import net.mirrorloong.chineseweapons.client.TangDynastyHengSaberClientEvents;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableScreen;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEvents;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModKeyHandler;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModModels;

public final class NeoForgeClientSetup {
    private NeoForgeClientSetup() {
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(DeferredGui.REG_MENU.get(), WeaponsCastingTableScreen::new);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ChineseWeaponsClientEvents.onClientSetup(event);
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ChineseWeaponsClientEvents.registerItemColors(event);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ChineseweaponsModModels.registerLayerDefinitions(event);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ChineseweaponsModEvents.registerEntityRenderers(event);
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ChineseweaponsModKeyHandler.ACTION_KEY);
    }

    public static final class GameEvents {
        private GameEvents() {
        }

        @SubscribeEvent
        public static void onKey(InputEvent.Key event) {
            ChineseweaponsModKeyHandler.onKey(event);
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            GlaiveSpinAnimationHandler.onClientTick();
        }

        @SubscribeEvent
        public static void onRenderHand(RenderHandEvent event) {
            GlaiveSpinAnimationHandler.onRenderHand(event);
            GlaiveItemClientEvent.modifyItemRender(event);
            TangDynastyHengSaberClientEvents.modifyItemRender(event);
        }

        @SubscribeEvent
        public static void onGlaiveSpinStarted(GlaiveSpinStartedEvent event) {
            GlaiveSpinAnimationHandler.start(event.getPlayer(), event.getItemStack());
        }
    }
}
