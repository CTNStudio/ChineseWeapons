package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BedCrossbowWindPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BedCrossbowWindPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "bed_crossbow_wind"));

    public static final StreamCodec<FriendlyByteBuf, BedCrossbowWindPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, packet) -> buffer.writeBoolean(packet.winding),
                    buffer -> new BedCrossbowWindPacket(buffer.readBoolean())
            );

    private final boolean winding;

    public BedCrossbowWindPacket(boolean winding) {
        this.winding = winding;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BedCrossbowWindPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
            if (crossbow != null) {
                crossbow.setWinding(player, packet.winding);
            }
        });
    }
}
