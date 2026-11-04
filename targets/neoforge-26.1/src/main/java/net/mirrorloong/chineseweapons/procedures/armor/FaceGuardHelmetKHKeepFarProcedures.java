package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.mirrorloong.chineseweapons.rules.FaceGuardRules;

/** NeoForge 1.21.1 entity-tick wiring for the face-guard fear timer. */
public class FaceGuardHelmetKHKeepFarProcedures {

    public static void addFar(Monster monster, long until) {
        FaceGuardRules.addFar(monster, until);
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Monster monster) {
            FaceGuardRules.tick(monster);
        }
    }
}
