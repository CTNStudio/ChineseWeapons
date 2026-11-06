package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * JEI category for {@code chineseweapons:weapon_casting_shaped}.
 *
 * <p>1.21.1 recipes reach viewers through {@link RecipeHolder}s, so the category is
 * typed on the holder and the recipe itself is read from {@link RecipeHolder#value()}.
 * The background is an {@link IDrawable} instead of a direct {@code GuiGraphics#blit}
 * call because the blit overload used by the 1.20.1 port no longer exists.</p>
 */
public class WeaponsCastingTableRecipeJei implements IRecipeCategory<RecipeHolder<WCTRecipe>> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChineseWeapons.MOD_ID, "textures/gui/jei/weapons_casting_table.png");
    private static final int WIDTH = 157;
    private static final int HEIGHT = 62;

    private final IDrawable background;
    private final IDrawable icon;

    public WeaponsCastingTableRecipeJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()));
    }

    @Override
    @NotNull
    public RecipeType<RecipeHolder<WCTRecipe>> getRecipeType() {
        return WeaponsCastingTableRecipeJeiPlugin.RECIPE_TYPE;
    }

    @Override
    @NotNull
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
    @NotNull
    public IDrawable getIcon() {
        return icon;
    }

    /**
     * JEI 19 replaced {@code getBackground()} with widgets rendered from
     * {@link IRecipeExtrasBuilder}; the texture is the same 157x62 sheet the
     * 1.20.1 port blitted directly.
     */
    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<WCTRecipe> recipe,
                                   @NotNull IRecipeSlotsView recipeSlotsView, @NotNull IFocusGroup focuses) {
        builder.addDrawable(background, 0, 0);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<WCTRecipe> holder,
                          @NotNull IFocusGroup focuses) {
        WCTRecipe recipe = holder.value();
        var ingredients = recipe.getIngredients();
        int index = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotX = col * 18 + 3;
                int slotY = row * 18 + 3;
                if (index < ingredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                            .addIngredients(ingredients.get(index));
                }
                index++;
            }
        }

        if (ingredients.size() >= 10) {
            builder.addSlot(RecipeIngredientRole.INPUT, 74, 39)
                    .addIngredients(ingredients.get(9));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 21)
                .addItemStack(recipe.getResultItem2());
    }
}
