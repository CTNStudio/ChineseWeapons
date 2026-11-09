package net.mirrorloong.chineseweapons.jei;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * JEI category for {@code chineseweapons:weapon_casting_shaped}. 26.1 JEI deals
 * in {@link RecipeHolder}s and draws through {@link GuiGraphicsExtractor}; the
 * background is a drawable instead of a raw blit so the 157x62 texture is not
 * scaled against JEI's default 256x256 texture size.
 */
public class WeaponsCastingTableRecipeJei implements IRecipeCategory<RecipeHolder<WCTRecipe>> {
    public static final Identifier BACKGROUND_TEXTURE =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/gui/jei/weapons_casting_table.png");
    public static final int WIDTH = 157;
    public static final int HEIGHT = 62;

    private final IDrawable background;
    private final IDrawable icon;

    public WeaponsCastingTableRecipeJei(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()));
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<WCTRecipe> recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.value().ingredients();
        int index = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotX = col * 18 + 3;
                int slotY = row * 18 + 3;
                if (index < ingredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                            .add(ingredients.get(index));
                }
                index++;
            }
        }

        if (ingredients.size() >= 10) {
            builder.addSlot(RecipeIngredientRole.INPUT, 74, 39)
                    .add(ingredients.get(9));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 21)
                .add(recipe.value().getResultItem2());
    }
}
