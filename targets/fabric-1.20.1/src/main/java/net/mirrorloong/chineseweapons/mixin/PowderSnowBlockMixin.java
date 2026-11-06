package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.mirrorloong.chineseweapons.item.FabricArmorItem;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forge consults {@code IForgeItem#canWalkOnPowderedSnow}; vanilla only knows
 * leather boots. Honour the definition's {@code snowWalk} flag on the boots.
 */
@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void chineseweapons$snowWalkBoots(Entity entity,
                                                     CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        ItemStack feet = living.getItemBySlot(EquipmentSlot.FEET);
        if (feet.getItem() instanceof FabricArmorItem armor
                && armor.canWalkOnPowderedSnow(feet, living)) {
            cir.setReturnValue(true);
        }
    }
}
