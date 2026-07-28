package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

public class DaggerAxeAndGlaiveItemjizhongProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (Mth.nextDouble(RandomSource.create(), 1, 100) <= 80) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                _entity.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(), 120, 0, false, true));
        }
    }
}