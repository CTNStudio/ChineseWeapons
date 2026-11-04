package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;
import net.mirrorloong.chineseweapons.rules.GlaiveRules;

/** NeoForge 1.21.1 event wiring for the glaive spin. */
@EventBusSubscriber(modid = ChineseWeapons.MOD_ID)
public final class GlaiveItemGetLostKillProcedure {

    private GlaiveItemGetLostKillProcedure() {
    }

    public static boolean tryStart(Level world, Player player, InteractionHand hand, ItemStack itemStack) {
        GlaiveRules.StartResult result = GlaiveRules.tryStart(
                world, player, hand == InteractionHand.MAIN_HAND, player.isShiftKeyDown(), itemStack);

        if (result == GlaiveRules.StartResult.CLIENT) {
            NeoForge.EVENT_BUS.post(new GlaiveSpinStartedEvent(player, itemStack.copy()));
            return true;
        }
        return result == GlaiveRules.StartResult.SERVER;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        GlaiveRules.tick(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        GlaiveRules.onLogout(event.getEntity().getUUID());
    }
}
