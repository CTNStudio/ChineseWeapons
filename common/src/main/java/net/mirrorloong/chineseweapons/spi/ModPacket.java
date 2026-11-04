package net.mirrorloong.chineseweapons.spi;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * A loader-neutral packet body. Client-bound handlers receive a null sender.
 */
public interface ModPacket {
    void encode(FriendlyByteBuf buf);

    void handle(ServerPlayer sender);
}
