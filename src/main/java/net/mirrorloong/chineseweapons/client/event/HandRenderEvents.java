package net.mirrorloong.chineseweapons.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class HandRenderEvents {

    public interface Handler {
        boolean render(PoseStack poseStack, Player player, InteractionHand hand, ItemStack stack, float partialTick);
    }

    private static final List<Handler> HANDLERS = new ArrayList<>();

    private HandRenderEvents() {
    }

    public static void register(Handler handler) {
        HANDLERS.add(handler);
    }

    public static boolean invokeAll(PoseStack poseStack, Player player, InteractionHand hand, ItemStack stack, float partialTick) {
        boolean cancel = false;
        for (int i = 0; i < HANDLERS.size(); i++) {
            cancel |= HANDLERS.get(i).render(poseStack, player, hand, stack, partialTick);
        }
        return cancel;
    }
}
