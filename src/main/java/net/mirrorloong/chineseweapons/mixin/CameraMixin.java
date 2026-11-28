package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.mirrorloong.chineseweapons.client.BedCrossbowClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Inject(
            method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
            at = @At("TAIL")
    )
    private void chineseweapons$bedCrossbowShake(BlockGetter level, Entity entity, boolean detached,
                                                 boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!BedCrossbowClientEvents.hasCameraShake()) {
            return;
        }
        this.setRotation(
                this.yRot + BedCrossbowClientEvents.shakeYaw(partialTick),
                this.xRot + BedCrossbowClientEvents.shakePitch(partialTick));
    }
}
