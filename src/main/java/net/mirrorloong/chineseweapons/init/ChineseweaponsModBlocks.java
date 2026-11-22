package net.mirrorloong.chineseweapons.init;

import net.mirrorloong.chineseweapons.block.*;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.mirrorloong.chineseweapons.block.DeepslateAncientgemoreBlock;

public class ChineseweaponsModBlocks {
	//old api DeferredRegister.create(ForgeRegistries.BLOCKS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.BLOCK, MODID)
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, ChineseweaponsMod.MODID);
	public static final RegistryObject<Block> ARMOR_CASTING_TABLE = REGISTRY.register("armor_casting_table", ArmorcastingtableBlock::new);
    public static final RegistryObject<Block> ANCIENT_GEM_ORE = REGISTRY.register("ancient_gem_ore", AncientgemoreBlock::new);
    public static final RegistryObject<Block> DeepslateANCIENT_GEM_ORE = REGISTRY.register("deepslate_ancient_gem_ore", DeepslateAncientgemoreBlock::new);
    public static final RegistryObject<Block> ANCIENT_GEM_BLOCK =  REGISTRY.register("ancient_gem_block", AncientGemBlock::new);

        public static void bootstrap() {
    }
}
