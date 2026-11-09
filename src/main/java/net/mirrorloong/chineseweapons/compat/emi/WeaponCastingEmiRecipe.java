package net.mirrorloong.chineseweapons.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

import java.util.List;

public final class WeaponCastingEmiRecipe extends BasicEmiRecipe {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(
            ChineseweaponsMod.MODID,
            "textures/gui/jei/weapons_casting_table.png"
    );
    private static final int WIDTH = 157;
    private static final int HEIGHT = 62;

    private final List<EmiIngredient> ingredients;
    private final EmiStack result;

    public WeaponCastingEmiRecipe(WCTRecipe recipe) {
        super(ChineseWeaponsEmiPlugin.WEAPON_CASTING, recipe.getId(), WIDTH, HEIGHT);
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
