package net.mirrorloong.chineseweapons.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AncientGemBlock extends Block {
    public AncientGemBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(5f, 10f).requiresCorrectToolForDrops());
    }
}
