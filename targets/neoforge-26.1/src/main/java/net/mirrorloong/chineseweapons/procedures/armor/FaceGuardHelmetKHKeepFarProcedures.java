package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FaceGuardHelmetKHKeepFarProcedures {
    private static final Map<Monster, Long> FEAR_MAP = new ConcurrentHashMap<>();
    public static void addFar(Monster monster, long until) {
        FEAR_MAP.put(monster, until);
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Monster monster)) {
            return;
        }

        if (monster.level().isClientSide()) {
            return;
        }

        if (!FEAR_MAP.containsKey(monster)) {
            return;
        }

        long now = monster.level().getGameTime();
        long fearUntil = FEAR_MAP.get(monster);

        if (now >= fearUntil) {
            FEAR_MAP.remove(monster);
            return;
        }

        if (monster.getTarget() instanceof Player) {
            monster.setTarget(null);
            monster.setLastHurtByMob(null);
            monster.setLastHurtByPlayer((net.minecraft.world.entity.player.Player) null, 0);
        }

        if (!monster.isAlive()) {
            FEAR_MAP.remove(monster);
        }
    }
}