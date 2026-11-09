package net.mirrorloong.chineseweapons.client;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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

            event.getPoseStack().mulPose(Vector3f.XP.rotationDegrees(ROTATION * progress));
            event.getPoseStack().translate(0, 0, ITEM_OFFSET * progress);
        }
}