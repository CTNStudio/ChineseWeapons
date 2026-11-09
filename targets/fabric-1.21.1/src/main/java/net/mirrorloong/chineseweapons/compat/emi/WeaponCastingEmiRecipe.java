package net.mirrorloong.chineseweapons.compat.emi;

import java.util.List;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * EMI display of one {@code chineseweapons:weapon_casting_shaped} recipe.
 *
 * <p>1.21.1 no longer exposes an id on {@code Recipe}, so the id comes from the
 * {@link RecipeHolder} that owns the recipe.</p>
 */
public final class WeaponCastingEmiRecipe extends BasicEmiRecipe {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            ChineseWeapons.MOD_ID,
            "textures/gui/jei/weapons_casting_table.png"
    );
    private static final int WIDTH = 157;
    private static final int HEIGHT = 62;

    private final List<EmiIngredient> ingredients;
    private final EmiStack result;

    public WeaponCastingEmiRecipe(RecipeHolder<WCTRecipe> holder) {
        super(ChineseWeaponsEmiPlugin.WEAPON_CASTING, holder.id(), WIDTH, HEIGHT);
        WCTRecipe recipe = holder.value();
        this.ingredients = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        this.result = EmiStack.of(recipe.getResultItem2());
        this.inputs.addAll(this.ingredients);
        this.outputs.add(this.result);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);

        for (int index = 0; index < Math.min(9, ingredients.size()); index++) {
            int row = index / 3;
            int column = index % 3;
            widgets.addSlot(ingredients.get(index), column * 18 + 3, row * 18 + 3).drawBack(false);
        }

        if (ingredients.size() >= 10) {
            widgets.addSlot(ingredients.get(9), 74, 39).drawBack(false);
        }

        widgets.addSlot(result, 120, 21).drawBack(false).recipeContext(this);
    }
}
