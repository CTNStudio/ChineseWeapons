package net.mirrorloong.chineseweapons.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT)
public class ChineseweaponsModKeyHandler {
    public static final KeyMapping ACTION_KEY = new KeyMapping(
            "key.standing_shield.onground",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "item_group.chineseweapons.chinese_weapons");

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        while (ACTION_KEY.consumeClick()) {
            ChineseweaponsMod.CHANNEL.sendToServer(new ShieldActionPacket());
        }
    }
}