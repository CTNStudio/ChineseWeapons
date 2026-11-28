package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

import java.util.function.Supplier;

public class BedCrossbowReleasePacket {

    public BedCrossbowReleasePacket() {
    }

    public BedCrossbowReleasePacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }
            BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
            if (crossbow != null) {
                crossbow.release(true);
            }
        });
        context.get().setPacketHandled(true);
    }
}
