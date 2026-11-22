package net.mirrorloong.chineseweapons.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.item.GuDuoItem;

//old api net.minecraftforge.network.NetworkRegistry / SimpleChannel / PacketDistributor -> new api Fabric Networking API v1
public final class NetworkHandler {

    public static final ResourceLocation CHANNEL = new ResourceLocation(ChineseweaponsMod.MODID, "main");

    private static final int ID_GUDUO_SQUASH = 0;
    private static final int ID_SHIELD_ACTION = 1;
    private static final int ID_CASTING_TABLE_SLOT = 2;
    private static final int ID_CASTING_TABLE_PROGRESS = 3;

    private NetworkHandler() {
    }

            public static void initCommon() {
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL, (server, player, handler, buf, responseSender) -> {
            int packetId = buf.readVarInt();
                        FriendlyByteBuf payload = new FriendlyByteBuf(buf.copy());
            server.execute(() -> {
                switch (packetId) {
                    case ID_GUDUO_SQUASH -> GuDuoItem.SquashPacket.decode(payload).handleClientOnly();
                    case ID_SHIELD_ACTION -> new ShieldActionPacket(payload).handle(player);
                    case ID_CASTING_TABLE_SLOT -> new ArmorcastingtableguiSlotMessage(payload).handle(player);
                    default -> ChineseweaponsMod.LOGGER.warn("Unknown packet id {} from {}", packetId, player.getName().getString());
                }
            });
        });
    }

            public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL, (client, handler, buf, responseSender) -> {
            int packetId = buf.readVarInt();
            FriendlyByteBuf payload = new FriendlyByteBuf(buf.copy());
            client.execute(() -> {
                if (packetId == ID_CASTING_TABLE_PROGRESS) {
                    ArmorCastingTableBlockProgressPacket.decode(payload).handleClient();
                } else if (packetId == ID_GUDUO_SQUASH) {
                    GuDuoItem.SquashPacket.decode(payload).handleClient();
                }
            });
        });
    }

    public static void sendToServer(ModPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(packet.id());
        packet.encode(buf);
        ClientPlayNetworking.send(CHANNEL, buf);
    }

    public static void sendToPlayer(ServerPlayer player, ModPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(packet.id());
        packet.encode(buf);
        ServerPlayNetworking.send(player, CHANNEL, buf);
    }
}
