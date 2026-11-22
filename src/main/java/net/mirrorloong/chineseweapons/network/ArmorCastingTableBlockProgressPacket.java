package net.mirrorloong.chineseweapons.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;
import net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache;

public record ArmorCastingTableBlockProgressPacket(BlockPos pos, int progress, boolean hasRecipe) implements ModPacket {

    public static final int ID = 3;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeBlockPos(pos);
        b.writeVarInt(progress);
        b.writeBoolean(hasRecipe);
    }

    public static ArmorCastingTableBlockProgressPacket decode(FriendlyByteBuf b) {
        return new ArmorCastingTableBlockProgressPacket(b.readBlockPos(), b.readVarInt(), b.readBoolean());
    }

        public void handleClient() {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null) {
            net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(pos);
            if (be instanceof ArmorCastingTableBlockEntity table) {
                table.setProgress(progress);
            }
        }
        ArmorCastingTableBlockEntityCache.put(pos, progress);
        ArmorCastingTableBlockEntityCache.putHasRecipe(pos, hasRecipe);
    }

    public static void send(ServerLevel level, BlockPos pos, int progress, boolean hasRecipe) {
        ArmorCastingTableBlockProgressPacket pkt = new ArmorCastingTableBlockProgressPacket(pos, progress, hasRecipe);
        for (ServerPlayer sp : level.players()) {
            if (sp.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
                //old api ChineseweaponsMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), pkt) -> new api NetworkHandler.sendToPlayer
                NetworkHandler.sendToPlayer(sp, pkt);
            }
        }
    }
}
