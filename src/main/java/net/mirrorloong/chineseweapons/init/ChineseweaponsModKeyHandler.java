package net.mirrorloong.chineseweapons.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.mirrorloong.chineseweapons.network.NetworkHandler;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import org.lwjgl.glfw.GLFW;

public class ChineseweaponsModKeyHandler {

    public static final KeyMapping ACTION_KEY = new KeyMapping(
            "key.skill.text",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "item_group.chineseweapons.chinese_weapons");

    //old api RegisterKeyMappingsEvent -> new api KeyBindingHelper.registerKeyBinding
    public static void registerKeyBinding() {
        KeyBindingHelper.registerKeyBinding(ACTION_KEY);
    }

        public static void onKey(Minecraft client) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        while (ACTION_KEY.consumeClick()) {
            //old api ChineseweaponsMod.CHANNEL.sendToServer -> new api NetworkHandler.sendToServer
            NetworkHandler.sendToServer(new ShieldActionPacket());
        }
    }
}
