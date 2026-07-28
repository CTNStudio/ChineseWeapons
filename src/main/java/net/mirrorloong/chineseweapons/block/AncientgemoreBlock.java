package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;


public class AncientgemoreBlock extends Block {
    public AncientgemoreBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }
}
