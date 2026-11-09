package net.mirrorloong.chineseweapons.forge;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;

public final class ForgeNetwork implements Network {
    private static final String PROTOCOL = "1";
    private final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ChineseWeapons.MOD_ID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    @Override
    public <T extends ModPacket> void register(int id, Class<T> type, PacketDirection direction,
            Function<FriendlyByteBuf, T> decoder) {
        channel.messageBuilder(type, id)
                .encoder(ModPacket::encode)
                .decoder(decoder::apply)
                .consumer((message, contextSupplier) -> {
                    var context = contextSupplier.get();
                    context.enqueueWork(() -> message.handle(context.getSender()));
                    context.setPacketHandled(true);
                })
                .add();
    }

    @Override
    public void sendToServer(ModPacket packet) {
        channel.sendToServer(packet);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ModPacket packet) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
