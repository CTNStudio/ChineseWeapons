package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.item.FabricArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forge consults {@code IForgeItem#makesPiglinsNeutral}; vanilla 1.20.1 only
 * checks {@code ArmorMaterials.GOLD}. Extend the check to our golden sets.
 */
@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

    @Inject(method = "isWearingGold", at = @At("HEAD"), cancellable = true)
    private static void chineseweapons$goldArmorNeutral(LivingEntity entity,
                                                        CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack stack : entity.getArmorSlots()) {
            if (stack.getItem() instanceof FabricArmorItem armor
                    && armor.makesPiglinsNeutral(stack, entity)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
