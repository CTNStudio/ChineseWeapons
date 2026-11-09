package net.mirrorloong.chineseweapons.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.item.TangDynastyHorseItem;

@EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TangDynastyHorseJianProcedure {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHorseHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Horse horse)) return;
        if (!(event.getSource().getDirectEntity() instanceof AbstractArrow)) return;

        ItemStack armor = horse.getBodyArmorItem();
        if (!armor.isEmpty() && armor.getItem() instanceof TangDynastyHorseItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onArrowImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrow &&
                event.getRayTraceResult() instanceof EntityHitResult hitResult &&
                hitResult.getEntity() instanceof Horse horse) {

            ItemStack armor = horse.getBodyArmorItem();
            if (!armor.isEmpty() && armor.getItem() instanceof TangDynastyHorseItem) {
                event.setCanceled(true);
                reflectArrow(arrow);
            }
        }
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