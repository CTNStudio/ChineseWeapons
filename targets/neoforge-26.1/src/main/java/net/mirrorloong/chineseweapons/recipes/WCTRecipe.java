package net.mirrorloong.chineseweapons.recipes;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Casting table recipe. 26.1 recipes are codec driven, so the legacy
 * pattern/key/main_material JSON is parsed through the codec in
 * {@link WCTRecipeSer}; slot matching and ingredient layout come from the
 * shared {@link CastingRecipe}.
 */
public class WCTRecipe implements Recipe<WCTRecipeInput> {
    private final List<Ingredient> input;
    private final ItemStackTemplate result;
    private final CastingRecipe recipe;

    public WCTRecipe(List<Ingredient> input, ItemStackTemplate result) {
        this.input = input;
        this.result = result;
        this.recipe = new CastingRecipe(input, ItemStack.EMPTY, true);
    }

    @Override
    public boolean matches(WCTRecipeInput inv, Level level) {
        return recipe.matches(inv::getItem);
    }

    @Override
    public ItemStack assemble(WCTRecipeInput input) {
        return this.result.create();
    }

    public ItemStack getResultItem2() {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<WCTRecipeInput>> getSerializer() {
        return WCTRecipeSer.SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WCTRecipeInput>> getType() {
        return DeferredRecipe.WeaponCastingShapedType.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public List<Ingredient> ingredients() {
        return input;
    }

    public ItemStackTemplate resultTemplate() {
        return result;
    }
}
