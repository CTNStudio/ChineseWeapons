package net.mirrorloong.chineseweapons.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

/**
 * 26.1 effect APIs take {@link Holder}s. The shared SPI exposes registry
 * entries as plain values, so the registered object is wrapped back into the
 * holder the registry created for it.
 */
public final class EffectHolders {
    private EffectHolders() {
    }

    public static Holder<MobEffect> of(MobEffect effect) {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    }
}
