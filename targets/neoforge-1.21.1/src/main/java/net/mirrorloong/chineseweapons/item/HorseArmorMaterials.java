package net.mirrorloong.chineseweapons.item;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Horse armour needs a registry-backed material because {@code AnimalArmorItem}
 * resolves its texture from the material's registry key.
 */
public final class HorseArmorMaterials {
    private static final DeferredRegister<ArmorMaterial> REGISTER =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ChineseWeapons.MOD_ID);
    private static final Map<Integer, DeferredHolder<ArmorMaterial, ArmorMaterial>> BY_DEFENSE = new HashMap<>();
    private static boolean initialized;

    private HorseArmorMaterials() {
    }

    public static void init(IEventBus modBus) {
        if (initialized) {
            return;
        }
        initialized = true;
        for (int defense : new int[]{8, 11, 18, 25}) {
            BY_DEFENSE.put(defense, REGISTER.register("horse_armor_" + defense,
                    () -> create(defense)));
        }
        REGISTER.register(modBus);
    }

    public static Holder<ArmorMaterial> get(int defense) {
        DeferredHolder<ArmorMaterial, ArmorMaterial> holder = BY_DEFENSE.get(defense);
        if (holder == null) {
            throw new IllegalArgumentException("Unknown horse armour defense: " + defense);
        }
        return holder;
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
