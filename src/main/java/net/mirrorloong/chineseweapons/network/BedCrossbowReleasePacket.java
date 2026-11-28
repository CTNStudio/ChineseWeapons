package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BedCrossbowReleasePacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BedCrossbowReleasePacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "bed_crossbow_release"));

    public static final StreamCodec<FriendlyByteBuf, BedCrossbowReleasePacket> STREAM_CODEC =
            StreamCodec.unit(new BedCrossbowReleasePacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BedCrossbowReleasePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
            if (crossbow != null) {
                crossbow.release(true);
            }
        });
    }
}
