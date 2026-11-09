package net.mirrorloong.chineseweapons.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CutsEffect extends MobEffect {
    public CutsEffect() {
        super(MobEffectCategory.HARMFUL, 0x000000);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity.isSprinting()) {
            entity.hurt(entity.damageSources().generic(), 1f);
        }

        if (level.getRandom().nextFloat() <= 0.3) {
            entity.hurt(entity.damageSources().generic(), 1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
