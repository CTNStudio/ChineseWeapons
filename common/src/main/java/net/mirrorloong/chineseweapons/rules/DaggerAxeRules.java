package net.mirrorloong.chineseweapons.rules;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.DaggerAxeItem;

import java.util.List;
import java.util.function.Predicate;

/**
 * Dagger-axe fierce hook: raycast along the look vector, pull the first hit
 * entity, damage it, roll the dismount and award the hook advancements.
 */
public final class DaggerAxeRules {

    private static final double PULL_STOP_DISTANCE = 2.0D;
    private static final double MAX_PULL_DISTANCE_PER_USE = 4.0D;
    private static final double SAFE_DESTINATION_STEP = 0.25D;
    private static final int SAFE_DESTINATION_ATTEMPTS = 12;
    private static final int HOOK_COOLDOWN_TICKS = 10;

    private DaggerAxeRules() {
    }

    public static boolean isDaggerAxe(ItemStack stack) {
        return stack.getItem() instanceof DaggerAxeItem;
    }

    /** Runs the fierce hook for a player holding a dagger-axe (shield action packet). */
    public static void hook(Player player, ItemStack itemStack) {
        float scriptChance = ChineseWeapons.gameplay().daggerAxeHookChance(itemStack);
        execute(ChineseWeapons.gameplay().level(player), player.getX(), player.getY(), player.getZ(),
                player, itemStack, scriptChance, stack -> ChineseWeapons.gameplay().armorGain(stack));
    }

    /**
     * @param scriptChance target-resolved {@code HOOK_DISMOUNT} chance
     *                     (0.30 fallback), used when no armour overrides it
     */
    public static void execute(Level world, double x, double y, double z,
                               Entity entity, ItemStack itemStack, float scriptChance,
                               Predicate<ItemStack> armorGain) {
        if (entity == null) {
            return;
        }

        if (ChineseWeapons.gameplay().isClientSide(world)) {
            return;
        }
        if (entity instanceof Player player && ChineseWeapons.gameplay().isOnCooldown(player, itemStack)) {
            return;
        }

        Vec3 direction = entity.getLookAngle().normalize();

        for (int distance = 1; distance <= 7; distance++) {
            double currentX = x + direction.x * distance;
            double currentY = y + direction.y * distance;
            double currentZ = z + direction.z * distance;
            AABB collisionBox = new AABB(
                    currentX - 0.5D, currentY - 0.5D, currentZ - 0.5D,
                    currentX + 0.5D, currentY + 0.5D, currentZ + 0.5D
            );
            List<Entity> hitEntities = world.getEntitiesOfClass(
                    Entity.class,
                    collisionBox,
                    candidate -> candidate != entity && candidate.isAlive()
            );

            if (hitEntities.isEmpty()) {
                continue;
            }

            if (entity.getVehicle() != null && hitEntities.contains(entity.getVehicle())) {
                continue;
            }

            LivingEntity target = resolveHookTarget(hitEntities);
            if (target == null) {
                continue;
            }

            boolean hookTriggered = executeHook(world, entity, itemStack, target, scriptChance, armorGain);
            if (hookTriggered && entity instanceof Player player) {
                ChineseWeapons.gameplay().addCooldown(player, itemStack, HOOK_COOLDOWN_TICKS);
            }
            break;
        }
    }

    private static LivingEntity resolveHookTarget(List<Entity> hitEntities) {
        for (Entity candidate : hitEntities) {
            if (candidate instanceof LivingEntity living && living.getVehicle() != null) {
                return living;
            }
        }

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

    private static boolean executeHook(Level world, Entity sourceEntity, ItemStack itemStack,
                                       LivingEntity targetEntity, float scriptChance,
                                       Predicate<ItemStack> armorGain) {
        boolean damaged = teleportAndDamage(world, sourceEntity, targetEntity, itemStack);
        if (!damaged) {
            return false;
        }

        float chance = dismountChance(targetEntity, scriptChance, armorGain);

        if (targetEntity.getVehicle() != null
                && ChineseWeapons.gameplay().nextFloat(world) < chance) {
            dismount(targetEntity);
        }

        if (sourceEntity instanceof ServerPlayer serverPlayer) {
            ChineseWeapons.gameplay().grantAdvancement(serverPlayer, "get_dagger_axe/dagger_axe_hook");

            if (targetEntity instanceof Creeper) {
                ChineseWeapons.gameplay().grantAdvancement(serverPlayer, "get_dagger_axe/dagger_axe_hook_creeper");
            }
        }

        return true;
    }

    private static float dismountChance(LivingEntity targetEntity, float scriptChance,
                                        Predicate<ItemStack> armorGain) {
        if (ArmorRules.isFullFootmenArmorSet(targetEntity, armorGain)) {
            return 0.10F;
        }
        if (ArmorRules.isFullArmorSet(targetEntity, ArmorRules.Family.EARLY_MING_GUANG.itemPathMarker())) {
            return 0.15F;
        }
        if (ArmorRules.isFullArmorSet(targetEntity, ArmorRules.Family.MIDDLE_MING_GUANG.itemPathMarker())) {
            return 0.15F;
        }
        if (ArmorRules.isFullArmorSet(targetEntity, ArmorRules.Family.LATE_MING_GUANG.itemPathMarker())) {
            return 0.10F;
        }
        return scriptChance;
    }

    private static boolean dismount(LivingEntity target) {
        if (target.getVehicle() == null) {
            return false;
        }

        target.stopRiding();
        target.removeVehicle();
        return target.getVehicle() == null;
    }

    private static boolean teleportAndDamage(Level world, Entity sourceEntity, LivingEntity target, ItemStack itemStack) {
        Vec3 safeDestination = findSafeDestination(world, target, findPullDestination(sourceEntity, target));
        if (safeDestination != null) {
            target.setDeltaMovement(Vec3.ZERO);
            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.teleport(safeDestination.x, safeDestination.y, safeDestination.z,
                        target.getYRot(), target.getXRot());
            } else {
                target.teleportTo(safeDestination.x, safeDestination.y, safeDestination.z);
            }
        }

        Player attacker = sourceEntity instanceof Player player ? player : null;
        boolean damaged = ChineseWeapons.gameplay().hurt(target, attacker, 2.0F);

        if (damaged && sourceEntity instanceof LivingEntity living) {
            ChineseWeapons.gameplay().damageItemRandom(itemStack, 2, living);
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

    private static Vec3 findSafeDestination(Level world, Entity target, Vec3 hitPosition) {
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
