package net.mirrorloong.chineseweapons.jei;

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
import net.minecraft.world.item.crafting.RecipeMap;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

/**
 * JEI plugin for the casting table. On Fabric JEI discovers plugins through the
 * {@code jei_mod_plugin} entrypoint, which is declared in {@code fabric.mod.json};
 * the {@link JeiPlugin} annotation is kept for tooling and loader parity.
 *
 * <p>26.1 no longer syncs every recipe to the client {@code RecipeManager}, so
 * the recipe list comes from the recipe map JEI syncs itself
 * ({@link Internal#getClientSyncedRecipes()}). There is no public API for it yet.</p>
 */
@JeiPlugin
public class WeaponsCastingTableRecipeJeiPlugin implements IModPlugin {
    private static final Identifier PLUGIN_ID =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "jei_plugin");

    public static final IRecipeHolderType<WCTRecipe> RECIPE_TYPE =
            IRecipeHolderType.create(DeferredRecipe.WeaponCastingShapedType.get());

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
        RecipeMap recipes = Internal.getClientSyncedRecipes();
        List<RecipeHolder<WCTRecipe>> castingRecipes =
                List.copyOf(recipes.byType(DeferredRecipe.WeaponCastingShapedType.get()));
        registration.addRecipes(RECIPE_TYPE, castingRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RECIPE_TYPE, ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // Menu slots: 0-8 input grid, 9 extra input, 10 output, 11-46 player inventory.
        registration.addRecipeTransferHandler(
                WeaponsCastingTableTypeGuiMenu.class,
                DeferredGui.REG_MENU.get(),
                RECIPE_TYPE,
                0, 10,
                11, 36
        );
    }
}
