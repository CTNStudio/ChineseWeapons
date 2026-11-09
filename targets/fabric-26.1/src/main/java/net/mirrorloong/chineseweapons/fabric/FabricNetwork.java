package net.mirrorloong.chineseweapons.fabric;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.buffer.Unpooled;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;

/**
 * Fabric transport for {@link ModPacket}. 26.1 Fabric networking only accepts
 * {@link CustomPacketPayload}s with a registered payload type, so every packet
 * body is wrapped in one {@link ModPayload} whose byte array still begins with
 * the shared numeric packet id.
 */
public final class FabricNetwork implements Network {
    public static final CustomPacketPayload.Type<ModPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "main"));

    private static final Map<Integer, Function<FriendlyByteBuf, ? extends ModPacket>> DECODERS = new LinkedHashMap<>();
    private static final Map<Class<?>, Integer> IDS = new LinkedHashMap<>();

    private static boolean serverReceiverRegistered;
    private static boolean payloadTypesRegistered;

    @Override
    public <T extends ModPacket> void register(int id, Class<T> type, PacketDirection direction,
            Function<FriendlyByteBuf, T> decoder) {
        registerPayloadTypes();
        DECODERS.put(id, decoder);
        IDS.put(type, id);
        if (direction == PacketDirection.SERVERBOUND) {
            registerServerReceiver();
        }
    }

    private static synchronized void registerPayloadTypes() {
        if (payloadTypesRegistered) {
            return;
        }
        PayloadTypeRegistry.serverboundPlay().register(TYPE, ModPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TYPE, ModPayload.STREAM_CODEC);
        payloadTypesRegistered = true;
    }

    private static synchronized void registerServerReceiver() {
        if (serverReceiverRegistered) {
            return;
        }
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            ModPacket packet = decode(payload.data());
            if (packet != null) {
                context.server().execute(() -> packet.handle(context.player()));
            }
        });
        serverReceiverRegistered = true;
    }

    public static void dispatchClientbound(byte[] payload) {
        ModPacket packet = decode(payload);
        if (packet != null) {
            packet.handle(null);
        }
    }

    public static byte[] encode(ModPacket packet) {
        Integer id = IDS.get(packet.getClass());
        if (id == null) {
            throw new IllegalArgumentException("Unregistered packet " + packet.getClass().getName());
        }
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeVarInt(id);
        packet.encode(buffer);
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
        ServerPlayNetworking.send(player, new ModPayload(encode(packet)));
    }

    public record ModPayload(byte[] data) implements CustomPacketPayload {
        public static final StreamCodec<RegistryFriendlyByteBuf, ModPayload> STREAM_CODEC =
                StreamCodec.of((buffer, payload) -> buffer.writeByteArray(payload.data),
                        buffer -> new ModPayload(buffer.readByteArray()));

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
