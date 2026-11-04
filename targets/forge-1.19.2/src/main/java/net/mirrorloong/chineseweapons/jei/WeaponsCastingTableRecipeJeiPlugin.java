package net.mirrorloong.chineseweapons.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

import java.util.List;

@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(ChineseWeapons.MOD_ID, "jei_plugin");
    public static final RecipeType<WCTRecipe> RECIPE_TYPE = RecipeType.create(
            ChineseWeapons.MOD_ID,
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

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                WeaponsCastingTableTypeGuiMenu.class,
                DeferredGui.REG_MENU.get(),
                RECIPE_TYPE,
                0, 10,
                11, 36
        );
    }
}
