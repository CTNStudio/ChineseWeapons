package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

import java.util.function.Supplier;

public class BedCrossbowWindPacket {

    private final boolean winding;

    public BedCrossbowWindPacket(boolean winding) {
        this.winding = winding;
    }

    public BedCrossbowWindPacket(FriendlyByteBuf buffer) {
        this.winding = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.winding);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }
            BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
            if (crossbow != null) {
                crossbow.setWinding(player, this.winding);
            }
        });
        context.get().setPacketHandled(true);
    }
}
