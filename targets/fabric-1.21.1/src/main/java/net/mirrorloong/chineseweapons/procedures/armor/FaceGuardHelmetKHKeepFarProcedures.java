package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.monster.Monster;
import net.mirrorloong.chineseweapons.rules.FaceGuardRules;

/** Fabric has no entity tick event, so the fear timer advances from the world tick. */
public class FaceGuardHelmetKHKeepFarProcedures {

    public static void addFar(Monster monster, long until) {
        FaceGuardRules.addFar(monster, until);
    }

    public static void tick() {
        FaceGuardRules.tickAll();
    }
}
