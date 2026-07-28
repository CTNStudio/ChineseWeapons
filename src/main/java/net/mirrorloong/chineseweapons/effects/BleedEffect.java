package net.mirrorloong.chineseweapons.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class BleedEffect extends MobEffect {
    public BleedEffect() {
        super( MobEffectCategory.HARMFUL,0x000000);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,25));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,25));
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration%20 == 0;
    }
}
