package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.rules.WeaponRules;

public class DaggerAxeHurtFiyyProcedure {

    public static void execute(LivingEntity attacker, LivingEntity victim) {
        WeaponRules.airSlash(attacker, victim);
    }
}
