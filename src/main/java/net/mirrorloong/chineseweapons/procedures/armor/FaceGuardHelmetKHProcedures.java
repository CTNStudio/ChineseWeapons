package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FaceGuardHelmetKHProcedures {

    private static final Map<UUID, Long> COOLDOWN_MAP = new HashMap<>();

    private static final long COOLDOWN_TICKS = 60L;

    public static void execute(Level level, Entity entity, ItemStack stack) {
        if (level.isClientSide()) {
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

        stack.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));

        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.RAID_HORN.get(),
                SoundSource.PLAYERS,
                3.0F,//音量
                1.0F
        );

        Vec3 lookVec = player.getViewVector(1.0F);

        for (Monster monster : monsters) {
            Vec3 toMonster = monster.position()
                    .subtract(player.getEyePosition())
                    .normalize();
            double dot = lookVec.dot(toMonster);

            boolean isLookingAt = dot > 0.85;

            float chance = isLookingAt ? 0.8F : 0.5F;

            if (level.random.nextFloat() < chance) {
                monster.setTarget(null);
                monster.setLastHurtByMob(null);
                monster.setLastHurtByPlayer(null);

                FaceGuardHelmetKHKeepFarProcedures.addFar(monster, level.getGameTime() + 60);

                double QZ = WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(),
                        WeaponScriptSettings.ALLWeapon.FACEGUARDHELMET_KH, 25.0);

                Vec3 fleeDir = monster.position()
                        .subtract(player.position())
                        .normalize();
                Vec3 fleeTarget = player.position().add(fleeDir.scale(QZ));

                // 驱逐
                monster.getNavigation().moveTo(
                        fleeTarget.x, fleeTarget.y, fleeTarget.z, 1.5
                );
            }
        }
    }
}
