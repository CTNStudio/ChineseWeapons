package net.mirrorloong.chineseweapons.rules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;

import java.util.function.Predicate;

/** Footman armour set: low-health buffs, sprint collisions and arrow deflection. */
public final class FootmenRules {

    private FootmenRules() {
    }

    /**
     * Version-neutral view of a hit against the footman armour.
     * {@code playerOrMobAttack} mirrors the {@code DamageTypes.PLAYER_ATTACK} /
     * {@code DamageTypes.MOB_ATTACK} check of the original procedures.
     */
    public record IncomingDamage(float amount,
                                 LivingEntity attacker,
                                 boolean arrow,
                                 boolean playerOrMobAttack,
                                 Runnable deflectArrow) {
    }

    public static boolean isFullFootmenArmorSet(LivingEntity entity, Predicate<ItemStack> armorGain) {
        return ArmorRules.isFullFootmenArmorSet(entity, armorGain);
    }

    public static boolean isHoldingGlaive(LivingEntity entity) {
        return ArmorRules.isHoldingGlaive(entity);
    }

    /** Passive set bonuses and sprint collisions. */
    public static void execute(Entity entity, Predicate<ItemStack> armorGain) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(livingEntity))) {
            return;
        }
        if (!isFullFootmenArmorSet(livingEntity, armorGain)) {
            return;
        }

        if (!(livingEntity.getHealth() > livingEntity.getMaxHealth() * 0.30)) {
            if (!isHoldingGlaive(livingEntity)) {
                ChineseWeapons.gameplay().applyEffect(livingEntity, GameplayCompat.Effect.SLOWNESS, 1, 1, false, false);
            }
        }

        if (livingEntity.getHealth() <= 3) {
            ChineseWeapons.gameplay().applyEffect(livingEntity, GameplayCompat.Effect.STRENGTH, 100, 1, false, true);
            ChineseWeapons.gameplay().applyEffect(livingEntity, GameplayCompat.Effect.ABSORPTION, 100, 1, false, true);
            ChineseWeapons.gameplay().applyEffect(livingEntity, GameplayCompat.Effect.SPEED, 100, 2, false, true);
        }

        if (entity.isSprinting() && entity instanceof Player player) {
            Entity collisionEntity = ChineseWeapons.gameplay().collisionMount(player);
            checkCollisions(player, collisionEntity);
        }
    }

    public static void checkCollisions(Player player, Entity collisionEntity) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(player))) {
            return;
        }

        boolean hitAnyEntity = false;
        for (Entity entity : ChineseWeapons.gameplay().level(player).getEntities(collisionEntity,
                collisionEntity.getBoundingBox().inflate(1.0, 0.5, 1.0))) {
            if (!(entity instanceof LivingEntity livingEntity)
                    || entity == player
                    || entity.isPassengerOfSameVehicle(player)) {
                continue;
            }
            if (collisionEntity.getBoundingBox().intersects(entity.getBoundingBox())) {
                handleCollision(player, livingEntity);
                hitAnyEntity = true;
            }
        }
        if (hitAnyEntity) {
            player.causeFoodExhaustion(2.0F);
        }
    }

    private static void handleCollision(Player player, LivingEntity target) {
        ChineseWeapons.gameplay().hurt(target, player, 1.0F);
        target.push(player.getLookAngle().x, 0.5D, player.getLookAngle().z);
    }

    /** Cancel-side rule: a full set bounces arrows back at their shooter. */
    public static boolean allowDamage(LivingEntity victim, IncomingDamage damage, Predicate<ItemStack> armorGain) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(victim))) {
            return true;
        }
        if (isFullFootmenArmorSet(victim, armorGain) && damage.arrow()) {
            if (damage.deflectArrow() != null) {
                damage.deflectArrow().run();
            }
            return false;
        }
        return true;
    }

    /**
     * Attacker-side combat effect: a glaive user hitting a near-death footman is
     * put on cooldown, and footman attackers get the (net-zero) +2/-2 amount
     * adjustment. Returns the adjusted amount.
     */
    public static float adjustAmount(LivingEntity victim, IncomingDamage damage, Predicate<ItemStack> armorGain) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(victim))) {
            return damage.amount();
        }

        float amount = damage.amount();

        if (victim.getHealth() <= 3f && isFullFootmenArmorSet(victim, armorGain)) {
            LivingEntity trueAttacker = damage.attacker();
            if (trueAttacker instanceof Player attackerPlayer && isHoldingGlaive(attackerPlayer)) {
                ItemStack weapon = attackerPlayer.getMainHandItem();
                if (!weapon.isEmpty()) {
                    ChineseWeapons.gameplay().addCooldown(attackerPlayer, weapon, 40);
                }
            }
        }

        if (damage.attacker() != null
                && isFullFootmenArmorSet(damage.attacker(), armorGain)
                && damage.playerOrMobAttack()) {
            amount = amount + 2.0F;
            amount = Math.max(0f, amount - 2f);
        }

        return amount;
    }
}
