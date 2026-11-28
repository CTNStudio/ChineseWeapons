package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.mirrorloong.chineseweapons.client.BedCrossbowClientEvents;

public class BedCrossbowShakePacket implements ModPacket {

    public static final int ID = 6;

    public BedCrossbowShakePacket() {
    }

    public BedCrossbowShakePacket(FriendlyByteBuf buffer) {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
    }

    public static BedCrossbowShakePacket decode(FriendlyByteBuf buffer) {
        return new BedCrossbowShakePacket(buffer);
    }

    public void handleClient() {
        BedCrossbowClientEvents.triggerCrossbowShake();
    }
}
