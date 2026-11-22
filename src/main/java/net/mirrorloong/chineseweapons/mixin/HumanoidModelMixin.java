package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.client.event.ArmPoseEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void chineseweapons$applyArmPose(LivingEntity entity, float limbSwing, float limbSwingAmount,
                                             float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ArmPoseEvents.invokeAll((HumanoidModel<?>) (Object) this, entity);
    }
}
