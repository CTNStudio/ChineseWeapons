package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.fabric.FabricGameplayCompat;
import net.mirrorloong.chineseweapons.rules.ArmorRules;
import net.mirrorloong.chineseweapons.rules.FootmenRules;

public class FootmenarmorcompletetestingProcedure {

    public static boolean isFullFootmenArmorSet(LivingEntity livingEntity) {
        return ArmorRules.isFullFootmenArmorSet(livingEntity, FabricGameplayCompat.ARMOR_GAIN);
    }

    public static boolean isHoldingGlaive(LivingEntity livingEntity) {
        return ArmorRules.isHoldingGlaive(livingEntity);
    }

    public static void execute(Entity entity) {
        FootmenRules.execute(entity, FabricGameplayCompat.ARMOR_GAIN);
    }

    public static void checkCollisions(Player player) {
        FootmenRules.checkCollisions(player, ChineseWeapons.gameplay().collisionMount(player));
    }

    /** Fabric's cancel-side hook ({@code ALLOW_DAMAGE}). */
    public static boolean allowDamage(LivingEntity victim, DamageSource source) {
        return FootmenRules.allowDamage(victim, describe(victim, source, 0.0F), FabricGameplayCompat.ARMOR_GAIN);
    }

    /** Fabric's amount hook, driven from the {@code LivingEntityMixin}. */
    public static float modifyDamageAmount(LivingEntity victim, DamageSource source, float amount) {
        return FootmenRules.adjustAmount(victim, describe(victim, source, amount), FabricGameplayCompat.ARMOR_GAIN);
    }

    private static FootmenRules.IncomingDamage describe(LivingEntity victim, DamageSource source, float amount) {
        LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
        Entity direct = source.getDirectEntity();
        boolean arrow = direct instanceof AbstractArrow;
        boolean playerOrMobAttack = source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK);
        Runnable deflect = arrow
                ? () -> {
                    AbstractArrow shot = (AbstractArrow) direct;
                    shot.setDeltaMovement(victim.getLookAngle().scale(1.8D));
                    shot.setOwner(victim);
                    shot.pickup = AbstractArrow.Pickup.DISALLOWED;
                }
                : null;

        return new FootmenRules.IncomingDamage(amount, attacker, arrow, playerOrMobAttack, deflect);
    }
}
