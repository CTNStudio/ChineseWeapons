package net.mirrorloong.chineseweapons;

import net.fabricmc.api.ModInitializer;
import net.mirrorloong.chineseweapons.fabric.FabricEvents;
import net.mirrorloong.chineseweapons.fabric.FabricPlatform;

public final class ChineseWeaponsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ChineseWeapons.bootstrap(new FabricPlatform());
        // Item data components are bound by Minecraft after data pack tags load; do not
        // build DATA_COMPONENT_INITIALIZERS here, since tag lookups are unavailable yet.
        ChineseWeapons.registerPackets();
        FabricEvents.register();
    }
}
