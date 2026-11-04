package net.mirrorloong.chineseweapons.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.client.model.Modelblack_chui_armor;
import net.mirrorloong.chineseweapons.client.model.Modelbohai_helmet;
import net.mirrorloong.chineseweapons.client.model.Modelearly_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelface_guard;
import net.mirrorloong.chineseweapons.client.model.Modelfine_scale_armor;
import net.mirrorloong.chineseweapons.client.model.Modelfootmen_armor;
import net.mirrorloong.chineseweapons.client.model.Modellate_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmiddle_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmountain_character_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsong_dynasty_infantry_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsuanni_helmet;
import net.mirrorloong.chineseweapons.client.model.Modeltang_dynasty_infantry_armor;
import net.mirrorloong.chineseweapons.content.ArmorSlot;

/**
 * Central model selection for the data-driven armour items. Replaces the
 * per-material item subclasses that each carried their own bake-and-select
 * code.
 */
public final class FabricArmorModels {
    private FabricArmorModels() {
    }

    public static HumanoidModel create(String modelId, ArmorSlot slot, LivingEntity living,
            HumanoidModel defaultModel) {
        Map<String, ModelPart> parts = new HashMap<>();
        parts.put("head", empty());
        parts.put("hat", empty());
        parts.put("body", empty());
        parts.put("right_arm", empty());
        parts.put("left_arm", empty());
        parts.put("right_leg", empty());
        parts.put("left_leg", empty());

        Parts selected = parts(modelId);
        switch (slot) {
            case HELMET -> parts.put("head", selected.head());
            case CHESTPLATE -> {
                parts.put("body", selected.body());
                parts.put("right_arm", selected.rightArm());
                parts.put("left_arm", selected.leftArm());
            }
            case LEGGINGS -> {
                parts.put("right_leg", selected.rightLeg());
                parts.put("left_leg", selected.leftLeg());
            }
            case BOOTS -> {
                parts.put("right_leg", selected.rightBoot());
                parts.put("left_leg", selected.leftBoot());
            }
        }

        HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), parts));
        armorModel.crouching = living.isShiftKeyDown();
        armorModel.riding = defaultModel.riding;
        armorModel.young = living.isBaby();
        return armorModel;
    }

    private static Parts parts(String modelId) {
        return switch (modelId) {
            case "bohai_helmet" -> {
                Modelbohai_helmet model = new Modelbohai_helmet(bake(Modelbohai_helmet.LAYER_LOCATION));
                yield new Parts(model.bipedHead, empty(), empty(), empty(), empty(), empty(), empty(), empty());
            }
            case "face_guard" -> {
                Modelface_guard model = new Modelface_guard(bake(Modelface_guard.LAYER_LOCATION));
                yield new Parts(model.bipedHead, empty(), empty(), empty(), empty(), empty(), empty(), empty());
            }
            case "suanni_helmet" -> {
                Modelsuanni_helmet model = new Modelsuanni_helmet(bake(Modelsuanni_helmet.LAYER_LOCATION));
                yield new Parts(model.bipedHead, empty(), empty(), empty(), empty(), empty(), empty(), empty());
            }
            case "black_chui_armor" -> {
                Modelblack_chui_armor model = new Modelblack_chui_armor(bake(Modelblack_chui_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.RightBoots, model.LeftBoots);
            }
            case "early_tang_dynasty_ming_guang_armor" -> {
                Modelearly_tang_dynasty_ming_guang_armor model =
                        new Modelearly_tang_dynasty_ming_guang_armor(bake(Modelearly_tang_dynasty_ming_guang_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.bipedRight, model.bipedLeft);
            }
            case "footmen_armor" -> {
                Modelfootmen_armor model = new Modelfootmen_armor(bake(Modelfootmen_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.bipedRight, model.bipedLeft);
            }
            case "late_tang_dynasty_ming_guang_armor" -> {
                Modellate_tang_dynasty_ming_guang_armor model =
                        new Modellate_tang_dynasty_ming_guang_armor(bake(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.RightBoots, model.LeftBoots);
            }
            case "middle_tang_dynasty_ming_guang_armor" -> {
                Modelmiddle_tang_dynasty_ming_guang_armor model =
                        new Modelmiddle_tang_dynasty_ming_guang_armor(bake(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.bipedRight, model.bipedLeft);
            }
            case "mountain_character_armor" -> {
                Modelmountain_character_armor model =
                        new Modelmountain_character_armor(bake(Modelmountain_character_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.bipedRight, model.bipedLeft);
            }
            case "fine_scale_armor" -> {
                Modelfine_scale_armor model = new Modelfine_scale_armor(bake(Modelfine_scale_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.RightBoots, model.LeftBoots);
            }
            case "song_dynasty_infantry_armor" -> {
                Modelsong_dynasty_infantry_armor model =
                        new Modelsong_dynasty_infantry_armor(bake(Modelsong_dynasty_infantry_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.RightBoots, model.LeftBoots);
            }
            case "tang_dynasty_infantry_armor" -> {
                Modeltang_dynasty_infantry_armor model =
                        new Modeltang_dynasty_infantry_armor(bake(Modeltang_dynasty_infantry_armor.LAYER_LOCATION));
                yield new Parts(model.bipedHead, model.bipedBody, model.bipedRightArm, model.bipedLeftArm,
                        model.bipedRightLeg, model.bipedLeftLeg, model.RightBoots, model.LeftBoots);
            }
            default -> throw new IllegalArgumentException("Unknown armour model: " + modelId);
        };
    }

    private static ModelPart bake(ModelLayerLocation location) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(location);
    }

    private static ModelPart empty() {
        return new ModelPart(Collections.emptyList(), Collections.emptyMap());
    }

    private record Parts(ModelPart head, ModelPart body, ModelPart rightArm, ModelPart leftArm,
                         ModelPart rightLeg, ModelPart leftLeg, ModelPart rightBoot, ModelPart leftBoot) {
    }
}
