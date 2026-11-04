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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;

/**
 * Fabric transport for {@link ModPacket}. Fabric's 1.20.5+ networking API
 * carries typed payloads instead of raw channel buffers, so the shared packet
 * id is written inside a single payload type and dispatched from one receiver
 * per side.
 */
public final class FabricNetwork implements Network {
    public static final CustomPacketPayload.Type<Payload> CHANNEL =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "main"));
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC =
            StreamCodec.ofMember(Payload::write, Payload::decode);

    private static final Map<Integer, Function<FriendlyByteBuf, ? extends ModPacket>> DECODERS = new LinkedHashMap<>();
    private static final Map<Class<?>, Integer> IDS = new LinkedHashMap<>();

    private static boolean typesRegistered;
    private static boolean serverReceiverRegistered;

    public static void registerPayloadTypes() {
        if (typesRegistered) {
            return;
        }
        PayloadTypeRegistry.playC2S().register(CHANNEL, CODEC);
        PayloadTypeRegistry.playS2C().register(CHANNEL, CODEC);
        typesRegistered = true;
    }

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

    private static synchronized void registerServerReceiver() {
        if (serverReceiverRegistered) {
            return;
        }
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                ModPacket packet = decode(payload.data());
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

    public static Payload encodePayload(ModPacket packet) {
        return new Payload(encode(packet));
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
        ServerPlayNetworking.send(player, encodePayload(packet));
    }

    public record Payload(byte[] data) implements CustomPacketPayload {
        private static Payload decode(RegistryFriendlyByteBuf buffer) {
            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);
            return new Payload(data);
        }

        private void write(RegistryFriendlyByteBuf buffer) {
            buffer.writeBytes(data);
        }

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return CHANNEL;
        }
    }
}
