package net.mirrorloong.chineseweapons.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.mirrorloong.chineseweapons.spi.RecipeSupport;

/**
 * Legacy ({@code pattern}/{@code key}/{@code main_material}) JSON decoding for
 * the shared casting recipe parser.
 */
public final class RecipeJsonSupport implements RecipeSupport {

    @Override
    public Ingredient ingredientFromJson(JsonElement json) {
        return Ingredient.fromJson(json);
    }

    @Override
    public ItemStack resultFromJson(JsonObject json) {
        return ShapedRecipe.itemStackFromJson(json);
    }
}
