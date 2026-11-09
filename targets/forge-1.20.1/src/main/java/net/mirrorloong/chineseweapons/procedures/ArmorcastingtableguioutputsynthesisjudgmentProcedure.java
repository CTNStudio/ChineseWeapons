package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.world.entity.Entity;
import net.mirrorloong.chineseweapons.rules.CastingRules;

public class ArmorcastingtableguioutputsynthesisjudgmentProcedure {
    public static void execute(Entity entity) {
        CastingRules.consumeInputs(entity);
    }
}
