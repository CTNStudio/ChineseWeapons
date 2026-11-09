package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Builds the 1.21 armour material record from the legacy per-item values that
 * the shared 1.20.1 sources used to inline as an anonymous {@code ArmorMaterial}.
 * The defence array is ordered feet, legs, chest, helmet.
 */
public final class CustomArmorMaterials {
    private CustomArmorMaterials() {
    }

    public static Holder<ArmorMaterial> create(String name, int durabilityMultiplier, int[] defense,
            int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient,
            float toughness, float knockbackResistance) {
        Map<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        defenseMap.put(ArmorItem.Type.BOOTS, defense[0]);
        defenseMap.put(ArmorItem.Type.LEGGINGS, defense[1]);
        defenseMap.put(ArmorItem.Type.CHESTPLATE, defense[2]);
        defenseMap.put(ArmorItem.Type.HELMET, defense[3]);
        defenseMap.put(ArmorItem.Type.BODY, defense[2]);

        ArmorMaterial material = new ArmorMaterial(
                defenseMap,
                enchantmentValue,
                Holder.direct(equipSound.value()),
                repairIngredient,
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(
                        ChineseWeapons.MOD_ID, name.toLowerCase(Locale.ROOT)))),
                toughness,
                knockbackResistance);
        return Holder.direct(material);
    }
}
