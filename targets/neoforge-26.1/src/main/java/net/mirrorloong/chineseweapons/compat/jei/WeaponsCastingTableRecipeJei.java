package net.mirrorloong.chineseweapons.compat.jei;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * JEI category for the armor casting table. 26.1 recipes are only available
 * through {@link RecipeHolder}, and the client no longer exposes a recipe
 * registry, so the ingredient layout is read from the holder's value.
 */
public class WeaponsCastingTableRecipeJei implements IRecipeCategory<RecipeHolder<WCTRecipe>> {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(
            ChineseWeapons.MOD_ID, "textures/gui/jei/weapons_casting_table.png");
    public static final int WIDTH = 157;
    public static final int HEIGHT = 62;

    private final IDrawable background;
    private final IDrawable icon;

    public WeaponsCastingTableRecipeJei(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemLike(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get());
    }

    @Override
    public IRecipeType<RecipeHolder<WCTRecipe>> getRecipeType() {
        return WeaponsCastingTableRecipeJeiPlugin.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + ChineseWeapons.MOD_ID + ".category.weapons_casting_table_shaped");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RecipeHolder<WCTRecipe> recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<WCTRecipe> holder, IFocusGroup focuses) {
        List<Ingredient> ingredients = holder.value().ingredients();

        // All ten input slots are created even when a pattern cell is empty:
        // JEI maps them positionally onto menu slots 0-9 during recipe transfer.
        for (int index = 0; index < 9; index++) {
            int row = index / 3;
            int column = index % 3;
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, column * 18 + 3, row * 18 + 3);
            Ingredient ingredient = index < ingredients.size() ? ingredients.get(index) : null;
            if (ingredient != null && !ingredient.isEmpty()) {
                slot.add(ingredient);
            }
        }

        IRecipeSlotBuilder mainMaterialSlot = builder.addSlot(RecipeIngredientRole.INPUT, 74, 39);
        if (ingredients.size() >= 10) {
            Ingredient mainMaterial = ingredients.get(9);
            if (mainMaterial != null && !mainMaterial.isEmpty()) {
                mainMaterialSlot.add(mainMaterial);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 21)
                .add(holder.value().getResultItem2());
    }
}
