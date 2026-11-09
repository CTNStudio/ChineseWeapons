package net.mirrorloong.chineseweapons.content;

import net.minecraft.world.item.Item;

/**
 * Everything the generic armour item needs to recreate one material/set pair.
 * Defence values follow the vanilla slot order: feet, legs, chest, helmet.
 */
public record ArmorDefinition(
        String id,
        String materialKey,
        int durabilityMultiplier,
        int feetDefense,
        int legsDefense,
        int chestDefense,
        int helmetDefense,
        int enchantmentValue,
        float toughness,
        float knockbackResistance,
        ArmorEquipSound equipSound,
        Item repairItem,
        String baseTexture,
        String overlayTexture,
        String modelId,
        boolean dyeable,
        boolean fireResistant,
        boolean piglinNeutral,
        boolean snowWalk,
        String materialName) {
}
