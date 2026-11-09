package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Horse armour is data-component driven in 26.1; the material supplies the
 * defence value and the equipment asset used by the (data driven) model.
 */
public final class HorseArmorMaterials {
    private HorseArmorMaterials() {
    }

    public static ArmorMaterial get(int defense) {
        Map<ArmorType, Integer> defenseMap = new EnumMap<>(ArmorType.class);
        defenseMap.put(ArmorType.BODY, defense);
        ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID,
                Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "horse_armor_" + defense));
        return new ArmorMaterial(0, defenseMap, 0, SoundEvents.ARMOR_EQUIP_LEATHER,
                0.0F, 0.0F, ItemTags.REPAIRS_LEATHER_ARMOR, asset);
    }

    public static Item.Properties apply(Item.Properties properties, int defense) {
        return properties.horseArmor(get(defense));
    }
}
