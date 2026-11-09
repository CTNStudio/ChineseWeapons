package net.mirrorloong.chineseweapons.fabric;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.buffer.Unpooled;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;

/**
 * Fabric transport for {@link ModPacket}. Fabric channels do not carry the
 * numeric id Forge's {@code SimpleChannel} adds, so every payload is prefixed
 * with the shared packet id and dispatched from a single receiver per side.
 */
public final class FabricNetwork implements Network {
    private static final ResourceLocation CHANNEL = new ResourceLocation(ChineseWeapons.MOD_ID, "main");
    private static final Map<Integer, Function<FriendlyByteBuf, ? extends ModPacket>> DECODERS = new LinkedHashMap<>();
    private static final Map<Class<?>, Integer> IDS = new LinkedHashMap<>();

    private static boolean serverReceiverRegistered;

    public static ResourceLocation channel() {
        return CHANNEL;
    }

    @Override
    public <T extends ModPacket> void register(int id, Class<T> type, PacketDirection direction,
            Function<FriendlyByteBuf, T> decoder) {
        DECODERS.put(id, decoder);
        IDS.put(type, id);
        if (direction == PacketDirection.SERVERBOUND) {
            registerServerReceiver();
        }
    }

    private static synchronized void registerServerReceiver() {
        if (serverReceiverRegistered) {
            return;
        }
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL, (server, player, handler, buffer, responseSender) -> {
            byte[] payload = readPayload(buffer);
            server.execute(() -> {
                ModPacket packet = decode(payload);
                if (packet != null) {
                    packet.handle(player);
                }
            });
        });
        serverReceiverRegistered = true;
    }

    public static void dispatchClientbound(byte[] payload) {
        ModPacket packet = decode(payload);
        if (packet != null) {
            packet.handle(null);
        }
    }

    public static FriendlyByteBuf encode(ModPacket packet) {
        Integer id = IDS.get(packet.getClass());
        if (id == null) {
            throw new IllegalArgumentException("Unregistered packet " + packet.getClass().getName());
        }
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeVarInt(id);
        packet.encode(buffer);
        return buffer;
    }

    public static byte[] readPayload(FriendlyByteBuf buffer) {
        byte[] payload = new byte[buffer.readableBytes()];
        buffer.readBytes(payload);
        return payload;
    }

    private static ModPacket decode(byte[] payload) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload));
        try {
            int id = buffer.readVarInt();
            Function<FriendlyByteBuf, ? extends ModPacket> decoder = DECODERS.get(id);
            if (decoder == null) {
                ChineseWeapons.LOGGER.warn("Ignoring unknown packet id {}", id);
                return null;
            }
            return decoder.apply(buffer);
        } catch (RuntimeException exception) {
            ChineseWeapons.LOGGER.error("Failed to decode a Chinese Weapons packet", exception);
            return null;
        }
    }

    @Override
    public void sendToServer(ModPacket packet) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            throw new IllegalStateException("Client-bound network transport used on a dedicated server");
        }
        FabricClientNetwork.sendToServer(packet);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ModPacket packet) {
        ServerPlayNetworking.send(player, CHANNEL, encode(packet));
    }
}
