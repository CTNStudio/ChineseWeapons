package net.mirrorloong.chineseweapons.rules;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Tang-dynasty heng saber double-right-click stab: click timing, cooldown,
 * durability, area damage, cuts effect and the 46-tick animation state.
 */
public final class TangSaberRules {

    private static final int STAB_COOLDOWN_TICKS = 10;
    private static final int STAB_DURABILITY_COST = 2;
    private static final int DOUBLE_CLICK_WINDOW_MILLIS = 400;
    private static final int ANIMATION_TICKS = 46;

    private static final Map<UUID, Long> LAST_RIGHT_CLICK = new HashMap<>();
    private static final Map<UUID, Boolean> HAS_FIRST_CLICK = new HashMap<>();
    private static final Map<UUID, StabAnimation> ANIMATIONS = new HashMap<>();

    private TangSaberRules() {
    }

    private record StabAnimation(Item bindItem, int tickLeft) {
    }

    /**
     * Handles one right-click. The target decides whether the held item is one
     * of the six heng sabers and supplies the "tamed horse owned by this player"
     * exclusion of its horse API.
     */
    public static void rightClick(Player player, boolean trackedWeapon, Predicate<Entity> excludedHorse,
                                  float baseDamage) {
        UUID uuid = player.getUUID();
        long now = System.currentTimeMillis();
        ItemStack mainHand = player.getMainHandItem();

        if (!trackedWeapon) {
            return;
        }

        if (ChineseWeapons.gameplay().isOnCooldown(player, mainHand)) {
            return;
        }
        if (ANIMATIONS.containsKey(uuid)) {
            return;
        }

        boolean firstPressed = HAS_FIRST_CLICK.getOrDefault(uuid, false);
        long lastTime = LAST_RIGHT_CLICK.getOrDefault(uuid, 0L);

        if (!firstPressed) {
            HAS_FIRST_CLICK.put(uuid, true);
            LAST_RIGHT_CLICK.put(uuid, now);
        } else {
            if (now - lastTime <= DOUBLE_CLICK_WINDOW_MILLIS) {
                executeStab(player, excludedHorse, baseDamage);
            }
            HAS_FIRST_CLICK.put(uuid, false);
        }
    }

    private static void executeStab(Player player, Predicate<Entity> excludedHorse, float baseDamage) {
        ItemStack handItem = player.getMainHandItem();
        if (handItem.isEmpty()) {
            return;
        }

        ChineseWeapons.gameplay().addCooldown(player, handItem, STAB_COOLDOWN_TICKS);
        ChineseWeapons.gameplay().damageItem(handItem, STAB_DURABILITY_COST, player);

        Vec3 look = player.getLookAngle();
        Vec3 playerPos = player.position();

        AABB killBox = new AABB(
                playerPos.x - 0.5F,
                playerPos.y,
                playerPos.z - 0.5F,
                playerPos.x + 0.5F + look.x * 3,
                playerPos.y + 2,
                playerPos.z + 0.5F + look.z * 3
        );

        float damage = baseDamage + 2.0F;

        ChineseWeapons.gameplay().level(player).getEntities(player, killBox,
                        e -> e instanceof LivingEntity living && living != player && !excludedHorse.test(e))
                .forEach(entity -> {
                    LivingEntity target = (LivingEntity) entity;
                    ChineseWeapons.gameplay().hurt(target, player, damage);
                    ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.CusEffectSupplier,
                            60, 0, false, true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        ChineseWeapons.gameplay().grantAdvancement(serverPlayer,
                                "get_ancient_gem/xiao_tang_dynasty_heng_saber");
                    }
                });

        ANIMATIONS.put(player.getUUID(), new StabAnimation(handItem.getItem(), ANIMATION_TICKS));
    }

    /** Advances the stab animation and drops it when the pose is interrupted. */
    public static void tick(Player player) {
        UUID uuid = player.getUUID();
        StabAnimation animation = ANIMATIONS.get(uuid);
        if (animation == null) {
            return;
        }

        boolean interrupt = player.getMainHandItem().getItem() != animation.bindItem()
                || player.getMainHandItem().isEmpty()
                || player.swinging
                || !player.getOffhandItem().isEmpty();

        if (interrupt) {
            ANIMATIONS.remove(uuid);
            return;
        }

        int tickLeft = animation.tickLeft() - 1;
        if (tickLeft <= 0) {
            ANIMATIONS.remove(uuid);
            return;
        }
        ANIMATIONS.put(uuid, new StabAnimation(animation.bindItem(), tickLeft));
    }

    public static float animationProgress(UUID uuid) {
        StabAnimation animation = ANIMATIONS.get(uuid);
        if (animation == null) {
            return 0.0F;
        }

        int tickLeft = animation.tickLeft();
        int passed = ANIMATION_TICKS - tickLeft;
        if (passed <= 8) {
            return (float) passed / 8;
        }
        if (passed <= 38) {
            return 1.0F;
        }

        int retractPass = passed - 38;
        return 1.0F - ((float) retractPass / 8);
    }
}
