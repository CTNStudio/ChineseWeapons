package net.mirrorloong.chineseweapons.compat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.rules.ShieldRules;

/**
 * Shared safe-zone test for the Fabric explosion hooks. Forge receives the
 * same decision through {@code ExplosionEvent.Detonate}.
 */
public final class StandingShieldExplosionProtection {
    private static final ThreadLocal<Vec3> CURRENT_EXPLOSION = new ThreadLocal<>();

    private StandingShieldExplosionProtection() {
    }

    public static void push(Vec3 center) {
        CURRENT_EXPLOSION.set(center);
    }

    public static void pop() {
        CURRENT_EXPLOSION.remove();
    }

    /** Checks the entity against the explosion currently running on this thread. */
    public static boolean isProtectedByCurrentExplosion(Entity entity) {
        Vec3 center = CURRENT_EXPLOSION.get();
        return center != null && isProtected(entity, center);
    }

    public static boolean isProtected(Entity entity, Vec3 center) {
        Level level = entity.level();
        if (level.isClientSide()) {
            return false;
        }

        AABB search = new AABB(center, center).inflate(32.0D);
        for (SongStandingShieldEntity shield
                : level.getEntitiesOfClass(SongStandingShieldEntity.class, search)) {
            if (ShieldRules.isInFront(shield.position(), shield.getShieldYaw(), center)
                    && shield.getExplosionSafeZone().contains(entity.position())) {
                return true;
            }
        }
        return false;
    }
}
