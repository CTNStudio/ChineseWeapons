package net.mirrorloong.chineseweapons.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;


public class DeepslateAncientgemoreBlock extends Block {
    public DeepslateAncientgemoreBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }
}
