package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.effects.BleedEffect;
import net.mirrorloong.chineseweapons.effects.CutsEffect;

public class ChineseWeaponsModEffects {
    public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChineseweaponsMod.MODID);
    public static final RegistryObject<MobEffect> BleedEffectSupplier = REGISTER.register("bleed",BleedEffect::new);

    public static final RegistryObject<MobEffect> CusEffectSupplier = REGISTER.register("cuts", CutsEffect::new);
}
