package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
    @Override
    @NotNull
    public RecipeType<WCTRecipe> getRecipeType() {
        return WeaponsCastingTableRecipeJeiPlugin.recipe;
    }

    private final IDrawable icon;
    public WeaponsCastingTableRecipeJei(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()));
    }
    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("jei.".concat(ChineseweaponsMod.MODID).concat(".category.weapons_casting_table_shaped"));
    }
    private static final ResourceLocation background = new ResourceLocation(ChineseweaponsMod.MODID,"textures/gui/jei/weapons_casting_table.png");
    @Override
    @NotNull
    public IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 157;
            }

            @Override
            public int getHeight() {
                return 62;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int i, int i1) {
                guiGraphics.blit(background,0,0,0,0,157,62,157,62);
            }
        };
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, @NotNull WCTRecipe wctRecipe, @NotNull IFocusGroup iFocusGroup) {
        int i=0;
        for(int x=0;x<=36;x+=18){
            for(int y=0;y<=36;y+=18){
                iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,y+3,x+3).addIngredients(wctRecipe.getIngredients().get(i));
                ++i;
            }
        }
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,74,39).addIngredients(wctRecipe.getIngredients().get(9));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 120,21).addItemStack(wctRecipe.getResultItem2());
    }
}
