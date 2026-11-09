package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorEquipSound;

/**
 * Builds 26.1 armour material records from the shared {@link ArmorDefinition}
 * table. 26.1 dropped the item-bound {@code ArmorMaterial} interface in favour
 * of a record that feeds data components, and repair ingredients/visuals became
 * tag/asset references. The repair tag and equipment asset names keep the ids
 * of the per-material items this replaces.
 */
public final class ModArmorMaterials {
    private ModArmorMaterials() {
    }

    public static Item.Properties apply(Item.Properties properties, ArmorDefinition definition, ArmorType type) {
        return properties.humanoidArmor(create(definition), type);
    }

    public static ArmorMaterial create(ArmorDefinition definition) {
        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        defense.put(ArmorType.BOOTS, definition.feetDefense());
        defense.put(ArmorType.LEGGINGS, definition.legsDefense());
        defense.put(ArmorType.CHESTPLATE, definition.chestDefense());
        defense.put(ArmorType.HELMET, definition.helmetDefense());
        defense.put(ArmorType.BODY, definition.chestDefense());

        String name = assetName(definition);
        TagKey<Item> repair = TagKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "armor_repair/" + name));
        ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID,
                Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, name));

        return new ArmorMaterial(definition.durabilityMultiplier(), defense, definition.enchantmentValue(),
                equipSound(definition.equipSound()), definition.toughness(), definition.knockbackResistance(),
                repair, asset);
    }

    /**
     * The Bohai helmets kept camel-cased legacy material names, while their
     * 26.1 equipment assets and repair tags use the registry id.
     */
    private static String assetName(ArmorDefinition definition) {
        return switch (definition.id()) {
            case "diamond_bohai_helmet", "golden_bohai_helmet", "iron_bohai_helmet", "netherite_bohai_helmet" ->
                    definition.id();
            default -> definition.materialName();
        };
    }

    private static Holder<SoundEvent> equipSound(ArmorEquipSound sound) {
        return switch (sound) {
            case IRON -> SoundEvents.ARMOR_EQUIP_IRON;
            case GOLD -> SoundEvents.ARMOR_EQUIP_GOLD;
            case DIAMOND -> SoundEvents.ARMOR_EQUIP_DIAMOND;
            case NETHERITE -> SoundEvents.ARMOR_EQUIP_NETHERITE;
        };
    }
}
