package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.1 keeps the crafting remainder as an {@code ItemStackTemplate}; the wooden
 * dagger axe returns itself.
 */
@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getCraftingRemainder", at = @At("HEAD"), cancellable = true)
    private void chineseweapons$woodRemainder(CallbackInfoReturnable<ItemStackTemplate> cir) {
        if ((Object) this == ChineseweaponsModItems.WOODEN_DAGGER_AXE.get()) {
            cir.setReturnValue(new ItemStackTemplate((Item) (Object) this));
        }
    }
}
