package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.item.FabricArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.1 checks {@code isWearingSafeArmor}; add our golden sets, which use a
 * custom armour material instead of {@code ArmorMaterials.GOLD}.
 */
@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

    @Inject(method = "isWearingSafeArmor", at = @At("HEAD"), cancellable = true)
    private static void chineseweapons$goldArmorNeutral(LivingEntity entity,
                                                        CallbackInfoReturnable<Boolean> cir) {
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.getItem() instanceof FabricArmorItem armor
                    && armor.definition().piglinNeutral()) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
