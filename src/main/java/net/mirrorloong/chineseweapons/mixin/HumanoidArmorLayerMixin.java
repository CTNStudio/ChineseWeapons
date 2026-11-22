package net.mirrorloong.chineseweapons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.client.armor.CustomArmorModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V",
            at = @At("HEAD"), cancellable = true)
    private void chineseweapons$skipCustomArmor(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity,
                                                EquipmentSlot slot, int light, HumanoidModel model, CallbackInfo ci) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (!stack.isEmpty() && stack.getItem() instanceof CustomArmorModel) {
            ci.cancel();
        }
    }
}
