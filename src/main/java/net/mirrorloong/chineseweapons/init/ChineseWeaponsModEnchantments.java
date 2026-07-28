package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.enchantment.CavalryBusterStar;

public class ChineseWeaponsModEnchantments {
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChineseweaponsMod.MODID);
    //public static final RegistryObject<Enchantment> BaneOfCavalryBaneOfCabalry = REGISTRY.register("cavalry_buster_star", CavalryBusterStar::new);
}
