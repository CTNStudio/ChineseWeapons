package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.item.TangDynastyHorseItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric has no Forge {@code ProjectileImpactEvent}; arrows hitting a horse
 * wearing Tang dynasty armour are bounced here instead.
 */
@Mixin(AbstractArrow.class)
public abstract class ArrowMixin {

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void chineseweapons$deflectHorseArmor(EntityHitResult result, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        if (!(result.getEntity() instanceof Horse horse)) {
            return;
        }

        ItemStack armor = horse.getArmor();
        if (!(armor.getItem() instanceof TangDynastyHorseItem)) {
            return;
        }

        Vec3 velocity = self.getDeltaMovement();
        RandomSource random = self.level().getRandom();
        self.setDeltaMovement(new Vec3(
                -velocity.x * 0.8D + (random.nextDouble() - 0.5D) * 0.3D,
                -velocity.y * 0.8D + 0.1D,
                -velocity.z * 0.8D + (random.nextDouble() - 0.5D) * 0.3D));
        self.setOwner((Entity) null);
        self.pickup = AbstractArrow.Pickup.DISALLOWED;
        ci.cancel();
    }
}
