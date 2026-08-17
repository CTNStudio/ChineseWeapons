package net.mirrorloong.chineseweapons.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public final class ChineseWeaponsKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerClasses(ClassFilter filter) {
        filter.allow("net.mirrorloong.chineseweapons.compat.kubejs");
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("ChineseWeapons", ChineseWeaponsKubeJSBindings.class);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace("chineseweapons")
                .register("weapon_casting_shaped", WeaponCastingRecipeSchema.SCHEMA);
    }
}
