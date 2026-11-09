package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.player.Player;
import net.mirrorloong.chineseweapons.item.ReachSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.1 vanilla exposes {@code Player#entityInteractionRange}; add the held
 * weapon's reach bonus here so client attack rays and server validation agree
 * with the Forge {@code ENTITY_REACH} behaviour.
 */
@Mixin(Player.class)
public abstract class PlayerReachMixin {

    @Inject(method = "entityInteractionRange", at = @At("RETURN"), cancellable = true)
    private void chineseweapons$extendEntityReach(CallbackInfoReturnable<Double> cir) {
        Player self = (Player) (Object) this;
        if (self.getMainHandItem().getItem() instanceof ReachSwordItem reach) {
            double bonus = reach.entityReachBonus();
            if (bonus > 0.0D) {
                cir.setReturnValue(cir.getReturnValueD() + bonus);
            }
        }
    }
}
