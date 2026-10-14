package net.mirrorloong.chineseweapons.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ArmorCastingTableBlockProgressPacket(BlockPos pos, int progress, boolean hasRecipe)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArmorCastingTableBlockProgressPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "armor_casting_table_progress"));

    public static final StreamCodec<FriendlyByteBuf, ArmorCastingTableBlockProgressPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {
                        buf.writeBlockPos(packet.pos());
                        buf.writeVarInt(packet.progress());
                        buf.writeBoolean(packet.hasRecipe());
                    },
                    buf -> new ArmorCastingTableBlockProgressPacket(
                            buf.readBlockPos(), buf.readVarInt(), buf.readBoolean())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ArmorCastingTableBlockProgressPacket m, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().getReceptionSide().isClient()) {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.level != null) {
                    net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(m.pos());
                    if (be instanceof net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity table) {
                        table.setProgress(m.progress());
                    }
                }
                net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.put(m.pos(), m.progress());
                net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.putHasRecipe(m.pos(), m.hasRecipe());
            }
        });
    }

    public static void send(ServerLevel level, BlockPos pos, int progress, boolean hasRecipe) {
        ArmorCastingTableBlockProgressPacket pkt =
                new ArmorCastingTableBlockProgressPacket(pos, progress, hasRecipe);
        for (ServerPlayer sp : level.players()) {
            if (sp.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
                PacketDistributor.sendToPlayer(sp, pkt);
            }
        }
    }
}