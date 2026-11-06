package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The wooden dagger axe returns itself when used as a crafting ingredient.
 * 1.20.1's {@code getCraftingRemainingItem} is final and needs the item before
 * it exists, so the self-reference is provided here.
 */
@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "hasCraftingRemainingItem", at = @At("HEAD"), cancellable = true)
    private void chineseweapons$woodRemainder(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == ChineseweaponsModItems.WOODEN_DAGGER_AXE.get()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getCraftingRemainingItem", at = @At("HEAD"), cancellable = true)
    private void chineseweapons$woodRemainderItem(CallbackInfoReturnable<Item> cir) {
        if ((Object) this == ChineseweaponsModItems.WOODEN_DAGGER_AXE.get()) {
            cir.setReturnValue((Item) (Object) this);
        }
    }
}
