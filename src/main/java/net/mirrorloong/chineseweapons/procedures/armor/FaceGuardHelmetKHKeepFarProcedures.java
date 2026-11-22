package net.mirrorloong.chineseweapons.procedures.armor;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FaceGuardHelmetKHKeepFarProcedures {
    private static final Map<Monster, Long> FEAR_MAP = new ConcurrentHashMap<>();
    private static boolean registered;

    public static void addFar(Monster monster, long until) {
        FEAR_MAP.put(monster, until);
    }

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        ServerTickEvents.END_SERVER_TICK.register(FaceGuardHelmetKHKeepFarProcedures::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        for (Monster monster : FEAR_MAP.keySet()) {
            onLivingTick(monster);
        }
    }

    public static void onLivingTick(Monster monster) {
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
            monster.setLastHurtByPlayer(null);
        }

        if (!monster.isAlive()) {
            FEAR_MAP.remove(monster);
        }
    }
}
