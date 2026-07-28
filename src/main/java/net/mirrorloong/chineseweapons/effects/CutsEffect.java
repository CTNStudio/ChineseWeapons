package net.mirrorloong.chineseweapons.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CutsEffect extends MobEffect {
    public CutsEffect() {
        super(MobEffectCategory.HARMFUL,0x000000);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        if(entity.isSprinting()){
            entity.hurt(entity.damageSources().generic(),1f);
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
