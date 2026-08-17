package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.EnumMap;
import java.util.List;

/**
 * Registered (keyed) armor materials for the Tang Dynasty horse armor.
 * {@link net.minecraft.world.item.AnimalArmorItem} requires a keyed holder so that it can
 * build its texture path from the material's key, hence this DeferredRegister.
 */
public class ChineseWeaponsModHorseArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, ChineseweaponsMod.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DIAMOND_TANG_DYNASTY_HORSE =
            REGISTRY.register("diamond_tang_dynasty_horse_armor",
                    () -> horseMaterial(18, Items.DIAMOND, SoundEvents.ARMOR_EQUIP_DIAMOND));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IRON_TANG_DYNASTY_HORSE =
            REGISTRY.register("iron_tang_dynasty_horse_armor",
                    () -> horseMaterial(8, Items.IRON_INGOT, SoundEvents.ARMOR_EQUIP_IRON));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NETHERITE_TANG_DYNASTY_HORSE =
            REGISTRY.register("netherite_tang_dynasty_horse_armor",
                    () -> horseMaterial(25, Items.NETHERITE_INGOT, SoundEvents.ARMOR_EQUIP_NETHERITE));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOLDEN_TANG_DYNASTY_HORSE =
            REGISTRY.register("golden_tang_dynasty_horse_armor",
                    () -> horseMaterial(11, Items.GOLD_INGOT, SoundEvents.ARMOR_EQUIP_GOLD));

    private static ArmorMaterial horseMaterial(int defense, Item repairItem, Holder<SoundEvent> equipSound) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        defenseMap.put(ArmorItem.Type.HELMET, 0);
        defenseMap.put(ArmorItem.Type.CHESTPLATE, 0);
        defenseMap.put(ArmorItem.Type.LEGGINGS, 0);
        defenseMap.put(ArmorItem.Type.BOOTS, 0);
        defenseMap.put(ArmorItem.Type.BODY, defense);
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "tang_dynasty_horse_armor")));
        return new ArmorMaterial(defenseMap, 0, equipSound, () -> Ingredient.of(repairItem), layers, 0.0F, 0.0F);
    }
}
