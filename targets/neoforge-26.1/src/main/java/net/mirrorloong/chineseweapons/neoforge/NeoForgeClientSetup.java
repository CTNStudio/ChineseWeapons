package net.mirrorloong.chineseweapons.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableScreen;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModKeyHandler;

public final class NeoForgeClientSetup {
    private NeoForgeClientSetup() {
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(DeferredGui.REG_MENU.get(), WeaponsCastingTableScreen::new);
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ChineseweaponsModKeyHandler.ACTION_KEY);
    }

    public static final class GameEvents {
        private GameEvents() {
        }

        @SubscribeEvent
        public static void onKey(InputEvent.Key event) {
            ChineseweaponsModKeyHandler.onKey();
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            ChineseweaponsModKeyHandler.onClientTick();
        }
    }
}
