package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.effects.BleedEffect;
import net.mirrorloong.chineseweapons.effects.CutsEffect;

public class ChineseWeaponsModEffects {
    public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ChineseweaponsMod.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> BleedEffectSupplier = REGISTER.register("bleed",BleedEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> CusEffectSupplier = REGISTER.register("cuts", CutsEffect::new);
}
