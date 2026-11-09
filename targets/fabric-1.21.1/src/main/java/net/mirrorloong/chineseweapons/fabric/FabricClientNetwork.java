package net.mirrorloong.chineseweapons.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mirrorloong.chineseweapons.spi.ModPacket;

/**
 * Client-only half of {@link FabricNetwork}. Referenced exclusively from the
 * client entry initializer and client-bound packet sends.
 */
public final class FabricClientNetwork implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.CHANNEL, (payload, context) ->
                context.client().execute(() -> FabricNetwork.dispatchClientbound(payload.data())));
    }

    public static void sendToServer(ModPacket packet) {
        ClientPlayNetworking.send(FabricNetwork.encodePayload(packet));
    }
}
