package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.mirrorloong.chineseweapons.item.ReachSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Vanilla caps attacks at three blocks in {@code GameRenderer#pick}
 * ({@code distSqr > 9.0}). Scale the cap by the held weapon's reach bonus so
 * the client-side attack ray accepts the designed distance.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @ModifyConstant(method = "pick", constant = @Constant(doubleValue = 9.0D))
    private double chineseweapons$extendAttackRange(double original) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return original;
        }
        if (player.getMainHandItem().getItem() instanceof ReachSwordItem reach) {
            double bonus = reach.entityReachBonus();
            if (bonus > 0.0D) {
                double range = 3.0D + bonus;
                return range * range;
            }
        }
        return original;
    }
}
