package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

public class DaggerAxeAndGlaiveItemjizhongProcedure {
    private static final RandomSource RANDOM = RandomSource.create();

    public static void execute(Entity entity) {
        if (entity == null) {
            return;
        }

        if (entity.level().isClientSide()) {
            return;
        }

        if (Mth.nextDouble(RANDOM, 1, 100) <= 80) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(
                        ChineseWeaponsModEffects.CusEffectSupplier.get(),
                        120,
                        0,
                        true,
                        true
                ));
            }
        }
    }
}