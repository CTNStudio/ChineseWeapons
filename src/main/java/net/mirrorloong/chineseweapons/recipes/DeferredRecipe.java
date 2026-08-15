package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.function.Supplier;
public class DeferredRecipe {
    public static final DeferredRegister<RecipeType<?>> DeferredRecipe = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ChineseweaponsMod.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<WCTRecipe>> WeaponCastingShapedType=
            DeferredRecipe.register("weapon_casting_shaped", () -> new RecipeType<>() {
                public String toString() { return "chineseweapons:weapon_casting_shaped"; }
            });
    public static final DeferredRegister<RecipeSerializer<?>> DeferredRecipeSer = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChineseweaponsMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> WeaponsCastingShapedSerType = DeferredRecipeSer.register("weapon_casting_shaped", new Supplier<RecipeSerializer<?>>() {
        @Override
        public RecipeSerializer<?> get() {
            return new WCTRecipeSer();
        }
    });
}