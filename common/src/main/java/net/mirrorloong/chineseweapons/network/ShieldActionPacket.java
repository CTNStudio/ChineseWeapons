package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.rules.DaggerAxeRules;
import net.mirrorloong.chineseweapons.rules.ShieldRules;
import net.mirrorloong.chineseweapons.rules.WeaponRules;
import net.mirrorloong.chineseweapons.spi.ModPacket;
import net.mirrorloong.chineseweapons.spi.StandingShieldEntity;
import net.mirrorloong.chineseweapons.spi.StandingShieldItem;

/**
 * Server-bound "use the special attack" request from the shield key. The
 * payload is empty; the action depends on what the player is looking at and
 * holding.
 */
public final class ShieldActionPacket implements ModPacket {

    public ShieldActionPacket() {
    }

    public ShieldActionPacket(FriendlyByteBuf buf) {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public void handle(ServerPlayer player) {
        if (player == null) {
            return;
        }

        StandingShieldEntity target = ShieldRules.findStandingShield(player);
        if (target != null) {
            ShieldRules.retrieveShield(player, target);
            return;
        }

        ItemStack main = player.getMainHandItem();

        if (DaggerAxeRules.isDaggerAxe(main)) {
            DaggerAxeRules.hook(player, main);
            return;
        }

        if (WeaponRules.isGlaive(main)) {
            LivingEntity livingTarget = ShieldRules.findLivingTarget(player, 3.0D);
            if (livingTarget != null) {
                WeaponRules.stopRun(livingTarget, player, main);
            }
            return;
        }

        if (main.getItem() instanceof StandingShieldItem) {
            ShieldRules.placeShield(player, main);
        }
    }
}
