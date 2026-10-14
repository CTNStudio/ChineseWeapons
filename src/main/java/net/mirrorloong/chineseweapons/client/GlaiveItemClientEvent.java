package net.mirrorloong.chineseweapons.client;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;

import java.util.UUID;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT)
public final class GlaiveItemClientEvent {
        private static final float ITEM_OFFSET = 3.0F / 16.0F;
        private static final float ROTATION = -30.0F;

        private GlaiveItemClientEvent() {
        }

        @SubscribeEvent
        public static void modifyItemRender(RenderHandEvent event) {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }

            UUID uuid = player.getUUID();
            float progress = GlaiveItemStopRunProcedure.getAnimationProgress(uuid);
            if (progress <= 0.0F) {
                return;
            }

            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(ROTATION * progress));
            event.getPoseStack().translate(0, 0, ITEM_OFFSET * progress);
        }
}