package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;


public class AncientgemoreBlock extends Block {
    public AncientgemoreBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE).requiresCorrectToolForDrops());
    }

}
