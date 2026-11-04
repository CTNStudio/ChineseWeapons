package net.mirrorloong.chineseweapons.recipes;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

/**
 * Casting table recipe. 26.1 recipes are codec driven, so the legacy
 * pattern/key/main_material JSON is parsed through the codec in
 * {@link WCTRecipeSer} instead of a hand written serializer.
 */
public class WCTRecipe implements Recipe<WCTRecipeInput> {
    private final List<Ingredient> input;
    private final ItemStackTemplate result;

    public WCTRecipe(List<Ingredient> input, ItemStackTemplate result) {
        this.input = input;
        this.result = result;
    }

    @Override
    public boolean matches(WCTRecipeInput inv, Level level) {
        for (int i = 0; i < 10; ++i) {
            Ingredient ingredient = i < input.size() ? input.get(i) : null;
            if (ingredient == null) {
                continue;
            }
            if (!ingredient.test(inv.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(WCTRecipeInput input) {
        return this.result.create();
    }

    public ItemStack getResultItem2() {
        return this.result.create();
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
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public List<Ingredient> ingredients() {
        return input;
    }

    public ItemStackTemplate resultTemplate() {
        return result;
    }
}
