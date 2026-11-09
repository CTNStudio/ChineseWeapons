package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.util.valueproviders.UniformInt;


public class AncientgemoreBlock extends Block {
    public AncientgemoreBlock(ResourceKey<Block> id) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).requiresCorrectToolForDrops().setId(id));
    }
}
