package net.mirrorloong.chineseweapons.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class HandRenderEvents {

    public interface Handler {
        boolean render(PoseStack poseStack, Player player, InteractionHand hand, ItemStack stack, float partialTick);
    }

    public interface FullHandler {
        boolean render(PoseStack poseStack, Player player, InteractionHand hand, ItemStack stack, float partialTick,
                       MultiBufferSource buffer, int packedLight);
    }

    private static final List<Handler> HANDLERS = new ArrayList<>();
    private static final List<FullHandler> FULL_HANDLERS = new ArrayList<>();

    private HandRenderEvents() {
    }

    public static void register(Handler handler) {
        HANDLERS.add(handler);
    }

    public static void registerFull(FullHandler handler) {
        FULL_HANDLERS.add(handler);
    }

    public static boolean invokeAll(PoseStack poseStack, Player player, InteractionHand hand, ItemStack stack,
                                    float partialTick, MultiBufferSource buffer, int packedLight) {
        boolean cancel = false;
        for (int i = 0; i < HANDLERS.size(); i++) {
            cancel |= HANDLERS.get(i).render(poseStack, player, hand, stack, partialTick);
        }
        for (int i = 0; i < FULL_HANDLERS.size(); i++) {
            cancel |= FULL_HANDLERS.get(i).render(poseStack, player, hand, stack, partialTick, buffer, packedLight);
        }
        return cancel;
    }
}
