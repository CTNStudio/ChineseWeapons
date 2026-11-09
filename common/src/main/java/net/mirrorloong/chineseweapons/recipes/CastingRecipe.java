package net.mirrorloong.chineseweapons.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Version-neutral data and matching logic of the weapon casting recipe.
 *
 * <p>The loader-specific recipe classes only wrap this object: they own the
 * {@code Recipe} interface plumbing, the JSON/codec shape and the network
 * format, while slot order, ingredient lookup and duplicate handling live
 * here.</p>
 */
public final class CastingRecipe {
    /** Number of inventory slots the casting table feeds to the recipe. */
    public static final int SLOT_COUNT = 10;

    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final boolean skipNullIngredients;

    public CastingRecipe(List<Ingredient> ingredients, ItemStack result) {
        this(ingredients, result, false);
    }

    /**
     * @param skipNullIngredients mirrors the 26.1 adapter, whose pattern rows
     *                            may leave holes when a key is missing
     */
    public CastingRecipe(List<Ingredient> ingredients, ItemStack result, boolean skipNullIngredients) {
        this.ingredients = ingredients;
        this.result = result;
        this.skipNullIngredients = skipNullIngredients;
    }

    /**
     * Builds the ten ingredients from the 1.18.2-1.20.1 JSON pattern. A symbol
     * missing from {@code key} stays {@code null} and a row shorter than three
     * characters fails, matching the legacy serializer.
     */
    public static List<Ingredient> legacyIngredients(List<String> pattern, Map<String, Ingredient> key,
            Ingredient mainMaterial) {
        Map<String, String> symbolsBySlot = new java.util.HashMap<>();
        int slot = 0;
        for (String row : pattern) {
            char[] symbols = row.toCharArray();
            for (int column = 0; column < 3; column++) {
                symbolsBySlot.put(String.valueOf(++slot), String.valueOf(symbols[column]));
            }
        }

        List<Ingredient> ingredients = new ArrayList<>(SLOT_COUNT);
        for (int index = 1; index <= 9; index++) {
            ingredients.add(key.get(symbolsBySlot.get(String.valueOf(index))));
        }
        ingredients.add(mainMaterial);
        return ingredients;
    }

    /**
     * Builds the ten ingredients from the 1.21 codec fields: exactly three
     * rows, missing rows and symbols padded with spaces, unknown keys as air.
     */
    public static List<Ingredient> paddedIngredients(List<String> pattern, Map<String, Ingredient> key,
            Ingredient mainMaterial) {
        List<Ingredient> ingredients = new ArrayList<>(SLOT_COUNT);
        for (int row = 0; row < 3; row++) {
            String line = row < pattern.size() ? pattern.get(row) : "   ";
            for (int column = 0; column < 3; column++) {
                char symbol = column < line.length() ? line.charAt(column) : ' ';
                Ingredient ingredient = key.get(String.valueOf(symbol));
                ingredients.add(ingredient == null ? Ingredient.of(Items.AIR) : ingredient);
            }
        }
        ingredients.add(mainMaterial);
        return ingredients;
    }

    /**
     * Builds the ingredients for the 26.1 codec: extra rows are ignored, rows
     * may be shorter, and unknown keys stay {@code null}.
     */
    public static List<Ingredient> sparseIngredients(List<String> pattern, Map<String, Ingredient> key,
            Ingredient mainMaterial) {
        List<Ingredient> ingredients = new ArrayList<>(SLOT_COUNT);
        for (int row = 0; row < Math.min(3, pattern.size()); row++) {
            String line = pattern.get(row);
            for (int column = 0; column < 3; column++) {
                char symbol = column < line.length() ? line.charAt(column) : ' ';
                ingredients.add(key.get(String.valueOf(symbol)));
            }
        }
        ingredients.add(mainMaterial);
        return ingredients;
    }

    /**
     * Builds the ingredients for the 26.1 Fabric codec: every character of
     * every row, unknown keys as air, without padding rows to three columns.
     */
    public static List<Ingredient> variableWidthIngredients(List<String> pattern, Map<String, Ingredient> key,
            Ingredient mainMaterial) {
        List<Ingredient> ingredients = new ArrayList<>(SLOT_COUNT);
        for (String row : pattern) {
            for (int i = 0; i < row.length(); i++) {
                String symbol = String.valueOf(row.charAt(i));
                ingredients.add(key.getOrDefault(symbol, Ingredient.of(Items.AIR)));
            }
        }
        ingredients.add(mainMaterial);
        return ingredients;
    }

    public boolean matches(IntFunction<ItemStack> slots) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            Ingredient ingredient = i < ingredients.size() ? ingredients.get(i) : null;
            if (ingredient == null) {
                if (skipNullIngredients) {
                    continue;
                }
                return false;
            }
            if (!ingredient.test(slots.apply(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(Container container) {
        return matches(container::getItem);
    }

    public ItemStack assemble() {
        return result.copy();
    }

    public List<Ingredient> rawIngredients() {
        return ingredients;
    }

    /** Ingredients padded to a {@link NonNullList}; unknown entries become air. */
    public NonNullList<Ingredient> ingredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(ingredients.size(), Ingredient.of(Items.AIR));
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (ingredient != null) {
                list.set(i, ingredient);
            }
        }
        return list;
    }

    public ItemStack result() {
        return result.copy();
    }
}
