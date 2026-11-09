package net.mirrorloong.chineseweapons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEnchantments;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlockEntities;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModTabs;
import net.mirrorloong.chineseweapons.network.ArmorCastingTableBlockProgressPacket;
import net.mirrorloong.chineseweapons.network.ArmorcastingtableguiSlotMessage;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import net.mirrorloong.chineseweapons.network.SquashPacket;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;
import net.mirrorloong.chineseweapons.spi.Platform;
import net.mirrorloong.chineseweapons.spi.Registration;

/**
 * Loader-neutral mod entry point. Targets create their {@link Platform}
 * implementation and call {@link #bootstrap(Platform)} from their mod
 * constructor, then {@link #registerPackets()} once the loader networking
 * channel is available.
 */
public final class ChineseWeapons {
    public static final String MOD_ID = "chineseweapons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Platform platform;

    private ChineseWeapons() {
    }

    public static Platform platform() {
        if (platform == null) {
            throw new IllegalStateException("Chinese Weapons platform is not initialized");
        }
        return platform;
    }

    public static Registration registration() {
        return platform().registration();
    }

    public static Network network() {
        return platform().network();
    }

    public static GameplayCompat gameplay() {
        return platform().gameplay();
    }

    public static void bootstrap(Platform implementation) {
        if (platform != null) {
            throw new IllegalStateException("Chinese Weapons bootstrap already ran");
        }
        platform = implementation;

        ChineseweaponsModBlocks.init();
        ChineseweaponsModItems.init();
        ChineseweaponsModTabs.init();
        ChineseWeaponsModEffects.init();
        ChineseWeaponsModEnchantments.init();
        ChineseweaponsModEntities.init();
        ChineseweaponsModBlockEntities.init();
        DeferredRecipe.init();
        ModSounds.init();

        implementation.attach();
    }

    public static void registerPackets() {
        Network network = network();
        network.register(0, SquashPacket.class, PacketDirection.CLIENTBOUND,
                SquashPacket::decode);
        network.register(1, ShieldActionPacket.class, PacketDirection.SERVERBOUND,
                ShieldActionPacket::new);
        network.register(2, ArmorCastingTableBlockProgressPacket.class, PacketDirection.CLIENTBOUND,
                ArmorCastingTableBlockProgressPacket::decode);
        network.register(3, ArmorcastingtableguiSlotMessage.class, PacketDirection.SERVERBOUND,
                ArmorcastingtableguiSlotMessage::new);
    }
}
