package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;


public class DeepslateAncientgemoreBlock extends Block {
    public DeepslateAncientgemoreBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).requiresCorrectToolForDrops());
    }

}
