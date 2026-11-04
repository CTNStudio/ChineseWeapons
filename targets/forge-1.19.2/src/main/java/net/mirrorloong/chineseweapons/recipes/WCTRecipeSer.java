package net.mirrorloong.chineseweapons.recipes;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import org.jetbrains.annotations.Nullable;

public class WCTRecipeSer implements RecipeSerializer<WCTRecipe> {

    @Override
    public WCTRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        CastingRecipe recipe = CastingRecipeJson.read(resourceLocation.toDebugFileName(),
                resourceLocation.toString(), jsonObject, ChineseWeapons.platform().recipes());
        return recipe == null ? null : new WCTRecipe(resourceLocation, recipe);
    }

    @Override
    public @Nullable WCTRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int ingredientSize = buf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);
        for (int i = 0; i < ingredientSize; ++i) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }
        ItemStack result = buf.readItem();
        return new WCTRecipe(id, ingredients, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, WCTRecipe wctRecipe) {
        buf.writeVarInt(wctRecipe.getIngredients().size());
        for (Ingredient ingredient : wctRecipe.getIngredients()) {
            ingredient.toNetwork(buf);
        }
        buf.writeItemStack(wctRecipe.getResultItem2(), false);
    }
}
