package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.rules.WeaponRules;

import java.util.UUID;

public class GlaiveItemStopRunProcedure {

    public static void execute(Entity target, Entity source, ItemStack itemstack) {
        WeaponRules.stopRun(target, source, itemstack);
    }

    public static float getAnimationProgress(UUID uid) {
        return WeaponRules.stopRunAnimationProgress(uid);
    }
}
