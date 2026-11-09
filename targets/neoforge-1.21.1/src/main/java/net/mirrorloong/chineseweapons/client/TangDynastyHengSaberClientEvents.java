package net.mirrorloong.chineseweapons.client;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.mirrorloong.chineseweapons.procedures.weapon.TangDynastyHengSabeCutsProcedures;

import java.util.UUID;

public final class TangDynastyHengSaberClientEvents {
    private static final float ITEM_OFFSET = 1F;
    private static final float ROTATION = -60.0F;

    private TangDynastyHengSaberClientEvents() {
    }

    public static void modifyItemRender(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        UUID uuid = player.getUUID();
        float progress = TangDynastyHengSabeCutsProcedures.getAnimationProgress(uuid);
        if (progress <= 0.0F) {
            return;
        }
        event.getPoseStack().translate(0, 0, -ITEM_OFFSET * progress);
        event.getPoseStack().mulPose(Axis.XP.rotationDegrees(ROTATION * progress));
    }
}
