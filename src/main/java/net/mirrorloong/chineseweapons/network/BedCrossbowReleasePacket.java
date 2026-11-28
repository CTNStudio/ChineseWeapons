package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

public class BedCrossbowReleasePacket implements ModPacket {

    public static final int ID = 4;

    public BedCrossbowReleasePacket() {
    }

    public BedCrossbowReleasePacket(FriendlyByteBuf buffer) {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
    }

    public static BedCrossbowReleasePacket decode(FriendlyByteBuf buffer) {
        return new BedCrossbowReleasePacket(buffer);
    }

    public void handle(ServerPlayer player) {
        if (player == null) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow != null) {
            crossbow.release(true);
        }
    }
}
