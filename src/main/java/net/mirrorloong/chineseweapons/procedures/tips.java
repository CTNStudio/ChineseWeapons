package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT)
public class tips {

    private static boolean patchouliTipShown = false;

    private static final String PATCHOULI_URL = "https://www.curseforge.com/minecraft/mc-mods/patchouli";

    @SubscribeEvent
    public static void onEnterGame(PlayerEvent.PlayerLoggedInEvent event) {
        if (patchouliTipShown) {
            return;
        }

        if (!ModList.get().isLoaded("patchouli")) {
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

            event.getEntity().sendSystemMessage(modtip.copy().append(link));
            patchouliTipShown = true;
        }
    }
}