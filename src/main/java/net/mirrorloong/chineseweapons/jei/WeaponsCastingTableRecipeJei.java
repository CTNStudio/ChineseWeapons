package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeaponsCastingTableRecipeJei implements IRecipeCategory<WCTRecipe> {
    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ChineseweaponsMod.MODID, "textures/gui/jei/weapons_casting_table.png");
    public static final int WIDTH = 157;
    public static final int HEIGHT = 62;

    private final IDrawable icon;

    public WeaponsCastingTableRecipeJei(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()));
    }

    @Override
    @NotNull
    public RecipeType<WCTRecipe> getRecipeType() {
        return WeaponsCastingTableRecipeJeiPlugin.RECIPE_TYPE;
    }

    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("jei." + ChineseweaponsMod.MODID + ".category.weapons_casting_table_shaped");
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
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(@NotNull WCTRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView,
                     @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(BACKGROUND_TEXTURE, 0, 0, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull WCTRecipe recipe, @NotNull IFocusGroup focuses) {
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