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
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.HashMap;

public class WCTRecipeSer implements RecipeSerializer<WCTRecipe> {
    @Override
    public WCTRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        try{
            Map<String, String> Pattern = new HashMap<String, String>();
            NonNullList<Ingredient> ingredients2 = NonNullList.create();
            Map<String, Ingredient> KeyPattern = new HashMap<String, Ingredient>();
            int i = 0;
            KeyPattern.put(" ", Ingredient.of(Items.AIR));
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("pattern").asList()) {
                char[] pattern = jsonElement.getAsString().toCharArray();
                for (int j = 0; j < 3; j++) {
                    Pattern.put(String.valueOf(++i), String.valueOf(pattern[j]));
                }
            }
            for (String jsonElement : jsonObject.getAsJsonObject("key").keySet()) {
                KeyPattern.put(jsonElement, Ingredient.fromJson(jsonObject.getAsJsonObject("key").get(jsonElement)));
            }
            for (int l = 1; l <= 9; ++l) {
                ingredients2.add(KeyPattern.get(Pattern.get(String.valueOf(l))));

            }
            ingredients2.add(Ingredient.fromJson(jsonObject.get("main_material")));
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result"));
            return new WCTRecipe(resourceLocation, ingredients2, itemStack);

        } catch (Exception e) {
            ChineseweaponsMod.LOGGER.error(resourceLocation.toDebugFileName().concat(" recipe is error"));
        }
        return null;
    }

    @Override
    public @Nullable WCTRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int IngredientSize =buf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(IngredientSize,Ingredient.EMPTY);
        for (int i=0;i<IngredientSize;++i) Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        return new WCTRecipe(id, ingredients, result);
    }
    @Override
    public void toNetwork(FriendlyByteBuf buf, WCTRecipe wctRecipe) {
        buf.writeVarInt(wctRecipe.getIngredients().size());
        for(Ingredient ingredient: wctRecipe.getIngredients()) ingredient.toNetwork(buf);
        buf.writeItemStack(wctRecipe.getResultItem2(),false);
    }

}
