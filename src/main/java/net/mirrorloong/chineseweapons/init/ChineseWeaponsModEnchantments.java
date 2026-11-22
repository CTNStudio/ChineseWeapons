package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.enchantment.CavalryBusterStar;

public class ChineseWeaponsModEnchantments {
    //old api DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, MODID)
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, ChineseweaponsMod.MODID);

            public static void bootstrap() {
    }
}
