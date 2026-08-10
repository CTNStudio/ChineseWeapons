package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.function.Supplier;
public class DeferredRecipe {
    public static final DeferredRegister<RecipeType<?>> DeferredRecipe = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ChineseweaponsMod.MODID);

    public static final RegistryObject<RecipeType<WCTRecipe>> WeaponCastingShapedType=
            DeferredRecipe.register("weapon_casting_shaped", () -> new RecipeType<>() {
                public String toString() { return "chineseweapons:weapon_casting_shaped"; }
            });
    public static final DeferredRegister<RecipeSerializer<?>> DeferredRecipeSer = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChineseweaponsMod.MODID);

    public static final RegistryObject<RecipeSerializer<?>> WeaponsCastingShapedSerType = DeferredRecipeSer.register("weapon_casting_shaped", new Supplier<RecipeSerializer<?>>() {
        @Override
        public RecipeSerializer<?> get() {
            return new WCTRecipeSer();
        }
    });
}
