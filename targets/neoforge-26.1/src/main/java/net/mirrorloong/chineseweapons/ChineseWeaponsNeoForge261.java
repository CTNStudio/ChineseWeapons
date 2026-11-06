package net.mirrorloong.chineseweapons;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeClientSetup;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeEvents;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeNetwork;
import net.mirrorloong.chineseweapons.neoforge.NeoForgePlatform;

@Mod(ChineseWeapons.MOD_ID)
public final class ChineseWeaponsNeoForge261 {
    public static final String MOD_ID = ChineseWeapons.MOD_ID;

    public ChineseWeaponsNeoForge261(IEventBus modBus) {
        NeoForgePlatform platform = new NeoForgePlatform(modBus);
        ChineseWeapons.bootstrap(platform);
        // Item data components are bound by Minecraft after data pack tags load; do not
        // build DATA_COMPONENT_INITIALIZERS here, since tag lookups are unavailable yet.
        ChineseWeapons.registerPackets();

        NeoForgeNetwork network = platform.networkImpl();
        modBus.addListener(network::registerPayloads);
        NeoForge.EVENT_BUS.register(NeoForgeEvents.class);

        if (FMLEnvironment.getDist().isClient()) {
            modBus.addListener(NeoForgeClientSetup::registerScreens);
            modBus.addListener(NeoForgeClientSetup::registerKeyMappings);
            NeoForge.EVENT_BUS.register(NeoForgeClientSetup.GameEvents.class);
        }
    }
}
