
package net.mirrorloong.chineseweapons.init;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.mirrorloong.chineseweapons.block.AncientgemoreBlock;
import net.mirrorloong.chineseweapons.block.ArmorcastingtableBlock;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.mirrorloong.chineseweapons.block.DeepslateAncientgemoreBlock;

public class ChineseweaponsModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ChineseweaponsMod.MODID);
	public static final RegistryObject<Block> ARMOR_CASTING_TABLE = REGISTRY.register("armor_casting_table", ArmorcastingtableBlock::new);
    public static final RegistryObject<Block> ANCIENT_GEM_ORE = REGISTRY.register("ancient_gem_ore", AncientgemoreBlock::new);
    public static final RegistryObject<Block> DeepslateANCIENT_GEM_ORE = REGISTRY.register("deepslate_ancient_gem_ore", DeepslateAncientgemoreBlock::new);
}
