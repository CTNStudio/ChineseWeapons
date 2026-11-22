package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ModPacket {

    int id();

    void encode(FriendlyByteBuf buf);
}
