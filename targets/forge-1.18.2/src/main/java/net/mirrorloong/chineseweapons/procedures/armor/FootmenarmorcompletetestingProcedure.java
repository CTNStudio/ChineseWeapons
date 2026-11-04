package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.forge.ForgeGameplayCompat;
import net.mirrorloong.chineseweapons.rules.ArmorRules;
import net.mirrorloong.chineseweapons.rules.FootmenRules;

public class FootmenarmorcompletetestingProcedure {

    public static boolean isFullFootmenArmorSet(LivingEntity livingEntity) {
        return ArmorRules.isFullFootmenArmorSet(livingEntity, ForgeGameplayCompat.ARMOR_GAIN);
    }

    public static boolean isHoldingGlaive(LivingEntity livingEntity) {
        return ArmorRules.isHoldingGlaive(livingEntity);
    }

    public static void execute(Entity entity) {
        FootmenRules.execute(entity, ForgeGameplayCompat.ARMOR_GAIN);
    }

    public static void checkCollisions(Player player) {
        FootmenRules.checkCollisions(player, ChineseWeapons.gameplay().collisionMount(player));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = (LivingEntity) event.getEntity();
        FootmenRules.IncomingDamage damage = describe(victim, event.getSource(), event.getAmount());
        float adjusted = FootmenRules.adjustAmount(victim, damage, ForgeGameplayCompat.ARMOR_GAIN);
        if (!FootmenRules.allowDamage(victim, damage, ForgeGameplayCompat.ARMOR_GAIN)) {
            event.setCanceled(true);
            return;
        }
        event.setAmount(adjusted);
    }

    private static FootmenRules.IncomingDamage describe(LivingEntity victim, DamageSource source, float amount) {
        LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
        Entity direct = source.getDirectEntity();
        boolean arrow = direct instanceof AbstractArrow;
        String messageId = source.getMsgId();
        boolean playerOrMobAttack = "player".equals(messageId) || "mob".equals(messageId);
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
