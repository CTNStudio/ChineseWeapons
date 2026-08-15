package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;


public class WCTRecipe implements Recipe<RecipeInput> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> input;
    private final ItemStack output;

    public WCTRecipe(ResourceLocation id, NonNullList<Ingredient> input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }
    @Override
    public boolean matches(RecipeInput inv, Level level) {
        for (int i = 0; i < 10; ++i) {
            if (!input.get(i).test(inv.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput inv, HolderLookup.Provider registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return output.copy();
    }
    @Override
    public NonNullList<Ingredient> getIngredients(){
        return input;
    }
    public ItemStack getResultItem2(){
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DeferredRecipe.WeaponsCastingShapedSerType.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DeferredRecipe.WeaponCastingShapedType.get();
    }
}
