package net.mirrorloong.chineseweapons.neoforge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.PacketDirection;

public final class NeoForgeNetwork implements Network {
    private static final String PROTOCOL_VERSION = "1";

    private final Map<Integer, Registration<?>> registrations = new LinkedHashMap<>();
    private final Map<Class<?>, Integer> idsByType = new LinkedHashMap<>();
    private final Map<Integer, CustomPacketPayload.Type<WrappedPayload>> payloadTypes = new LinkedHashMap<>();

    @Override
    public <T extends ModPacket> void register(int id, Class<T> type, PacketDirection direction,
            Function<FriendlyByteBuf, T> decoder) {
        Registration<T> registration = new Registration<>(id, type, direction, decoder);
        registrations.put(id, registration);
        idsByType.put(type, id);
    }

    public void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        for (Registration<?> registration : registrations.values()) {
            registration.attach(registrar);
        }
    }

    @Override
    public void sendToServer(ModPacket packet) {
        PacketDistributor.sendToServer(createPayload(packet));
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ModPacket packet) {
        PacketDistributor.sendToPlayer(player, createPayload(packet));
    }

    private WrappedPayload createPayload(ModPacket packet) {
        Integer id = idsByType.get(packet.getClass());
        if (id == null) {
            throw new IllegalArgumentException("Packet is not registered: " + packet.getClass().getName());
        }
        CustomPacketPayload.Type<WrappedPayload> payloadType = payloadTypes.get(id);
        if (payloadType == null) {
            throw new IllegalStateException("Packet handlers have not been attached yet: " + id);
        }
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        return new WrappedPayload(payloadType, id, data);
    }

    private final class Registration<T extends ModPacket> {
        private static final StreamCodec<RegistryFriendlyByteBuf, WrappedPayload> CODEC = StreamCodec.of(
                (buffer, payload) -> {
                    buffer.writeVarInt(payload.id());
                    buffer.writeByteArray(payload.data());
                },
                buffer -> new WrappedPayload(null, buffer.readVarInt(), buffer.readByteArray()));

        private final int id;
        private final Class<T> type;
        private final PacketDirection direction;
        private final Function<FriendlyByteBuf, T> decoder;

        private Registration(int id, Class<T> type, PacketDirection direction,
                Function<FriendlyByteBuf, T> decoder) {
            this.id = id;
            this.type = type;
            this.direction = direction;
            this.decoder = decoder;
        }

        private void attach(PayloadRegistrar registrar) {
            CustomPacketPayload.Type<WrappedPayload> payloadType = new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "packet_" + id));
            payloadTypes.put(id, payloadType);

            if (direction == PacketDirection.CLIENTBOUND) {
                registrar.playToClient(payloadType, CODEC,
                        (payload, context) -> context.enqueueWork(() -> handle(payload.data(), null)));
            } else {
                registrar.playToServer(payloadType, CODEC,
                        (payload, context) -> context.enqueueWork(() -> handle(payload.data(),
                                context.player() instanceof ServerPlayer player ? player : null)));
            }
        }

        private void handle(byte[] data, ServerPlayer sender) {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
            T packet = decoder.apply(buffer);
            packet.handle(sender);
        }
    }

    private record WrappedPayload(CustomPacketPayload.Type<WrappedPayload> type, int id, byte[] data)
            implements CustomPacketPayload {
    }
}
