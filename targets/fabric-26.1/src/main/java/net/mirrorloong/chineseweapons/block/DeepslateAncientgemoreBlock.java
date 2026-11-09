package net.mirrorloong.chineseweapons.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;


public class DeepslateAncientgemoreBlock extends Block {
    public DeepslateAncientgemoreBlock(ResourceKey<Block> id) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).requiresCorrectToolForDrops().setId(id));
    }
}
