package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

public class BedCrossbowWindPacket implements ModPacket {

    public static final int ID = 5;

    private final boolean winding;

    public BedCrossbowWindPacket(boolean winding) {
        this.winding = winding;
    }

    public BedCrossbowWindPacket(FriendlyByteBuf buffer) {
        this.winding = buffer.readBoolean();
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.winding);
    }

    public static BedCrossbowWindPacket decode(FriendlyByteBuf buffer) {
        return new BedCrossbowWindPacket(buffer);
    }

    public void handle(ServerPlayer player) {
        if (player == null) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow != null) {
            crossbow.setWinding(player, this.winding);
        }
    }
}
