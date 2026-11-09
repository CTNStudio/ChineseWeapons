package net.mirrorloong.chineseweapons.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;

public record ArmorCastingTableBlockProgressPacket(BlockPos pos, int progress, boolean hasRecipe)
        implements ModPacket {

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeBlockPos(pos);
        b.writeVarInt(progress);
        b.writeBoolean(hasRecipe);
    }

    public static ArmorCastingTableBlockProgressPacket decode(FriendlyByteBuf b) {
        return new ArmorCastingTableBlockProgressPacket(b.readBlockPos(), b.readVarInt(), b.readBoolean());
    }

    @Override
    public void handle(ServerPlayer sender) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null) {
            net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(pos);
            if (be instanceof net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity table) {
                table.setProgress(progress);
            }
        }
        net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.put(pos, progress);
        net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache.putHasRecipe(pos, hasRecipe);
    }

    public static void send(ServerLevel level, BlockPos pos, int progress, boolean hasRecipe) {
        ArmorCastingTableBlockProgressPacket pkt = new ArmorCastingTableBlockProgressPacket(pos, progress, hasRecipe);
        for (ServerPlayer sp : level.players()) {
            if (sp.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
                ChineseWeapons.network().sendToPlayer(sp, pkt);
            }
        }
    }
}