package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.mirrorloong.chineseweapons.item.ReachSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forge exposes {@code ForgeMod.ENTITY_REACH}; 1.20.1 vanilla hard-codes the
 * client pick range. Extend it while a reach weapon is held so the crosshair
 * can target entities at the weapon's designed distance.
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

    @Inject(method = "getPickRange", at = @At("RETURN"), cancellable = true)
    private void chineseweapons$extendPickRange(CallbackInfoReturnable<Float> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.getMainHandItem().getItem() instanceof ReachSwordItem reach) {
            double bonus = reach.entityReachBonus();
            if (bonus > 0.0D) {
                cir.setReturnValue((float) (cir.getReturnValueF() + bonus));
            }
        }
    }
}
