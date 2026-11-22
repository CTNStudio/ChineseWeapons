package net.mirrorloong.chineseweapons.procedures;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.item.TangDynastyHorseItem;

public class TangDynastyHorseJianProcedure {
    private static boolean registered;

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;

                                ServerLivingEntityEvents.ALLOW_DAMAGE.register(TangDynastyHorseJianProcedure::onLivingDamage);
    }

    public static boolean onLivingDamage(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof Horse horse)) return true;
        if (!(source.getDirectEntity() instanceof AbstractArrow arrow)) return true;

        ItemStack armor = horse.getArmor();
        if (!armor.isEmpty() && armor.getItem() instanceof TangDynastyHorseItem) {
            reflectArrow(arrow);
            return false;
        }
        return true;
    }

    private static void reflectArrow(AbstractArrow arrow) {
        Vec3 motion = arrow.getDeltaMovement();
        Vec3 reflected = motion.reverse();

        double randomX = (arrow.level().random.nextDouble() - 0.5) * 0.3;
        double randomZ = (arrow.level().random.nextDouble() - 0.5) * 0.3;
        reflected = reflected.add(randomX, 0.1, randomZ);

        double speed = motion.length() * 0.8;
        arrow.setDeltaMovement(reflected.normalize().scale(speed));
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        arrow.setOwner(null);
    }
}
