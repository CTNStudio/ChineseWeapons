package net.mirrorloong.chineseweapons.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mirrorloong.chineseweapons.client.model.ForgeModelFactory;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelParts;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpec;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpecs;
import net.mirrorloong.chineseweapons.content.ArmorSlot;

@OnlyIn(Dist.CLIENT)
public final class ArmorModels {
    private ArmorModels() {
    }

    public static HumanoidModel<?> create(String modelId, ArmorSlot slot, LivingEntity living,
                                          HumanoidModel<?> defaultModel) {
        ArmorModelSpec spec = ArmorModelSpecs.get(modelId);
        HumanoidModel<LivingEntity> armorModel = new HumanoidModel<>(
                ArmorModelParts.humanoidRoot(spec, slot, ForgeModelFactory.INSTANCE.bake(spec.model())));
        armorModel.crouching = living.isShiftKeyDown();
        armorModel.riding = defaultModel.riding;
        armorModel.young = living.isBaby();
        return armorModel;
    }
}
