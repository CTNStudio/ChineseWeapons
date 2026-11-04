package net.mirrorloong.chineseweapons.spi;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Loader-neutral packet transport.
 */
public interface Network {
    <T extends ModPacket> void register(int id, Class<T> type, PacketDirection direction,
            Function<FriendlyByteBuf, T> decoder);

    void sendToServer(ModPacket packet);

    void sendToPlayer(ServerPlayer player, ModPacket packet);
}
