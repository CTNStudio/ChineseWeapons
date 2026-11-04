package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.rules.GlaiveRules;

/** Fabric 1.20.1 adapter for the glaive spin (wiring lives in FabricEvents). */
public final class GlaiveItemGetLostKillProcedure {

    private GlaiveItemGetLostKillProcedure() {
    }

    public static boolean tryStart(Level world, Player player, InteractionHand hand, ItemStack itemStack) {
        GlaiveRules.StartResult result = GlaiveRules.tryStart(
                world, player, hand == InteractionHand.MAIN_HAND, player.isShiftKeyDown(), itemStack);
        return result != GlaiveRules.StartResult.REJECTED;
    }

    public static void playerTick(Player player) {
        GlaiveRules.tick(player);
    }

    public static void onPlayerLogout(Player player) {
        GlaiveRules.onLogout(player.getUUID());
    }
}
