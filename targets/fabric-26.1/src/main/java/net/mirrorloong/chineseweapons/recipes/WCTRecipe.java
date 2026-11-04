package net.mirrorloong.chineseweapons.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Casting table recipe. 26.1 recipes are codec driven: the serializer is a
 * {@link RecipeSerializer} record holding a {@link MapCodec} and a
 * {@link StreamCodec}, and the recipe must expose placement and recipe-book
 * metadata. Slot matching and ingredient layout come from the shared
 * {@link CastingRecipe}.
 */
public class WCTRecipe implements Recipe<WCTRecipeInput> {
    public static final MapCodec<WCTRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(WCTRecipe::pattern),
            Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(WCTRecipe::key),
            Ingredient.CODEC.fieldOf("main_material").forGetter(WCTRecipe::mainMaterial),
            ItemStack.CODEC.fieldOf("result").forGetter(WCTRecipe::output)
    ).apply(instance, WCTRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), WCTRecipe::ingredientList,
                    ItemStack.STREAM_CODEC, WCTRecipe::output,
                    WCTRecipe::new);

    private final List<String> pattern;
    private final Map<String, Ingredient> key;
    private final Ingredient mainMaterial;
    private final NonNullList<Ingredient> input;
    private final ItemStack output;
    private final CastingRecipe recipe;

    public WCTRecipe(List<String> pattern, Map<String, Ingredient> key, Ingredient mainMaterial, ItemStack output) {
        this.pattern = List.copyOf(pattern);
        this.key = Map.copyOf(key);
        this.mainMaterial = mainMaterial;
        this.output = output;
        this.input = NonNullList.create();
        this.input.addAll(CastingRecipe.variableWidthIngredients(this.pattern, this.key, mainMaterial));
        this.recipe = new CastingRecipe(input, output);
    }

    public WCTRecipe(List<Ingredient> ingredients, ItemStack output) {
        this.pattern = List.of();
        this.key = Map.of();
        this.mainMaterial = Ingredient.of(net.minecraft.world.item.Items.AIR);
        this.input = NonNullList.create();
        this.input.addAll(ingredients);
        this.output = output;
        this.recipe = new CastingRecipe(input, output);
    }

    public List<String> pattern() {
        return pattern;
    }

    public Map<String, Ingredient> key() {
        return key;
    }

    public Ingredient mainMaterial() {
        return mainMaterial;
    }

    public NonNullList<Ingredient> ingredientList() {
        return input;
    }

    public ItemStack output() {
        return output;
    }

    @Override
    public boolean matches(WCTRecipeInput inv, Level level) {
        return recipe.matches(inv::getItem);
    }

    @Override
    public ItemStack assemble(WCTRecipeInput inv) {
        return recipe.assemble();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<WCTRecipeInput>> getSerializer() {
        return WCTRecipeSer.SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WCTRecipeInput>> getType() {
        return DeferredRecipe.WeaponCastingShapedType.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(new ArrayList<>(input));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public ItemStack getResultItem2() {
        return recipe.result();
    }
}
