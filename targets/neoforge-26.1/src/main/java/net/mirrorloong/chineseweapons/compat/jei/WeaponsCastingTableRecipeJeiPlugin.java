package net.mirrorloong.chineseweapons.compat.jei;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.common.Internal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * Optional JEI integration for the armor casting table.
 *
 * <p>Since 26.1 the client has no recipe registry: JEI loads recipe data from
 * the client resource pack (or from the NeoForge recipe sync) and exposes it
 * through its own client recipe map. The plugin therefore reads the recipes
 * from {@link Internal#getClientSyncedRecipes()} instead of a client
 * {@code RecipeManager}, and JEI is never a runtime dependency.</p>
 */
@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final Identifier PLUGIN_ID =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "jei_plugin");

    /**
     * Uses the identifier form so the type can be created before the recipe
     * type registry is queried; the id matches the registered
     * {@code chineseweapons:weapon_casting_shaped} recipe type.
     */
    public static final IRecipeHolderType<WCTRecipe> RECIPE_TYPE =
            IRecipeHolderType.create(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "weapon_casting_shaped"));

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new WeaponsCastingTableRecipeJei(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeType<WCTRecipe> type = DeferredRecipe.WeaponCastingShapedType.get();
        List<RecipeHolder<WCTRecipe>> recipes = List.copyOf(Internal.getClientSyncedRecipes().byType(type));
        if (recipes.isEmpty()) {
            ChineseWeapons.LOGGER.warn("JEI found no {} recipes to display", type);
        }
        registration.addRecipes(RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RECIPE_TYPE, ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                WeaponsCastingTableTypeGuiMenu.class,
                DeferredGui.REG_MENU.get(),
                RECIPE_TYPE,
                0, 10,  // input slots 0-9 (3x3 grid plus main material)
                11, 36  // player inventory slots 11-46
        );
    }
}
