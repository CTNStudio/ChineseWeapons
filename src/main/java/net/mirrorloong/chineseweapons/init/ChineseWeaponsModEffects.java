package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.effects.BleedEffect;
import net.mirrorloong.chineseweapons.effects.CutsEffect;

public class ChineseWeaponsModEffects {
    //old api DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID)
    public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ChineseweaponsMod.MODID);

        public static final RegistryObject<MobEffect> BleedEffectSupplier = REGISTER.register("bleed",BleedEffect::new);

        public static final RegistryObject<MobEffect> CusEffectSupplier = REGISTER.register("cuts", CutsEffect::new);

            public static void bootstrap() {
    }
}
