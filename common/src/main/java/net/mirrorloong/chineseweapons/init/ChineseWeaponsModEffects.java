package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.effect.MobEffect;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public class ChineseWeaponsModEffects {
    private static final RegistryBuilder<MobEffect> REGISTER = ChineseWeapons.registration().builder("minecraft:mob_effect");

    //流血
    public static final RegistryEntry<MobEffect> BleedEffectSupplier =
            REGISTER.register("bleed", () -> ChineseWeapons.platform().content().bleedEffect());

    //割伤
    public static final RegistryEntry<MobEffect> CusEffectSupplier =
            REGISTER.register("cuts", () -> ChineseWeapons.platform().content().cutsEffect());

    public static void init() {
    }
}
