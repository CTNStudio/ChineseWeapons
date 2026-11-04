package net.mirrorloong.chineseweapons.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.common.MinecraftForge;
import net.mirrorloong.chineseweapons.ChineseWeapons;

@Mod(ChineseWeapons.MOD_ID)
public final class ChineseWeaponsForge {
    public ChineseWeaponsForge(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();

        ChineseWeapons.bootstrap(new ForgePlatform(modBus));

        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
        modBus.addListener(ChineseWeaponsForge::commonSetup);

        if (FMLEnvironment.dist.isClient()) {
            modBus.addListener(ForgeClientSetup::clientSetup);
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ChineseWeapons::registerPackets);
    }
}
