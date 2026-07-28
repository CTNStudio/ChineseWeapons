package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

import java.util.List;


@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(ChineseweaponsMod.MODID, "jei_plugin");
    public static final RecipeType<WCTRecipe> recipe = RecipeType.create(ChineseweaponsMod.MODID,"jei.".concat(ChineseweaponsMod.MODID).concat(".category.weapons_casting_table_shaped"), WCTRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration){
        List<WCTRecipe> recipes = WeaponsCastingTableTypeGuiMenu.recipeManager.getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get());
        registration.addRecipes(recipe,recipes);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg){
        reg.addRecipeCatalyst(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()),recipe);
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponsCastingTableRecipeJei(registration.getJeiHelpers().getGuiHelper()));
    }
}
