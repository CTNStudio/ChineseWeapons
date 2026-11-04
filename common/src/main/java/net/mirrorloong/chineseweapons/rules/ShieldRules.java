package net.mirrorloong.chineseweapons.rules;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.mirrorloong.chineseweapons.spi.StandingShieldEntity;
import net.mirrorloong.chineseweapons.spi.StandingShieldItem;

import java.util.List;
import java.util.Optional;

/**
 * Song standing-shield rules shared by the item and its entity: placement
 * raycast, rotation step, plank repair, damage absorption, safety boxes and the
 * custom-name text.
 *
 * <p>Entity creation, synced data and serialisation stay in each target because
 * {@code EntityType#create} and the save format changed between versions.</p>
 */
public final class ShieldRules {

    public static final int PLACE_RANGE = 5;
    public static final double SAFE_ZONE_HALF_SIZE = 4.0D;
    public static final float REPAIR_STEP = 50.0F;
    public static final float LOW_HEALTH = 100.0F;

    private ShieldRules() {
    }

    /**
     * Raycasts the placement spot of a standing shield.
     *
     * @return the hit result, or {@code null} when the ray misses or the player
     *         may not interact with the block
     */
    public static BlockHitResult findPlacement(Player player) {
        Level level = ChineseWeapons.gameplay().level(player);
        if (ChineseWeapons.gameplay().isClientSide(level)) {
            return null;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(PLACE_RANGE));
        BlockHitResult hit = level.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (hit.getType() == HitResult.Type.MISS) {
            return null;
        }

        BlockPos pos = ChineseWeapons.gameplay().blockPos(hit.getLocation());
        if (!level.mayInteract(player, pos)) {
            return null;
        }
        return hit;
    }

    /** Initial health of a placed shield (full durability of the stored stack). */
    public static float initialHealth(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    /** Yaw of a placed shield: the player's facing plus a half turn. */
    public static float placementYaw(Player player) {
        return player.getYRot() + 180.0F;
    }

    /**
     * New health after using a plank on the shield.
     *
     * @return the health to apply, or a negative value when the stored stack
     *         cannot be repaired
     */
    public static float repairedHealth(ItemStack stored, float health) {
        float maxHealth = stored.getMaxDamage();
        if (maxHealth <= 0) {
            return -1.0F;
        }
        if (health >= maxHealth) {
            return health;
        }
        return health < REPAIR_STEP ? maxHealth : Math.min(health + REPAIR_STEP, maxHealth);
    }

    /** Health after an unbreaking-reduced hit; zero or below means the shield breaks. */
    public static float damagedHealth(float health, float amount, int unbreaking) {
        return health - amount / (unbreaking + 1);
    }

    /** Safety box in front of the shield (whichever side the shield faces). */
    public static AABB safeZone(Vec3 shieldPos, float yaw, double behindCenter) {
        Vec3 forward = forwardOf(yaw);
        return boxAround(shieldPos.add(forward.scale(behindCenter)));
    }

    /** Safety box behind the shield, used to shield explosion damage. */
    public static AABB explosionSafeZone(Vec3 shieldPos, float yaw, double behindCenter) {
        Vec3 forward = forwardOf(yaw);
        return boxAround(shieldPos.subtract(forward.scale(behindCenter)));
    }

    /** Whether an explosion centre is in front of the shield. */
    public static boolean isInFront(Vec3 shieldPos, float yaw, Vec3 explosionCenter) {
        Vec3 toExplosion = explosionCenter.subtract(shieldPos);
        if (toExplosion.lengthSqr() < 1.0E-6D) {
            return false;
        }
        return toExplosion.normalize().dot(forwardOf(yaw)) > 0.0D;
    }

    /** New yaw after a sneaking interaction, turning towards the player's side. */
    public static float rotatedYaw(Vec3 shieldPos, float yaw, Vec3 playerPos, double angle) {
        double forwardX = -Math.sin(Math.toRadians(yaw));
        double forwardZ = Math.cos(Math.toRadians(yaw));
        double rightX = -forwardZ;
        double rightZ = forwardX;

        Vec3 toPlayer = playerPos.subtract(shieldPos);
        double dot = toPlayer.x * rightX + toPlayer.z * rightZ;
        return (float) (yaw + (dot > 0 ? -angle : angle));
    }

    /** Custom name showing the remaining shield endurance. */
    public static MutableComponent healthName(float health) {
        if (health <= LOW_HEALTH) {
            return ChineseWeapons.gameplay()
                    .translatable("entity.standing_shield.hp", (int) health)
                    .withStyle(ChatFormatting.RED);
        }
        return ChineseWeapons.gameplay().translatable("entity.standing_shield.hp", (int) health);
    }

    /**
     * Finds the deployed shield under the player's crosshair within five
     * blocks, using the same ray/box test as the shield action packet always
     * used.
     */
    public static StandingShieldEntity findStandingShield(Player player) {
        Level level = ChineseWeapons.gameplay().level(player);
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double reach = 5.0D;
        Vec3 end = eye.add(look.scale(reach));

        StandingShieldEntity best = null;
        double bestDist = Double.MAX_VALUE;

        List<Entity> candidates = level.getEntitiesOfClass(
                Entity.class,
                player.getBoundingBox().inflate(reach),
                entity -> entity instanceof StandingShieldEntity);

        for (Entity entity : candidates) {
            AABB box = entity.getBoundingBox().inflate(0);
            Optional<Vec3> hit = box.clip(eye, end);
            if (hit.isEmpty()) {
                continue;
            }

            double dist = eye.distanceToSqr(hit.get());
            if (dist < bestDist) {
                bestDist = dist;
                best = (StandingShieldEntity) entity;
            }
        }
        return best;
    }

    /**
     * Picks up a deployed shield, restoring the stored item with the damage it
     * accumulated while deployed.
     */
    public static void retrieveShield(ServerPlayer player, StandingShieldEntity shield) {
        ItemStack recovered = shield.storedItem();
        if (!recovered.isEmpty()) {
            int damage = (int) Math.max(0, Math.min(recovered.getMaxDamage(),
                    recovered.getMaxDamage() - shield.shieldHealth()));
            recovered.setDamageValue(damage);

            InteractionHand hand = shieldHand(player);
            if (hand != null && player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, recovered);
                shield.discard();
                return;
            }

            if (hasEmptySlot(player)) {
                boolean stored = player.addItem(recovered);
                if (!stored || recovered.isEmpty()) {
                    player.drop(recovered, false);
                }
            } else {
                player.drop(recovered, false);
            }
        }
        shield.discard();
    }

    /** Places the held standing shield at the block the player is looking at. */
    public static void placeShield(Player player, ItemStack stack) {
        BlockHitResult hit = findPlacement(player);
        if (hit == null) {
            return;
        }

        Level level = ChineseWeapons.gameplay().level(player);
        Entity entity = ChineseWeapons.gameplay().createEntity(
                ChineseweaponsModEntities.STANDING_SHIELD.get(), level);
        if (!(entity instanceof StandingShieldEntity shield)) {
            return;
        }

        float yaw = placementYaw(player);
        Vec3 spawn = hit.getLocation();
        ChineseWeapons.gameplay().moveEntity(entity, spawn.x, spawn.y, spawn.z, yaw, 0.0F);
        shield.storeShield(stack.copy(), initialHealth(stack), yaw);
        level.addFreshEntity(entity);

        stack.shrink(1);
    }

    /** First living entity along the look vector within {@code reach}, excluding the player. */
    public static LivingEntity findLivingTarget(Player player, double reach) {
        Level level = ChineseWeapons.gameplay().level(player);
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(reach));

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (LivingEntity living : level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(reach),
                entity -> entity != player && entity.isAlive())) {

            AABB box = living.getBoundingBox().inflate(0.3D);
            Optional<Vec3> hit = box.clip(eye, end);
            if (hit.isEmpty()) {
                continue;
            }

            double dist = eye.distanceToSqr(hit.get());
            if (dist < bestDist) {
                bestDist = dist;
                best = living;
            }
        }
        return best;
    }

    private static boolean hasEmptySlot(ServerPlayer player) {
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static InteractionHand shieldHand(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof StandingShieldItem) {
            return InteractionHand.MAIN_HAND;
        }
        if (player.getOffhandItem().getItem() instanceof StandingShieldItem) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    private static Vec3 forwardOf(float yaw) {
        double fx = -Math.sin(Math.toRadians(yaw));
        double fz = Math.cos(Math.toRadians(yaw));
        return new Vec3(fx, 0.0D, fz).normalize();
    }

    private static AABB boxAround(Vec3 center) {
        return new AABB(
                center.x - SAFE_ZONE_HALF_SIZE, center.y - SAFE_ZONE_HALF_SIZE,
                center.z - SAFE_ZONE_HALF_SIZE,
                center.x + SAFE_ZONE_HALF_SIZE, center.y + SAFE_ZONE_HALF_SIZE,
                center.z + SAFE_ZONE_HALF_SIZE
        );
    }
}
