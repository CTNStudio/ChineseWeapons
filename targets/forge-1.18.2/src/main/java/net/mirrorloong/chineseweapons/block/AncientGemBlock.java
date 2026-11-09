package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AncientGemBlock extends Block {
    public AncientGemBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.GLASS).strength(5f, 10f).requiresCorrectToolForDrops());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }
}
