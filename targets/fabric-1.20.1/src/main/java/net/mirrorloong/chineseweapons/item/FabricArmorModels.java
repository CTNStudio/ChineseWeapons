package net.mirrorloong.chineseweapons.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.client.model.FabricModelFactory;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelParts;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpec;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpecs;
import net.mirrorloong.chineseweapons.content.ArmorSlot;

/**
 * Central model selection for the data-driven armour items: shared
 * {@link ArmorModelSpec} table plus the Fabric factory.
 */
public final class FabricArmorModels {
    private FabricArmorModels() {
    }

    public static HumanoidModel create(String modelId, ArmorSlot slot, LivingEntity living,
                                       HumanoidModel defaultModel) {
        ArmorModelSpec spec = ArmorModelSpecs.get(modelId);
        HumanoidModel<LivingEntity> armorModel = new HumanoidModel<>(
                ArmorModelParts.humanoidRoot(spec, slot, FabricModelFactory.INSTANCE.bake(spec.model())));
        armorModel.crouching = living.isShiftKeyDown();
        armorModel.riding = defaultModel.riding;
        armorModel.young = living.isBaby();
        return armorModel;
    }
}
