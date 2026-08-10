package net.mirrorloong.chineseweapons.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public final class ChineseWeaponsKubeJSBindings {
    private ChineseWeaponsKubeJSBindings() {
    }

    public static void setReachBonus(String itemId, double bonus) {
        WeaponScriptSettings.setReachBonus(parseItemId(itemId), bonus);
    }

    public static void setHookDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HOOK_DISMOUNT, chance);
    }

    public static void setHitEffectChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HIT_EFFECT, chance);
    }

    public static void setGlaiveDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, chance);
    }

    private static ResourceLocation parseItemId(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid item id: " + itemId);
        }
        return id;
    }
}
