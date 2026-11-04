package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fabric has no per-entity tick event, so the fear timer is advanced from the
 * server level tick instead of Forge's {@code LivingEvent.LivingTickEvent}.
 */
public class FaceGuardHelmetKHKeepFarProcedures {
    private static final Map<Monster, Long> FEAR_MAP = new ConcurrentHashMap<>();

    public static void addFar(Monster monster, long until) {
        FEAR_MAP.put(monster, until);
    }

    public static void tick() {
        if (FEAR_MAP.isEmpty()) {
            return;
        }

        for (Iterator<Map.Entry<Monster, Long>> iterator = FEAR_MAP.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Monster, Long> entry = iterator.next();
            Monster monster = entry.getKey();

            if (!monster.isAlive()) {
                iterator.remove();
                continue;
            }

            if (monster.level().isClientSide()) {
                continue;
            }

            long now = monster.level().getGameTime();
            if (now >= entry.getValue()) {
                iterator.remove();
                continue;
            }

            if (monster.getTarget() instanceof Player) {
                monster.setTarget(null);
                monster.setLastHurtByMob(null);
                monster.setLastHurtByPlayer(null);
            }
        }
    }
}
