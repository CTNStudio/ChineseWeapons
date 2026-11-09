package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mirrorloong.chineseweapons.rules.FaceGuardRules;

/** Forge per-entity tick wiring for the face-guard fear timer. */
public class FaceGuardHelmetKHKeepFarProcedures {

    public static void addFar(Monster monster, long until) {
        FaceGuardRules.addFar(monster, until);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            FaceGuardRules.tick(monster);
        }
    }
}
