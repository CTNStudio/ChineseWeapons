package net.mirrorloong.chineseweapons.client.armor;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

public final class ArmorRendererRegistry {

    private ArmorRendererRegistry() {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void init() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            LivingEntityRenderer renderer = (LivingEntityRenderer) entityRenderer;
            if (renderer.getModel() instanceof HumanoidModel) {
                registrationHelper.register(new CustomArmorFeatureRenderer(renderer, context.getModelSet()));
            }
        });
    }
}
