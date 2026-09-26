package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DaggerAxeHurtFiyyProcedure {
    public static void execute(LivingEntity attacker, LivingEntity victim) {
        if (attacker == null || victim == null || !victim.isAlive()) return;
        if (victim.level().isClientSide()) return;
        if (attacker.onGround()) return;

        if (attacker instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.isEmpty() || player.getCooldowns().isOnCooldown(mainHand.getItem())) {
                return;
            }

            Vec3 lookVec = attacker.getLookAngle();
            victim.push(lookVec.x, 0.5, lookVec.z);
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, true));

            DamageSource damageSource = new DamageSource(
                    victim.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.PLAYER_ATTACK),
                    attacker
            );
            victim.hurt(damageSource, 1.0f);

            mainHand.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));

            player.getCooldowns().addCooldown(mainHand.getItem(), 40);
        }
    }
}