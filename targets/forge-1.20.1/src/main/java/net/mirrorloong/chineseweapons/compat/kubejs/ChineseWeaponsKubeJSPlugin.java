package net.mirrorloong.chineseweapons.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public final class ChineseWeaponsKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("net.mirrorloong.chineseweapons.compat.kubejs");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ChineseWeapons", ChineseWeaponsKubeJSBindings.class);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace("chineseweapons")
                .register("weapon_casting_shaped", WeaponCastingRecipeSchema.SCHEMA);
    }
}
