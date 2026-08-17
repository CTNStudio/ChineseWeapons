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

    private static final MapCodec<WCTRecipe> CODEC = MapCodec.assumeMapUnsafe(
        Codec.PASSTHROUGH.xmap(
            dynamic -> decode(dynamic),
            recipe -> new Dynamic<>(JsonOps.INSTANCE, encode(recipe))
        )
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> STREAM_CODEC = StreamCodec.of(
        (buf, recipe) -> {
            buf.writeVarInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem2());
        },
        buf -> {
            int ingredientSize = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);
            for (int i = 0; i < ingredientSize; ++i) {
                ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
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
            Map<String, String> pattern = new HashMap<>();
            NonNullList<Ingredient> ingredients2 = NonNullList.create();
            Map<String, Ingredient> keyPattern = new HashMap<>();
            int i = 0;
            keyPattern.put(" ", Ingredient.of(Items.AIR));
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("pattern").asList()) {
                char[] patternChars = jsonElement.getAsString().toCharArray();
                for (int j = 0; j < 3; j++) {
                    pattern.put(String.valueOf(++i), String.valueOf(patternChars[j]));
                }
            }
            for (String key : jsonObject.getAsJsonObject("key").keySet()) {
                keyPattern.put(key, ingredientFromJson(ops, jsonObject.getAsJsonObject("key").get(key)));
            }
            for (int l = 1; l <= 9; ++l) {
                ingredients2.add(keyPattern.get(pattern.get(String.valueOf(l))));
            }
            ingredients2.add(ingredientFromJson(ops, jsonObject.get("main_material")));
            ItemStack itemStack = ItemStack.STRICT_CODEC.parse(ops, jsonObject.getAsJsonObject("result")).result().orElse(ItemStack.EMPTY);
            return new WCTRecipe(PLACEHOLDER_ID, ingredients2, itemStack);
        } catch (Exception e) {
            ChineseweaponsMod.LOGGER.error("weapon casting recipe is error");
            return new WCTRecipe(PLACEHOLDER_ID, NonNullList.withSize(10, Ingredient.EMPTY), ItemStack.EMPTY);
        }
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
        json.add("result", ItemStack.STRICT_CODEC.encodeStart(JsonOps.INSTANCE, recipe.getResultItem2()).result().orElse(JsonNull.INSTANCE));
        return json;
    }

    private static JsonElement ingredientToJson(Ingredient ingredient) {
        return Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).result().orElse(JsonNull.INSTANCE);
    }

    private static Ingredient ingredientFromJson(DynamicOps<JsonElement> ops, JsonElement element) {
        return Ingredient.CODEC.parse(ops, element).result().orElse(Ingredient.EMPTY);
    }

    @SuppressWarnings("unchecked")
    private static DynamicOps<JsonElement> castOps(DynamicOps<?> ops) {
        return (DynamicOps<JsonElement>) ops;
    }
}
