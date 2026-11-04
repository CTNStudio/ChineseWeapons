package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;
import net.mirrorloong.chineseweapons.rules.GlaiveRules;

/** Forge 1.20.1 event wiring for the glaive spin. */
@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GlaiveItemGetLostKillProcedure {

    private GlaiveItemGetLostKillProcedure() {
    }

    public static boolean tryStart(Level world, Player player, InteractionHand hand, ItemStack itemStack) {
        GlaiveRules.StartResult result = GlaiveRules.tryStart(
                world, player, hand == InteractionHand.MAIN_HAND, player.isShiftKeyDown(), itemStack);

        if (result == GlaiveRules.StartResult.CLIENT) {
            MinecraftForge.EVENT_BUS.post(new GlaiveSpinStartedEvent(player, itemStack.copy()));
            return true;
        }
        return result == GlaiveRules.StartResult.SERVER;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        GlaiveRules.tick(event.player);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        GlaiveRules.onLogout(event.getEntity().getUUID());
    }
}
