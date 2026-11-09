package net.mirrorloong.chineseweapons.compat;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public final class EffectHolders {
    private EffectHolders() {
    }

    public static Holder<MobEffect> holder(RegistryEntry<MobEffect> entry) {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(entry.get());
    }
}
