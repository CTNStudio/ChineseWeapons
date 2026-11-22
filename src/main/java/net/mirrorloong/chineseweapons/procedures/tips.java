package net.mirrorloong.chineseweapons.procedures;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;

public class tips {

    private static boolean registered;

    private static boolean patchouliTipShown = false;

    private static final String PATCHOULI_URL = "https://www.curseforge.com/minecraft/mc-mods/patchouli";

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;

        //old api @SubscribeEvent PlayerEvent.PlayerLoggedInEvent -> new api ServerPlayConnectionEvents.JOIN
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onEnterGame(handler.player));
    }

    public static void onEnterGame(ServerPlayer player) {
        if (patchouliTipShown) {
            return;
        }

        //old api ModList.get().isLoaded("patchouli") -> new api FabricLoader.getInstance().isModLoaded("patchouli")
        if (!FabricLoader.getInstance().isModLoaded("patchouli")) {
            Component cw = Component.translatable("item_group.chineseweapons.chinese_weapons");

            Component modtip = Component.literal("[")
                    .append(cw)
                    .append(Component.literal("] "))
                    .withStyle(ChatFormatting.GREEN);

            Component link = Component.translatable("text.chineseweapons.tips.miss_patchouli")
                    .withStyle(style -> style
                            .withColor(ChatFormatting.GOLD)
                            .withUnderlined(true)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, PATCHOULI_URL))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    Component.literal(PATCHOULI_URL)))
                    );

            player.sendSystemMessage(modtip.copy().append(link));
            patchouliTipShown = true;
        }
    }
}
