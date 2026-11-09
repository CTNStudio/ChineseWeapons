package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.compat.StandingShieldExplosionProtection;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.rules.ShieldRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric has no Forge {@code ExplosionEvent.Detonate}, so the standing-shield
 * explosion safe zone is applied directly: blocks are removed from the
 * explosion list before {@code finalizeExplosion} destroys them, and entities
 * behind a shield get zero explosion exposure.
 */
@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @Inject(method = "explode", at = @At("HEAD"))
    private void chineseweapons$beginExplosion(CallbackInfo ci) {
        if (!this.level.isClientSide()) {
            StandingShieldExplosionProtection.push(new Vec3(this.x, this.y, this.z));
        }
    }

    @Inject(method = "explode", at = @At("RETURN"))
    private void chineseweapons$protectBlocksAndEnd(CallbackInfo ci) {
        if (this.level.isClientSide()) {
            return;
        }

        try {
            Vec3 center = new Vec3(this.x, this.y, this.z);
            AABB search = new AABB(center, center).inflate(32.0D);
            for (SongStandingShieldEntity shield
                    : this.level.getEntitiesOfClass(SongStandingShieldEntity.class, search)) {
                if (!ShieldRules.isInFront(shield.position(), shield.getShieldYaw(), center)) {
                    continue;
                }
                AABB safeZone = shield.getExplosionSafeZone();
                ((Explosion) (Object) this).getToBlow()
                        .removeIf(pos -> safeZone.contains(Vec3.atCenterOf(pos)));
            }
        } finally {
            StandingShieldExplosionProtection.pop();
        }
    }

    @Inject(method = "getSeenPercent", at = @At("HEAD"), cancellable = true)
    private static void chineseweapons$protectEntities(Vec3 center, Entity entity,
                                                       CallbackInfoReturnable<Float> cir) {
        if (StandingShieldExplosionProtection.isProtected(entity, center)) {
            cir.setReturnValue(0.0F);
        }
    }
}
