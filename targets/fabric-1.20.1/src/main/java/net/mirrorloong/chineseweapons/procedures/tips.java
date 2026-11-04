package net.mirrorloong.chineseweapons.procedures;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;

/**
 * Fabric replacement for the Forge {@code @EventBusSubscriber} login tip:
 * {@code FabricEvents} calls {@link #onEnterGame(ServerPlayer)} from
 * {@code ServerPlayConnectionEvents.JOIN} and mod presence is queried through
 * the loader instead of {@code ModList}.
 */
public class tips {

    private static boolean patchouliTipShown = false;

    private static final String PATCHOULI_URL = "https://www.curseforge.com/minecraft/mc-mods/patchouli";

    public static void onEnterGame(ServerPlayer player) {
        if (patchouliTipShown) {
            return;
        }

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
