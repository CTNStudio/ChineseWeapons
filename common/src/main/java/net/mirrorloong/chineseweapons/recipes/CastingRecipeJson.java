package net.mirrorloong.chineseweapons.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RecipeSupport;

/**
 * Parser for the legacy ({@code pattern} / {@code key} / {@code main_material})
 * casting recipe JSON shared by 1.18.2-1.20.1. Ingredient and result decoding
 * are the only pieces the target supplies through {@link RecipeSupport}.
 */
public final class CastingRecipeJson {

    private CastingRecipeJson() {
    }

    /**
     * @param debugId  id used in the parse-error log, typically
     *                 {@code ResourceLocation#toDebugFileName()}
     * @param recipeId id used in duplicate warnings
     * @return the parsed recipe, or {@code null} when the JSON is invalid
     */
    public static CastingRecipe read(String debugId, String recipeId, JsonObject jsonObject,
            RecipeSupport support) {
        try {
            List<String> pattern = new ArrayList<>();
            for (JsonElement element : jsonObject.getAsJsonArray("pattern")) {
                pattern.add(element.getAsString());
            }

            Map<String, Ingredient> key = new HashMap<>();
            key.put(" ", Ingredient.of(Items.AIR));
            JsonObject keyJson = jsonObject.getAsJsonObject("key");
            for (String symbol : keyJson.keySet()) {
                key.put(symbol, support.ingredientFromJson(keyJson.get(symbol)));
            }

            Ingredient mainMaterial = support.ingredientFromJson(jsonObject.get("main_material"));
            ItemStack result = support.resultFromJson(jsonObject.getAsJsonObject("result"));

            List<Ingredient> ingredients = CastingRecipe.legacyIngredients(pattern, key, mainMaterial);
            RecipeDuplicates.check(RecipeDuplicates.signature(jsonObject), recipeId);

            return new CastingRecipe(ingredients, result);
        } catch (Exception exception) {
            ChineseWeapons.LOGGER.error(debugId.concat(" recipe is error"));
        }
        return null;
    }
}
