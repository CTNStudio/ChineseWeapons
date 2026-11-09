package net.mirrorloong.chineseweapons.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;

@EmiEntrypoint
public final class ChineseWeaponsEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory WEAPON_CASTING = new EmiRecipeCategory(
            new ResourceLocation(ChineseWeapons.MOD_ID, "weapon_casting_shaped"),
            EmiStack.of(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WEAPON_CASTING);
        registry.addWorkstation(WEAPON_CASTING, EmiStack.of(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()));
        registry.getRecipeManager()
                .getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get())
                .stream()
                .map(WeaponCastingEmiRecipe::new)
                .forEach(registry::addRecipe);
    }
}
