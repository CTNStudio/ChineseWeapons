package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.rules.WeaponRules;

public class DaggerAxeAndGlaiveItemjizhongProcedure {

    private static final float DEFAULT_HIT_EFFECT_CHANCE = 0.80F;

    public static void execute(Entity entity, ItemStack weapon) {
        float chance = WeaponScriptSettings.getChance(
                weapon.getItem(),
                WeaponScriptSettings.Skill.HIT_EFFECT,
                DEFAULT_HIT_EFFECT_CHANCE
        );

        WeaponRules.applyHitEffect(entity, weapon, chance);
    }
}
