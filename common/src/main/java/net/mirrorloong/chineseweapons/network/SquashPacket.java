package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.rules.GuDuoRules;
import net.mirrorloong.chineseweapons.spi.ModPacket;

/** Client-bound squash animation trigger; payload is the entity id as a varint. */
public record SquashPacket(int id) implements ModPacket {

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(id);
    }

    public static SquashPacket decode(FriendlyByteBuf buf) {
        return new SquashPacket(buf.readVarInt());
    }

    @Override
    public void handle(ServerPlayer sender) {
        GuDuoRules.SQUASH.put(id, System.currentTimeMillis());
    }
}
