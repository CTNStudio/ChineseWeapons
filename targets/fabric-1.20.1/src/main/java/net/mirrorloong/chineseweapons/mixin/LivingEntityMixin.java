package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.compat.StandingShieldExplosionProtection;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric 1.20.1 has no damage event that can change the incoming amount before
 * armour absorption (its {@code ServerLivingEntityEvents.ALLOW_DAMAGE} can only
 * cancel), so the shared armour/weapon damage rules are applied here, mirroring
 * Forge's {@code LivingHurtEvent}.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float chineseweapons$modifyHurtAmount(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (source.is(DamageTypeTags.IS_EXPLOSION)
                && StandingShieldExplosionProtection.isProtectedByCurrentExplosion(self)) {
            return 0.0F;
        }
        float modified = ChineseWeaponsCombatEvents.modifyDamageAmount(self, source, amount);
        return FootmenarmorcompletetestingProcedure.modifyDamageAmount(self, source, modified);
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void chineseweapons$tryEscapeTether(CallbackInfo ci) {
        ChineseWeaponsCombatEvents.tryEscapeTether((LivingEntity) (Object) this);
    }
}
