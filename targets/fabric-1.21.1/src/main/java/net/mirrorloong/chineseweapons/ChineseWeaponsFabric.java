package net.mirrorloong.chineseweapons;

import net.fabricmc.api.ModInitializer;
import net.mirrorloong.chineseweapons.fabric.FabricEvents;
import net.mirrorloong.chineseweapons.fabric.FabricNetwork;
import net.mirrorloong.chineseweapons.fabric.FabricPlatform;
import net.mirrorloong.chineseweapons.fabric.FabricTrades;

public final class ChineseWeaponsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricNetwork.registerPayloadTypes();
        ChineseWeapons.bootstrap(new FabricPlatform());
        ChineseWeapons.registerPackets();
        FabricEvents.register();
        FabricTrades.register();
    }
}
