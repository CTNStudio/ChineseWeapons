package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorEquipSound;
import net.mirrorloong.chineseweapons.content.ArmorSlot;

/**
 * Builds the 26.1 {@link ArmorMaterial} record from the per-set values that the
 * shared 1.20.1 sources inline as an anonymous material. Defence is ordered
 * feet, legs, chest, helmet, matching {@link ArmorType#getDurability(int)}'s
 * slot ordering.
 */
public final class ArmorSupport {
    private ArmorSupport() {
    }

    public static ArmorMaterial armor(int durability, int[] defense, int enchantmentValue,
            Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
            TagKey<Item> repairIngredient, String assetName) {
        Map<ArmorType, Integer> defenseMap = new EnumMap<>(ArmorType.class);
        defenseMap.put(ArmorType.BOOTS, defense[0]);
        defenseMap.put(ArmorType.LEGGINGS, defense[1]);
        defenseMap.put(ArmorType.CHESTPLATE, defense[2]);
        defenseMap.put(ArmorType.HELMET, defense[3]);
        defenseMap.put(ArmorType.BODY, defense[2]);
        return new ArmorMaterial(durability, defenseMap, enchantmentValue, equipSound, toughness,
                knockbackResistance, repairIngredient, EquipmentAssets.createId(assetName));
    }

    /**
     * Builds the material for one entry of the shared armour table.
     */
    public static ArmorMaterial armor(ArmorDefinition definition) {
        return armor(definition.durabilityMultiplier(),
                new int[] {definition.feetDefense(), definition.legsDefense(),
                        definition.chestDefense(), definition.helmetDefense()},
                definition.enchantmentValue(), equipSound(definition.equipSound()),
                definition.toughness(), definition.knockbackResistance(),
                repairIngredient(definition.materialKey()), equipmentAsset(definition));
    }

    public static ArmorType type(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> ArmorType.HELMET;
            case CHESTPLATE -> ArmorType.CHESTPLATE;
            case LEGGINGS -> ArmorType.LEGGINGS;
            case BOOTS -> ArmorType.BOOTS;
        };
    }

    /**
     * 26.1 renders armour from data-driven equipment assets. Every set is stored
     * under its registry path except the face guards, whose assets drop the
     * {@code _helmet} suffix.
     */
    public static String equipmentAsset(ArmorDefinition definition) {
        String id = definition.id();
        if (id.endsWith("_face_guard_helmet")) {
            id = id.substring(0, id.length() - "_helmet".length());
        }
        return ChineseWeapons.MOD_ID + ":" + id;
    }

    private static Holder<SoundEvent> equipSound(ArmorEquipSound sound) {
        return switch (sound) {
            case IRON -> SoundEvents.ARMOR_EQUIP_IRON;
            case GOLD -> SoundEvents.ARMOR_EQUIP_GOLD;
            case DIAMOND -> SoundEvents.ARMOR_EQUIP_DIAMOND;
            case NETHERITE -> SoundEvents.ARMOR_EQUIP_NETHERITE;
        };
    }

    private static TagKey<Item> repairIngredient(String materialKey) {
        return switch (materialKey) {
            case "iron" -> ItemTags.REPAIRS_IRON_ARMOR;
            case "golden" -> ItemTags.REPAIRS_GOLD_ARMOR;
            case "diamond" -> ItemTags.REPAIRS_DIAMOND_ARMOR;
            case "netherite" -> ItemTags.REPAIRS_NETHERITE_ARMOR;
            default -> throw new IllegalArgumentException("Unknown armour material: " + materialKey);
        };
    }

    /**
     * The 1.20.1 code tested {@code player.getArmorSlots()}; that view no longer
     * exists, so the four worn slots are checked directly.
     */
    public static boolean isWorn(LivingEntity entity, ItemStack stack) {
        for (EquipmentSlot slot : new EquipmentSlot[] {
                EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD }) {
            if (entity.getItemBySlot(slot) == stack) {
                return true;
            }
        }
        return false;
    }
}
