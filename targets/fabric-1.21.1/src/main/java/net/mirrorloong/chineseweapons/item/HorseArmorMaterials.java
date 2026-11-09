package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Horse armour needs a registry-backed material because 1.21 resolves armour
 * textures and tooltips from the material's registry key. Fabric registers
 * eagerly, so the entries are created on first use during item bootstrap.
 */
public final class HorseArmorMaterials {
    private static final Map<Integer, Holder<ArmorMaterial>> BY_DEFENSE = new HashMap<>();

    private HorseArmorMaterials() {
    }

    public static Holder<ArmorMaterial> get(int defense) {
        return BY_DEFENSE.computeIfAbsent(defense, HorseArmorMaterials::register);
    }

    private static Holder<ArmorMaterial> register(int defense) {
        ArmorMaterial material = create(defense);
        ChineseWeapons.registration()
                .builder(Registries.ARMOR_MATERIAL)
                .register("horse_armor_" + defense, () -> material);
        return BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(material);
    }

    private static ArmorMaterial create(int defense) {
        Map<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        defenseMap.put(ArmorItem.Type.BODY, defense);
        return new ArmorMaterial(
                defenseMap,
                0,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(Items.LEATHER),
                List.of(new ArmorMaterial.Layer(
                        ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "horse_armor_" + defense))),
                0.0F,
                0.0F);
    }
}
