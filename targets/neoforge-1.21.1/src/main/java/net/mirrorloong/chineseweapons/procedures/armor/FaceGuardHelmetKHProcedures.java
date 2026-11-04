package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.rules.FaceGuardRules;

/** Adapter for the face-guard helmet shout; the script distance is resolved here. */
public class FaceGuardHelmetKHProcedures {

    private static final double DEFAULT_FLEE_DISTANCE = 25.0D;

    public static void execute(Level level, Entity entity, ItemStack stack) {
        double fleeDistance = WeaponScriptSettings.getArmorThreshold(
                stack.getItem(),
                WeaponScriptSettings.ALLWeapon.FACEGUARDHELMET_KH,
                DEFAULT_FLEE_DISTANCE);

        FaceGuardRules.execute(level, entity, stack, fleeDistance);
    }
}
