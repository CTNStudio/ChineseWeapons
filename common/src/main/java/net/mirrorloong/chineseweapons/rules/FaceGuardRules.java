package net.mirrorloong.chineseweapons.rules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Face-guard helmet shout: cooldown, monster selection, flee and fear timer. */
public final class FaceGuardRules {

    private static final long COOLDOWN_TICKS = 60L;
    private static final Map<UUID, Long> COOLDOWN_MAP = new HashMap<>();
    private static final Map<Monster, Long> FEAR_MAP = new ConcurrentHashMap<>();

    private FaceGuardRules() {
    }

    /**
     * @param fleeDistance result of the target's {@code FACEGUARDHELMET_KH}
     *                     script setting (25.0 by default)
     */
    public static void execute(Level level, Entity entity, ItemStack stack, double fleeDistance) {
        if (ChineseWeapons.gameplay().isClientSide(level)) {
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }

        if (!player.isCrouching()) {
            return;
        }

        UUID uuid = player.getUUID();
        long currentTick = level.getGameTime();
        Long lastTrigger = COOLDOWN_MAP.get(uuid);

        if (lastTrigger != null && currentTick - lastTrigger < COOLDOWN_TICKS) {
            return;
        }

        AABB searchBox = player.getBoundingBox().inflate(20.0);

        List<Monster> monsters = level.getEntitiesOfClass(
                Monster.class,
                searchBox,
                monster -> monster.isAlive()
                        && monster.distanceToSqr(player) >= 16.0
                        && monster.distanceToSqr(player) <= 169.0
        );

        if (monsters.isEmpty()) {
            return;
        }

        COOLDOWN_MAP.put(uuid, currentTick);

        ChineseWeapons.gameplay().damageItemInUsedHand(stack, 2, player);

        ChineseWeapons.gameplay().playSound(level, player.getX(), player.getY(), player.getZ(),
                GameplayCompat.Sound.RAID_HORN, 3.0F, 1.0F);

        Vec3 lookVec = player.getViewVector(1.0F);

        for (Monster monster : monsters) {
            Vec3 toMonster = monster.position()
                    .subtract(player.getEyePosition())
                    .normalize();
            double dot = lookVec.dot(toMonster);

            boolean isLookingAt = dot > 0.85;

            float chance = isLookingAt ? 0.8F : 0.5F;

            if (ChineseWeapons.gameplay().nextFloat(level) < chance) {
                ChineseWeapons.gameplay().clearAggro(monster);

                addFar(monster, level.getGameTime() + 60);

                Vec3 fleeDir = monster.position()
                        .subtract(player.position())
                        .normalize();
                Vec3 fleeTarget = player.position().add(fleeDir.scale(fleeDistance));

                monster.getNavigation().moveTo(fleeTarget.x, fleeTarget.y, fleeTarget.z, 1.5);
            }
        }
    }

    public static void addFar(Monster monster, long until) {
        FEAR_MAP.put(monster, until);
    }

    /** Per-entity tick used by the targets that have an entity tick event. */
    public static void tick(Monster monster) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(monster))) {
            return;
        }

        Long fearUntil = FEAR_MAP.get(monster);
        if (fearUntil == null) {
            return;
        }

        long now = ChineseWeapons.gameplay().level(monster).getGameTime();
        if (now >= fearUntil || !monster.isAlive()) {
            FEAR_MAP.remove(monster);
            return;
        }

        if (monster.getTarget() instanceof Player) {
            ChineseWeapons.gameplay().clearAggro(monster);
        }
    }

    /** Whole-map tick used by the targets without an entity tick event. */
    public static void tickAll() {
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

            if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(monster))) {
                continue;
            }

            long now = ChineseWeapons.gameplay().level(monster).getGameTime();
            if (now >= entry.getValue()) {
                iterator.remove();
                continue;
            }

            if (monster.getTarget() instanceof Player) {
                ChineseWeapons.gameplay().clearAggro(monster);
            }
        }
    }
}
