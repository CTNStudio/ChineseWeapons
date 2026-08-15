package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

public class DaggerAxeItemFierceHookProcedure {
    private static final double PULL_STOP_DISTANCE = 2.0D;
    private static final double MAX_PULL_DISTANCE_PER_USE = 4.0D;
    private static final double SAFE_DESTINATION_STEP = 0.25D;
    private static final int SAFE_DESTINATION_ATTEMPTS = 12;
    private static final int HOOK_COOLDOWN_TICKS = 10;
    private static final float DEFAULT_DISMOUNT_CHANCE = 0.30F;

    private static void spawnAndMoveCollision(LevelAccessor world, double startX, double startY, double startZ, Entity sourceEntity, ItemStack itemstack) {
        if (world.isClientSide()) return;
        if (sourceEntity instanceof Player player && player.getCooldowns().isOnCooldown(itemstack.getItem())) return;

        Vec3 direction = sourceEntity.getLookAngle().normalize();

        for (int distance = 1; distance <= 7; distance++) {
            double currentX = startX + direction.x * distance;
            double currentY = startY + direction.y * distance;
            double currentZ = startZ + direction.z * distance;
            AABB collisionBox = new AABB(
                    currentX - 0.5D, currentY - 0.5D, currentZ - 0.5D,
                    currentX + 0.5D, currentY + 0.5D, currentZ + 0.5D
            );
            List<Entity> hitEntities = world.getEntitiesOfClass(
                    Entity.class,
                    collisionBox,
                    candidate -> candidate != sourceEntity && candidate.isAlive()
            );

            if (!hitEntities.isEmpty()) {
                LivingEntity target = resolveHookTarget(hitEntities);
                if (target == null) {
                    continue;
                }
                boolean hookTriggered = executeOriginalLogic(world, sourceEntity, itemstack, target);
                if (hookTriggered && sourceEntity instanceof Player player) {
                    player.getCooldowns().addCooldown(itemstack.getItem(), HOOK_COOLDOWN_TICKS);
                }
                break;
            }
        }
    }

    private static LivingEntity resolveHookTarget(List<Entity> hitEntities) {
        for (Entity candidate : hitEntities) {
            if (candidate instanceof LivingEntity living && living.getVehicle() != null) {
                return living;
            }
        }

        // Mount hitboxes can hide their riders from the sampled collision box.
        // Check direct and indirect passengers before falling back to the hit mob.
        for (Entity candidate : hitEntities) {
            for (Entity passenger : candidate.getIndirectPassengers()) {
                if (passenger instanceof LivingEntity living && living.getVehicle() != null) {
                    return living;
                }
            }
            for (Entity passenger : candidate.getPassengers()) {
                if (passenger instanceof LivingEntity living) {
                    return living;
                }
            }
        }

        for (Entity candidate : hitEntities) {
            if (candidate instanceof LivingEntity living) {
                return living;
            }
        }

        return null;
    }

    private static boolean dismount(LivingEntity target) {
        if (target.getVehicle() == null) {
            return false;
        }

        target.stopRiding();
        // Keep this explicit for entities whose vehicle implementation delays
        // passenger removal until the next tick.
        target.removeVehicle();
        return target.getVehicle() == null;
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if (entity == null) return;

        spawnAndMoveCollision(world, x, y, z, entity, itemstack);
    }

    private static boolean executeOriginalLogic(LevelAccessor world, Entity sourceEntity, ItemStack itemstack, LivingEntity targetEntity) {
        boolean damaged = teleportAndDamage(world, sourceEntity, targetEntity, itemstack);
        if (!damaged) {
            return false;
        }

        // The hook always pulls and damages first; only a successful hit can
        // trigger the rider dismount effect, and that effect remains 30%.
        float chance = WeaponScriptSettings.getChance(itemstack.getItem(), WeaponScriptSettings.Skill.HOOK_DISMOUNT, DEFAULT_DISMOUNT_CHANCE);
        if (targetEntity.getVehicle() != null && world.getRandom().nextFloat() < chance) {
            dismount(targetEntity);
        }

        return true;
    }

    private static boolean teleportAndDamage(LevelAccessor world, Entity sourceEntity, Entity target, ItemStack itemstack) {
        Vec3 safeDestination = findSafeDestination(world, target, findPullDestination(sourceEntity, target));
        if (safeDestination != null) {
            target.teleportTo(safeDestination.x, safeDestination.y, safeDestination.z);
            target.setDeltaMovement(Vec3.ZERO);
            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.teleport(safeDestination.x, safeDestination.y, safeDestination.z, target.getYRot(), target.getXRot());
            }
        }

        boolean damaged = target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), 2);

        if (damaged && world instanceof ServerLevel serverLevel && sourceEntity instanceof LivingEntity living) {
            itemstack.hurtAndBreak(1, serverLevel, living, item -> {
            });
            if (itemstack.isEmpty()) {
                itemstack.shrink(1);
                itemstack.setDamageValue(0);
            }
        }

        return damaged;
    }

    private static Vec3 findPullDestination(Entity sourceEntity, Entity target) {
        Vec3 horizontalOffset = target.position().subtract(sourceEntity.position()).multiply(1.0D, 0.0D, 1.0D);
        double horizontalDistance = horizontalOffset.length();
        if (horizontalDistance <= PULL_STOP_DISTANCE || horizontalDistance < 1.0E-4D) {
            return target.position();
        }

        double pullDistance = Math.min(MAX_PULL_DISTANCE_PER_USE, horizontalDistance - PULL_STOP_DISTANCE);
        return target.position().subtract(horizontalOffset.scale(pullDistance / horizontalDistance));
    }

    private static Vec3 findSafeDestination(LevelAccessor world, Entity target, Vec3 hitPosition) {
        for (int attempt = 0; attempt <= SAFE_DESTINATION_ATTEMPTS; attempt++) {
            Vec3 candidate = new Vec3(hitPosition.x, target.getY() + attempt * SAFE_DESTINATION_STEP, hitPosition.z);
            AABB movedBounds = target.getBoundingBox().move(candidate.subtract(target.position()));
            if (world.noCollision(target, movedBounds)) {
                return candidate;
            }
        }

        return null;
    }
}
