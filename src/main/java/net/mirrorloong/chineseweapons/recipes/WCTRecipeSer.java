package net.mirrorloong.chineseweapons.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class WCTRecipeSer implements RecipeSerializer<WCTRecipe> {
    private static final Map<String, ResourceLocation> TRUE_REG_RECIPE = new HashMap<>();

    @Override
    public WCTRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        try{
            Map<String, String> Pattern = new HashMap<String, String>();
            NonNullList<Ingredient> ingredients2 = NonNullList.create();
            Map<String, Ingredient> KeyPattern = new HashMap<String, Ingredient>();
            Map<String, JsonElement> KeyPatternJson = new HashMap<>();
            int i = 0;
            KeyPattern.put(" ", Ingredient.of(Items.AIR));
            KeyPatternJson.put(" ", new JsonObject());

            for (JsonElement jsonElement : jsonObject.getAsJsonArray("pattern").asList()) {
                char[] pattern = jsonElement.getAsString().toCharArray();
                for (int j = 0; j < 3; j++) {
                    Pattern.put(String.valueOf(++i), String.valueOf(pattern[j]));
                }
            }
            for (String jsonElement : jsonObject.getAsJsonObject("key").keySet()) {
                JsonElement keyJson = jsonObject.getAsJsonObject("key").get(jsonElement);
                KeyPattern.put(jsonElement, Ingredient.fromJson(keyJson));
                KeyPatternJson.put(jsonElement, keyJson);
            }
            for (int l = 1; l <= 9; ++l) {
                String patternKey = Pattern.get(String.valueOf(l));
                ingredients2.add(KeyPattern.get(patternKey));
            }
            ingredients2.add(Ingredient.fromJson(jsonObject.get("main_material")));
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result"));

            String signature = generateRecipeSignature(ingredients2, jsonObject);
            if (TRUE_REG_RECIPE.containsKey(signature)) {
                ResourceLocation existingRecipe = TRUE_REG_RECIPE.get(signature);
                //防粗心Xiris喵
                ChineseweaponsMod.LOGGER.error("Duplicate recipe detected: " + existingRecipe + " <--> " + resourceLocation + " - same ingredients but different output");
            } else {
                TRUE_REG_RECIPE.put(signature, resourceLocation);
            }

            return new WCTRecipe(resourceLocation, ingredients2, itemStack);

        } catch (Exception e) {
            ChineseweaponsMod.LOGGER.error(resourceLocation.toDebugFileName().concat(" recipe is error"));
        }
        return null;
    }

    private String generateRecipeSignature(NonNullList<Ingredient> ingredients, JsonObject jsonObject) {
        StringBuilder sb = new StringBuilder();

        int Index = 0;
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("pattern").asList()) {
            char[] pattern = jsonElement.getAsString().toCharArray();
            for (int j = 0; j < 3; j++) {
                String key = String.valueOf(pattern[j]);
                if (key.equals(" ")) {
                    sb.append("air|");
                } else {
                    JsonElement keyJson = jsonObject.getAsJsonObject("key").get(key);
                    sb.append(getIngredientSignatureFromJson(keyJson)).append("|");
                }
                Index++;
            }
        }

        JsonElement mainMaterialJson = jsonObject.get("main_material");
        sb.append(getIngredientSignatureFromJson(mainMaterialJson));

        return sb.toString();
    }

    private String getIngredientSignatureFromJson(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return "null";
        }

        JsonObject jsonObj = jsonElement.getAsJsonObject();
        if (jsonObj.has("tag")) {
            return "tag:" + jsonObj.get("tag").getAsString();
        }

        if (jsonObj.has("item")) {
            return "item:" + jsonObj.get("item").getAsString();
        }

        return "json:" + jsonElement;
    }

    @Override
    public @Nullable WCTRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int IngredientSize = buf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(IngredientSize, Ingredient.EMPTY);
        for (int i = 0; i < IngredientSize; ++i) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }
        ItemStack result = buf.readItem();
        return new WCTRecipe(id, ingredients, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, WCTRecipe wctRecipe) {
        buf.writeVarInt(wctRecipe.getIngredients().size());
        for(Ingredient ingredient: wctRecipe.getIngredients()) ingredient.toNetwork(buf);
        buf.writeItemStack(wctRecipe.getResultItem2(), false);
    }
}