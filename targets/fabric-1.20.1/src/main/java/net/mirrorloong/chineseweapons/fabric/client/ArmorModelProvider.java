package net.mirrorloong.chineseweapons.fabric.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

/**
 * Fabric-side replacement for Forge's {@code Item#initializeClient} /
 * {@code IClientItemExtensions} armor model hook. The armor item classes that
 * used to return a custom model from {@code initializeClient} implement this
 * interface instead; {@link FabricArmorRenderers} registers one
 * {@code ArmorRenderer} per implementing item.
 */
public interface ArmorModelProvider {
    HumanoidModel createArmorModel(LivingEntity living, HumanoidModel defaultModel);
}
