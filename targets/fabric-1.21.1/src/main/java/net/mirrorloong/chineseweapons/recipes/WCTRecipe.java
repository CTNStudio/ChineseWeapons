package net.mirrorloong.chineseweapons.recipes;

import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class WCTRecipe implements Recipe<WCTRecipeInput> {
    /** Accepts the legacy {@code {"item": ..., "count": ...}} result syntax used by the mod's data. */
    private static final Codec<ItemStack> RESULT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStack::getItem),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
    ).apply(instance, ItemStack::new));

    public static final MapCodec<WCTRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(recipe -> recipe.pattern),
            Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(recipe -> recipe.key),
            Ingredient.CODEC.fieldOf("main_material").forGetter(recipe -> recipe.mainMaterial),
            RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output)
    ).apply(instance, WCTRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> STREAM_CODEC = StreamCodec.of(
            (buffer, recipe) -> {
                buffer.writeVarInt(recipe.recipe.rawIngredients().size());
                for (Ingredient ingredient : recipe.recipe.rawIngredients()) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                }
                ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            },
            buffer -> {
                int size = buffer.readVarInt();
                NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
                for (int i = 0; i < size; i++) {
                    ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                }
                return new WCTRecipe(ingredients, ItemStack.STREAM_CODEC.decode(buffer));
            });

    private final List<String> pattern;
    private final Map<String, Ingredient> key;
    private final Ingredient mainMaterial;
    private final ItemStack output;
    private final CastingRecipe recipe;

    public WCTRecipe(List<String> pattern, Map<String, Ingredient> key, Ingredient mainMaterial, ItemStack output) {
        this.pattern = pattern;
        this.key = key;
        this.mainMaterial = mainMaterial;
        this.output = output;
        this.recipe = new CastingRecipe(CastingRecipe.paddedIngredients(pattern, key, mainMaterial), output);
    }

    private WCTRecipe(NonNullList<Ingredient> input, ItemStack output) {
        this.pattern = List.of();
        this.key = Map.of();
        this.mainMaterial = Ingredient.EMPTY;
        this.output = output;
        this.recipe = new CastingRecipe(input, output);
    }

    @Override
    public boolean matches(WCTRecipeInput inv, Level level) {
        return recipe.matches(inv::getItem);
    }

    @Override
    public ItemStack assemble(WCTRecipeInput inv, HolderLookup.Provider registries) {
        return recipe.assemble();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return recipe.result();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipe.ingredients();
    }

    public ItemStack getResultItem2() {
        return recipe.result();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DeferredRecipe.WeaponsCastingShapedSerType.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DeferredRecipe.WeaponCastingShapedType.get();
    }
}
