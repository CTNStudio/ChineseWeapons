package net.mirrorloong.chineseweapons.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.InputEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import org.lwjgl.glfw.GLFW;

public class ChineseweaponsModKeyHandler {
    public static final KeyMapping ACTION_KEY = new KeyMapping(
            "key.skill.text",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "item_group.chineseweapons.chinese_weapons");

    public static void onKey(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        while (ACTION_KEY.consumeClick()) {
            ChineseWeapons.network().sendToServer(new ShieldActionPacket());
        }
    }
}
