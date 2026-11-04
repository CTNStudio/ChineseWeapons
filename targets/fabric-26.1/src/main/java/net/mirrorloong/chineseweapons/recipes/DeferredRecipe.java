package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public class DeferredRecipe {
    private static final RegistryBuilder<RecipeType<?>> TYPES =
            ChineseWeapons.registration().builder(Registries.RECIPE_TYPE);

    public static final RegistryEntry<RecipeType<WCTRecipe>> WeaponCastingShapedType =
            TYPES.register("weapon_casting_shaped", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "chineseweapons:weapon_casting_shaped";
                }
            });

    private static final RegistryBuilder<RecipeSerializer<?>> SERIALIZERS =
            ChineseWeapons.registration().builder(Registries.RECIPE_SERIALIZER);

    public static final RegistryEntry<RecipeSerializer<?>> WeaponsCastingShapedSerType =
            SERIALIZERS.register("weapon_casting_shaped", () -> WCTRecipeSer.SERIALIZER);

    public static void init() {
    }
}
