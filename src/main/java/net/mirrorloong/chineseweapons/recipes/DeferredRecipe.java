package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.function.Supplier;
public class DeferredRecipe {
    //old api DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID) -> new api DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, MODID)
    public static final DeferredRegister<RecipeType<?>> DeferredRecipe = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ChineseweaponsMod.MODID);

    public static final RegistryObject<RecipeType<WCTRecipe>> WeaponCastingShapedType=
            DeferredRecipe.register("weapon_casting_shaped", () -> new RecipeType<>() {
                public String toString() { return "chineseweapons:weapon_casting_shaped"; }
            });
    //old api DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID)
    public static final DeferredRegister<RecipeSerializer<?>> DeferredRecipeSer = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChineseweaponsMod.MODID);

    public static final RegistryObject<RecipeSerializer<?>> WeaponsCastingShapedSerType = DeferredRecipeSer.register("weapon_casting_shaped", new Supplier<RecipeSerializer<?>>() {
        @Override
        public RecipeSerializer<?> get() {
            return new WCTRecipeSer();
        }
    });

            public static void bootstrap() {
    }
}
