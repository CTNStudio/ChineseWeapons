package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.jei.WeaponsCastingTableRecipeJei;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

import java.util.List;

@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(ChineseweaponsMod.MODID, "jei_plugin");
    public static final RecipeType<WCTRecipe> RECIPE_TYPE = RecipeType.create(
            ChineseweaponsMod.MODID,
            "weapon_casting_shaped",
            WCTRecipe.class
    );

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponsCastingTableRecipeJei(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().getConnection().getRecipeManager();
        List<WCTRecipe> recipes = recipeManager.getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get());
        registration.addRecipes(RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()), RECIPE_TYPE);
    }
}