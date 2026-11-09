package net.mirrorloong.chineseweapons;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModTrades;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeClientSetup;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeEvents;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeNetwork;
import net.mirrorloong.chineseweapons.neoforge.NeoForgePlatform;

@Mod(ChineseWeapons.MOD_ID)
public final class ChineseWeaponsNeoForge121 {
    public static final String MOD_ID = ChineseWeapons.MOD_ID;

    public ChineseWeaponsNeoForge121(IEventBus modBus) {
        NeoForgePlatform platform = new NeoForgePlatform(modBus);
        ChineseWeapons.bootstrap(platform);
        ChineseWeapons.registerPackets();

        NeoForgeNetwork network = platform.networkImpl();
        modBus.addListener(network::registerPayloads);
        NeoForge.EVENT_BUS.register(NeoForgeEvents.class);
        NeoForge.EVENT_BUS.register(ChineseweaponsModTrades.class);

        if (FMLEnvironment.dist.isClient()) {
            modBus.addListener(NeoForgeClientSetup::registerScreens);
            modBus.addListener(NeoForgeClientSetup::onClientSetup);
            modBus.addListener(NeoForgeClientSetup::registerItemColors);
            modBus.addListener(NeoForgeClientSetup::registerLayerDefinitions);
            modBus.addListener(NeoForgeClientSetup::registerEntityRenderers);
            modBus.addListener(NeoForgeClientSetup::registerKeyMappings);
            NeoForge.EVENT_BUS.register(NeoForgeClientSetup.GameEvents.class);
        }
    }
}
