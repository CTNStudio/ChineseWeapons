package net.mirrorloong.chineseweapons.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class CutsEffect extends MobEffect {
    public CutsEffect() {
        super(MobEffectCategory.HARMFUL,0x000000);
    }
    @Override
    public boolean applyEffectTick(LivingEntity entity, int p_19468_) {
        if(entity.isSprinting()){
            entity.hurt(entity.damageSources().generic(),1f);
        }

        if (entity.level().getRandom().nextFloat() <= 0.3) {
            if (!entity.level().isClientSide()) {
                entity.hurt(entity.damageSources().generic(), 1.0F);
            }
        }

        return true;
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
