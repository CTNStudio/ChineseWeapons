package net.mirrorloong.chineseweapons.compat.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;

public final class WeaponCastingRecipeSchema {
    private static final RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    private static final RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
    private static final RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");
    private static final RecipeKey<InputItem> MAIN_MATERIAL = ItemComponents.INPUT.key("main_material");

    public static final RecipeSchema SCHEMA = new RecipeSchema(RESULT, PATTERN, KEY, MAIN_MATERIAL)
            .constructor(RESULT, PATTERN, KEY, MAIN_MATERIAL)
            .uniqueOutputId(RESULT);

    private WeaponCastingRecipeSchema() {
    }
}
