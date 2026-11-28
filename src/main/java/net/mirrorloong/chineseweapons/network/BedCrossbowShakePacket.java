package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.mirrorloong.chineseweapons.client.BedCrossbowClientEvents;

import java.util.function.Supplier;

public class BedCrossbowShakePacket {

    public BedCrossbowShakePacket() {
    }

    public BedCrossbowShakePacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().isClient()) {
                BedCrossbowClientEvents.triggerCrossbowShake();
            }
        });
        context.get().setPacketHandled(true);
    }
}
