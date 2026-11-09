package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.item.TangDynastyHorseItem;

/**
 * Fabric replacement for the Forge event subscriber of the same purpose.
 * The arrow-repelling part used Forge's {@code ProjectileImpactEvent}, which has
 * no Fabric 1.20.1 equivalent; only the damage cancellation is preserved.
 */
public class TangDynastyHorseJianProcedure {

    public static boolean allowDamage(LivingEntity entity, DamageSource source) {
        if (!(entity instanceof Horse horse)) {
            return true;
        }
        if (!(source.getDirectEntity() instanceof AbstractArrow)) {
            return true;
        }

        ItemStack armor = horse.getArmor();
        if (!armor.isEmpty() && armor.getItem() instanceof TangDynastyHorseItem) {
            return false;
        }
        return true;
    }
}
