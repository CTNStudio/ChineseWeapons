package net.mirrorloong.chineseweapons.recipes;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Duplicate detection for casting recipes. Two recipes with the same pattern,
 * keys and main material but different outputs are almost always a mistake,
 * so the second one is reported. The signature is derived from the raw JSON so
 * it stays independent of ingredient parsing.
 */
public final class RecipeDuplicates {
    private static final Map<String, String> REGISTERED = new HashMap<>();

    private RecipeDuplicates() {
    }

    /**
     * Registers the recipe signature and logs an error when another recipe
     * already claimed it.
     *
     * @param recipeId display id of the recipe, e.g. {@code chineseweapons:iron_dagger_axe}
     */
    public static void check(String signature, String recipeId) {
        String existing = REGISTERED.get(signature);
        if (existing != null) {
            ChineseWeapons.LOGGER.error("Duplicate recipe detected: " + existing + " <--> " + recipeId
                    + " - same ingredients but different output");
        } else {
            REGISTERED.put(signature, recipeId);
        }
    }

    /** Signature of a legacy casting recipe JSON object. */
    public static String signature(JsonObject jsonObject) {
        StringBuilder signature = new StringBuilder();

        for (JsonElement patternElement : jsonObject.getAsJsonArray("pattern")) {
            char[] pattern = patternElement.getAsString().toCharArray();
            for (int column = 0; column < 3; column++) {
                String key = String.valueOf(pattern[column]);
                if (key.equals(" ")) {
                    signature.append("air|");
                } else {
                    JsonElement keyJson = jsonObject.getAsJsonObject("key").get(key);
                    signature.append(ingredientSignature(keyJson)).append('|');
                }
            }
        }

        signature.append(ingredientSignature(jsonObject.get("main_material")));
        return signature.toString();
    }

    private static String ingredientSignature(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return "null";
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("tag")) {
            return "tag:" + jsonObject.get("tag").getAsString();
        }

        if (jsonObject.has("item")) {
            return "item:" + jsonObject.get("item").getAsString();
        }

        return "json:" + jsonElement;
    }
}
