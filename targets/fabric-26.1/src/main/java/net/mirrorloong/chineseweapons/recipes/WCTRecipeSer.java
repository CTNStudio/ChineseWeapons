package net.mirrorloong.chineseweapons.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mirrorloong.chineseweapons.ChineseWeapons;

/**
 * Serializer for {@link WCTRecipe}. The JSON layout (pattern rows, a key map
 * and a separate main material) is preserved from the shared data pack.
 */
public final class WCTRecipeSer {
    public static final MapCodec<WCTRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(recipe -> List.of()),
            Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(recipe -> Map.of()),
            Ingredient.CODEC.fieldOf("main_material").forGetter(recipe -> recipe.ingredients().get(recipe.ingredients().size() - 1)),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(WCTRecipe::resultTemplate)
    ).apply(instance, WCTRecipeSer::create));

    public static final StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> STREAM_CODEC = StreamCodec.of(
            (buffer, recipe) -> {
                buffer.writeVarInt(recipe.ingredients().size());
                for (Ingredient ingredient : recipe.ingredients()) {
                    buffer.writeBoolean(ingredient != null);
                    if (ingredient != null) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                    }
                }
                ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.resultTemplate());
            },
            buffer -> {
                int size = buffer.readVarInt();
                List<Ingredient> ingredients = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    ingredients.add(buffer.readBoolean() ? Ingredient.CONTENTS_STREAM_CODEC.decode(buffer) : null);
                }
                return new WCTRecipe(ingredients, ItemStackTemplate.STREAM_CODEC.decode(buffer));
            });

    public static final RecipeSerializer<WCTRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private WCTRecipeSer() {
    }

    private static WCTRecipe create(List<String> pattern, Map<String, Ingredient> key, Ingredient mainMaterial,
            ItemStackTemplate result) {
        List<Ingredient> ingredients = CastingRecipe.sparseIngredients(pattern, key, mainMaterial);
        if (pattern.size() > 3) {
            ChineseWeapons.LOGGER.warn("Casting recipe pattern has more than three rows; extra rows ignored");
        }
        return new WCTRecipe(ingredients, result);
    }
}
