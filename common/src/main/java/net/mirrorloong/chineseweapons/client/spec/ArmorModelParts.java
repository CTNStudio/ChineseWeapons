package net.mirrorloong.chineseweapons.client.spec;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.model.geom.ModelPart;
import net.mirrorloong.chineseweapons.content.ArmorSlot;

/**
 * Fills the {@code HumanoidModel} part names from an armour model's baked root,
 * replacing the per-target model-id switches with the shared
 * {@link ArmorModelSpec} table. Missing parts become empty {@link ModelPart}s,
 * matching the former {@code empty()} placeholders.
 */
public final class ArmorModelParts {
    private ArmorModelParts() {
    }

    public static ModelPart humanoidRoot(ArmorModelSpec spec, ArmorSlot slot, ModelPart bakedRoot) {
        Map<String, ModelPart> parts = new LinkedHashMap<>();
        parts.put("head", empty());
        parts.put("hat", empty());
        parts.put("body", empty());
        parts.put("right_arm", empty());
        parts.put("left_arm", empty());
        parts.put("right_leg", empty());
        parts.put("left_leg", empty());

        switch (slot) {
            case HELMET -> parts.put("head", child(bakedRoot, spec.head()));
            case CHESTPLATE -> {
                parts.put("body", child(bakedRoot, spec.body()));
                parts.put("right_arm", child(bakedRoot, spec.rightArm()));
                parts.put("left_arm", child(bakedRoot, spec.leftArm()));
            }
            case LEGGINGS -> {
                parts.put("right_leg", child(bakedRoot, spec.rightLeg()));
                parts.put("left_leg", child(bakedRoot, spec.leftLeg()));
            }
            case BOOTS -> {
                parts.put("right_leg", child(bakedRoot, spec.rightBoot()));
                parts.put("left_leg", child(bakedRoot, spec.leftBoot()));
            }
        }
        return new ModelPart(Collections.emptyList(), parts);
    }

    private static ModelPart child(ModelPart root, String name) {
        return name == null ? empty() : root.getChild(name);
    }

    private static ModelPart empty() {
        return new ModelPart(Collections.emptyList(), Collections.emptyMap());
    }
}
