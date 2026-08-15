package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.EnumMap;
import java.util.List;

public class ChineseWeaponsArmorMaterials {

    public static Holder<ArmorMaterial> holder(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound, Item repairItem, float toughness, float knockbackResistance) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        defenseMap.put(ArmorItem.Type.HELMET, defense[0]);
        defenseMap.put(ArmorItem.Type.CHESTPLATE, defense[1]);
        defenseMap.put(ArmorItem.Type.LEGGINGS, defense[2]);
        defenseMap.put(ArmorItem.Type.BOOTS, defense[3]);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, name)));
        return Holder.direct(new ArmorMaterial(defenseMap, enchantmentValue, equipSound, () -> Ingredient.of(repairItem), layers, toughness, knockbackResistance));
    }
}
