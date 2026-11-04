package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class WCTRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final CastingRecipe recipe;

    public WCTRecipe(ResourceLocation id, NonNullList<Ingredient> input, ItemStack output) {
        this(id, new CastingRecipe(input, output));
    }

    public WCTRecipe(ResourceLocation id, CastingRecipe recipe) {
        this.id = id;
        this.recipe = recipe;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return recipe.matches(inv::getItem);
    }

    @Override
    public ItemStack assemble(Container inv) {
        return recipe.assemble();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem() {
        return recipe.result();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipe.ingredients();
    }

    public ItemStack getResultItem2() {
        return recipe.result();
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
