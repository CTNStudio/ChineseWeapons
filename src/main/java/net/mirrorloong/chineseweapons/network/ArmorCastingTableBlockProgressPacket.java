package net.mirrorloong.chineseweapons.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.function.Supplier;

public record ArmorCastingTableBlockProgressPacket(BlockPos pos, int progress, boolean hasRecipe) {

    public static void encode(ArmorCastingTableBlockProgressPacket m, FriendlyByteBuf b) {
        b.writeBlockPos(m.pos);
        b.writeVarInt(m.progress);
        b.writeBoolean(m.hasRecipe);
    }

    public static ArmorCastingTableBlockProgressPacket decode(FriendlyByteBuf b) {
        return new ArmorCastingTableBlockProgressPacket(b.readBlockPos(), b.readVarInt(), b.readBoolean());
    }

    public static void handle(ArmorCastingTableBlockProgressPacket m, Supplier<NetworkEvent.Context> c) {
        NetworkEvent.Context ctx = c.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.level != null) {
                    net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(m.pos);
                    if (be instanceof net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity table) {
                        table.setProgress(m.progress);
                    }
                }
                net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.put(m.pos, m.progress);
                net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.putHasRecipe(m.pos, m.hasRecipe);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void send(ServerLevel level, BlockPos pos, int progress, boolean hasRecipe) {
        ArmorCastingTableBlockProgressPacket pkt = new ArmorCastingTableBlockProgressPacket(pos, progress, hasRecipe);
        for (ServerPlayer sp : level.players()) {
            if (sp.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
                ChineseweaponsMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), pkt);
            }
        }
    }
}