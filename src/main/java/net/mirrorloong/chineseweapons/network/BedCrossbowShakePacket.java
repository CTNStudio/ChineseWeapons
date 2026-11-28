package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.BedCrossbowClientEvents;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BedCrossbowShakePacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BedCrossbowShakePacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "bed_crossbow_shake"));

    public static final StreamCodec<FriendlyByteBuf, BedCrossbowShakePacket> STREAM_CODEC =
            StreamCodec.unit(new BedCrossbowShakePacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BedCrossbowShakePacket packet, IPayloadContext context) {
        context.enqueueWork(BedCrossbowClientEvents::triggerCrossbowShake);
    }
}
