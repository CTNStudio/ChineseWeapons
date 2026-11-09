package net.mirrorloong.chineseweapons.jei;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * Fabric JEI entrypoint. Fabric JEI finds plugins through the {@code jei_mod_plugin}
 * key of {@code fabric.mod.json}; the {@link JeiPlugin} annotation is kept so the
 * class stays a drop-in for the annotation-driven loaders.
 */
@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(
            ChineseWeapons.MOD_ID, "jei_plugin");

    public static final RecipeType<RecipeHolder<WCTRecipe>> RECIPE_TYPE =
            RecipeType.createFromVanilla(DeferredRecipe.WeaponCastingShapedType.get());

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
        if (Minecraft.getInstance().getConnection() == null) {
            return;
        }

        RecipeManager recipeManager = Minecraft.getInstance().getConnection().getRecipeManager();
        List<RecipeHolder<WCTRecipe>> recipes =
                recipeManager.getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get());
        registration.addRecipes(RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(RECIPE_TYPE, ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // Recipe slots 0-9 are the nine grid slots plus the main material; the menu
        // puts the output at slot 10 and the player inventory at 11-46.
        registration.addRecipeTransferHandler(
                WeaponsCastingTableTypeGuiMenu.class,
                DeferredGui.REG_MENU.get(),
                RECIPE_TYPE,
                0, 10,
                11, 36
        );
    }
}
