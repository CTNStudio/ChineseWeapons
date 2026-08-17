package net.mirrorloong.chineseweapons.compat.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.IntBounds;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class WeaponCastingRecipeSchema {
    private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
    private static final RecipeKey<List<String>> PATTERN = StringComponent.STRING.instance().asList().otherKey("pattern");
    private static final RecipeKey<TinyMap<Character, Ingredient>> KEY = MapRecipeComponent
            .patternOf(IngredientComponent.INGREDIENT.instance(), IntBounds.DEFAULT)
            .otherKey("key");
    private static final RecipeKey<Ingredient> MAIN_MATERIAL = IngredientComponent.INGREDIENT.inputKey("main_material");

    public static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, PATTERN, KEY, MAIN_MATERIAL)
            .constructor(RESULT, PATTERN, KEY, MAIN_MATERIAL)
            .uniqueId(RESULT);

    private WeaponCastingRecipeSchema() {
    }
}
