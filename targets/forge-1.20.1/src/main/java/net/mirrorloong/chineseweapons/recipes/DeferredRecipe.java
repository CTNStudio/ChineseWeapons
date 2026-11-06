package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

import java.util.function.Supplier;
public class DeferredRecipe {
    private static final RegistryBuilder<RecipeType<?>> DeferredRecipe = ChineseWeapons.registration().builder(Registries.RECIPE_TYPE);

    public static final RegistryEntry<RecipeType<WCTRecipe>> WeaponCastingShapedType=
            DeferredRecipe.register("weapon_casting_shaped", () -> new RecipeType<>() {
                public String toString() { return "chineseweapons:weapon_casting_shaped"; }
            });
    private static final RegistryBuilder<RecipeSerializer<?>> DeferredRecipeSer = ChineseWeapons.registration().builder(Registries.RECIPE_SERIALIZER);

    public static final RegistryEntry<RecipeSerializer<?>> WeaponsCastingShapedSerType = DeferredRecipeSer.register("weapon_casting_shaped", new Supplier<RecipeSerializer<?>>() {
        @Override
        public RecipeSerializer<?> get() {
            return new WCTRecipeSer();
        }
    });
    public static final RegistryEntry<RecipeSerializer<?>> DyeableArmorSerType =
            DeferredRecipeSer.register("crafting_special_dyeablearmor", () -> DyeableArmorRecipe.SERIALIZER);

    public static void init() {
    }
}
