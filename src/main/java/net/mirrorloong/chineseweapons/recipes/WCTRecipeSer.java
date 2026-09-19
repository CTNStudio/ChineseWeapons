package net.mirrorloong.chineseweapons.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import java.util.HashMap;
import java.util.Map;

public class WCTRecipeSer implements RecipeSerializer<WCTRecipe> {
    private static final ResourceLocation PLACEHOLDER_ID = ResourceLocation.withDefaultNamespace("weapon_casting_shaped");
    private static final Map<String, ResourceLocation> trueRegRecipe = new HashMap<>();

    private static final MapCodec<WCTRecipe> CODEC = MapCodec.assumeMapUnsafe(
            Codec.PASSTHROUGH.xmap(
                    dynamic -> decode(dynamic),
                    recipe -> new Dynamic<>(JsonOps.INSTANCE, encode(recipe))
            )
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                buf.writeVarInt(ingredients.size());
                for (Ingredient ingredient : ingredients) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                }
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.getResultItem2());
            },
            buf -> {
                int ingredientSize = buf.readVarInt();
                NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);
                for (int i = 0; i < ingredientSize; ++i) {
                    ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                }
                ItemStack result = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                return new WCTRecipe(PLACEHOLDER_ID, ingredients, result);
            }
    );

    @Override
    public MapCodec<WCTRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static WCTRecipe decode(Dynamic<?> dynamic) {
        DynamicOps<JsonElement> ops = castOps(dynamic.getOps());
        JsonObject jsonObject = ((JsonElement) dynamic.getValue()).getAsJsonObject();
        try {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            if (!jsonObject.has("pattern") || !jsonObject.get("pattern").isJsonArray()) {
                throw new IllegalArgumentException("Missing or invalid pattern array");
            }

            JsonArray patternArray = jsonObject.getAsJsonArray("pattern");
            if (patternArray.size() != 3) {
                throw new IllegalArgumentException("Pattern must have 3 rows");
            }

            if (!jsonObject.has("key") || !jsonObject.get("key").isJsonObject()) {
                throw new IllegalArgumentException("Missing or invalid key object");
            }

            JsonObject keyObject = jsonObject.getAsJsonObject("key");
            Map<String, Ingredient> keyPattern = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : keyObject.entrySet()) {
                keyPattern.put(entry.getKey(), ingredientFromJson(ops, entry.getValue()));
            }

            for (JsonElement rowElement : patternArray) {
                String rowPattern = rowElement.getAsString();
                if (rowPattern.length() != 3) {
                    throw new IllegalArgumentException("Each pattern row must be 3 characters long: " + rowPattern);
                }

                for (char c : rowPattern.toCharArray()) {
                    String key = String.valueOf(c);
                    if (key.equals(" ")) {
                        ingredients.add(Ingredient.EMPTY);
                    } else if (keyPattern.containsKey(key)) {
                        ingredients.add(keyPattern.get(key));
                    } else {
                        throw new IllegalArgumentException("Pattern key not found in key map: " + key);
                    }
                }
            }

            if (!jsonObject.has("main_material")) {
                throw new IllegalArgumentException("Missing main_material");
            }
            ingredients.add(ingredientFromJson(ops, jsonObject.get("main_material")));

            if (!jsonObject.has("result") || !jsonObject.get("result").isJsonObject()) {
                throw new IllegalArgumentException("Missing or invalid result");
            }

            JsonObject resultObject = jsonObject.getAsJsonObject("result");
            ItemStack itemStack = parseItemStack(resultObject);

            if (itemStack.isEmpty()) {
                throw new IllegalArgumentException("Result ItemStack cannot be empty");
            }

            String signature = generateRecipeSignature(ingredients, jsonObject);
            if (trueRegRecipe.containsKey(signature)) {
                ResourceLocation existingRecipe = trueRegRecipe.get(signature);
                //由猫猫修复 给猫猫买颗猫薄荷叭ovo
                //防粗心Xiris喵
                ChineseweaponsMod.LOGGER.error("Duplicate recipe detected: " + existingRecipe + " <--> " + "same ingredients but different output");
            } else {
                trueRegRecipe.put(signature, PLACEHOLDER_ID);
            }

            return new WCTRecipe(PLACEHOLDER_ID, ingredients, itemStack);
        } catch (Exception e) {
            ChineseweaponsMod.LOGGER.error("weapon casting recipe is error: " + e.getMessage());
            NonNullList<Ingredient> emptyIngredients = NonNullList.withSize(10, Ingredient.EMPTY);
            return new WCTRecipe(PLACEHOLDER_ID, emptyIngredients, ItemStack.EMPTY);
        }
    }

    private static ItemStack parseItemStack(JsonObject jsonObject) {
        if (!jsonObject.has("item")) {
            throw new IllegalArgumentException("Result must have 'item' field");
        }

        String itemName = jsonObject.get("item").getAsString();
        ResourceLocation itemId = ResourceLocation.tryParse(itemName);
        if (itemId == null) {
            throw new IllegalArgumentException("Invalid item name: " + itemName);
        }

        var item = BuiltInRegistries.ITEM.get(itemId);
        if (item == null || item == Items.AIR) {
            throw new IllegalArgumentException("Item not found: " + itemName);
        }

        int count = 1;
        if (jsonObject.has("count")) {
            count = jsonObject.get("count").getAsInt();
        }

        return new ItemStack(item, count);
    }

    private static JsonObject encode(WCTRecipe recipe) {
        JsonObject json = new JsonObject();
        JsonArray pattern = new JsonArray();
        JsonObject key = new JsonObject();
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        char[] keys = "ABCDEFGHI".toCharArray();

        for (int row = 0; row < 3; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < 3; col++) {
                Ingredient ing = ingredients.size() > row * 3 + col ? ingredients.get(row * 3 + col) : Ingredient.EMPTY;
                if (ing.isEmpty()) {
                    sb.append(' ');
                } else {
                    sb.append(keys[row * 3 + col]);
                    key.add(String.valueOf(keys[row * 3 + col]), ingredientToJson(ing));
                }
            }
            pattern.add(sb.toString());
        }

        json.add("pattern", pattern);
        if (!key.entrySet().isEmpty()) {
            json.add("key", key);
        }

        if (ingredients.size() > 9) {
            json.add("main_material", ingredientToJson(ingredients.get(9)));
        }

        ItemStack result = recipe.getResultItem2();
        if (!result.isEmpty()) {
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
            resultJson.addProperty("count", result.getCount());
            json.add("result", resultJson);
        }

        return json;
    }

    private static String generateRecipeSignature(NonNullList<Ingredient> ingredients, JsonObject jsonObject) {
        StringBuilder sb = new StringBuilder();

        if (jsonObject.has("pattern") && jsonObject.get("pattern").isJsonArray()) {
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("pattern").asList()) {
                char[] patternChars = jsonElement.getAsString().toCharArray();
                for (int j = 0; j < 3; j++) {
                    String key = String.valueOf(patternChars[j]);
                    if (key.equals(" ")) {
                        sb.append("air|");
                    } else if (jsonObject.has("key") && jsonObject.getAsJsonObject("key").has(key)) {
                        JsonElement keyJson = jsonObject.getAsJsonObject("key").get(key);
                        sb.append(getIngredientSignatureFromJson(keyJson)).append("|");
                    } else {
                        sb.append("null|");
                    }
                }
            }
        }

        if (jsonObject.has("main_material")) {
            JsonElement mainMaterialJson = jsonObject.get("main_material");
            sb.append(getIngredientSignatureFromJson(mainMaterialJson));
        }
        return sb.toString();
    }

    private static String getIngredientSignatureFromJson(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return "null";
        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            if (jsonObj.has("tag")) {
                return "tag:" + jsonObj.get("tag").getAsString();
            }
            if (jsonObj.has("item")) {
                return "item:" + jsonObj.get("item").getAsString();
            }
        }
        return "json:" + jsonElement;
    }

    private static JsonElement ingredientToJson(Ingredient ingredient) {
        return Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient)
                .result()
                .orElse(JsonNull.INSTANCE);
    }

    private static Ingredient ingredientFromJson(DynamicOps<JsonElement> ops, JsonElement element) {
        return Ingredient.CODEC.parse(ops, element)
                .result()
                .orElse(Ingredient.EMPTY);
    }

    @SuppressWarnings("unchecked")
    private static DynamicOps<JsonElement> castOps(DynamicOps<?> ops) {
        return (DynamicOps<JsonElement>) ops;
    }

    public static void clearDuplicateCache() {
        trueRegRecipe.clear();
    }
}