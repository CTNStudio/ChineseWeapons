package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class tips {

    private static boolean patchouliTipShown = false;

    private static final String PATCHOULI_URL = "https://www.curseforge.com/minecraft/mc-mods/patchouli";

    @SubscribeEvent
    public static void onEnterGame(PlayerEvent.PlayerLoggedInEvent event) {
        if (patchouliTipShown) {
            return;
        }

        if (!ModList.get().isLoaded("patchouli")) {
            Component cw = new TranslatableComponent("item_group.chineseweapons.chinese_weapons");

            Component modtip = new TextComponent("[")
                    .append(cw)
                    .append(new TextComponent("] "))
                    .withStyle(ChatFormatting.GREEN);

            Component link = new TranslatableComponent("text.chineseweapons.tips.miss_patchouli")
                    .withStyle(style -> style
                            .withColor(ChatFormatting.GOLD)
                            .withUnderlined(true)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, PATCHOULI_URL))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new TextComponent(PATCHOULI_URL)))
                    );

            event.getEntity().sendMessage(modtip.copy().append(link), Util.NIL_UUID);
            patchouliTipShown = true;
        }
    }
}