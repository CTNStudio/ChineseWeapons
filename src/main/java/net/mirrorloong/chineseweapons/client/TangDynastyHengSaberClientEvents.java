package net.mirrorloong.chineseweapons.client;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.procedures.TangDynastyHengSabeCutsProcedures;

import java.util.UUID;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TangDynastyHengSaberClientEvents {
    private static final float ITEM_OFFSET = 3.0F / 16.0F;
    private static final float ROTATION = -60.0F;

    private TangDynastyHengSaberClientEvents() {
    }

    @SubscribeEvent
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

        event.getPoseStack().mulPose(Axis.XP.rotationDegrees(ROTATION * progress));
        event.getPoseStack().translate(0, 0, ITEM_OFFSET * progress);
    }
}
