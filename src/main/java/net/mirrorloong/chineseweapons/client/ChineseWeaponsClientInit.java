package net.mirrorloong.chineseweapons.client;

import net.fabricmc.api.ClientModInitializer;
import net.mirrorloong.chineseweapons.client.armor.ArmorRendererRegistry;
import net.mirrorloong.chineseweapons.network.NetworkHandler;
import net.mirrorloong.chineseweapons.procedures.SectionBanner;

public class ChineseWeaponsClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
                NetworkHandler.initClient();

                ArmorRendererRegistry.init();

        //old api ScreenEvent.Render.Post + withTabFactory -> new api ScreenEvents + FabricItemGroup
        SectionBanner.init();

                ChineseWeaponsClientEvents.initClient();

        //old api @Mod.EventBusSubscriber(CLIENT) -> new api BedCrossbowClientEvents.init() / BedCrossbowHudRenderer.init()
        BedCrossbowClientEvents.init();
        net.mirrorloong.chineseweapons.gui.BedCrossbowHudRenderer.init();

        net.mirrorloong.chineseweapons.client.item.WeaponItemRenderer.init();

                net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableScreen.register();
    }
}
