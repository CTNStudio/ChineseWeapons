package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fabric replacement for Forge's {@code PlayerInteractEvent.RightClickBlock}:
 * registered through Fabric's {@code UseBlockCallback} and invoked with the
 * stack held in the interacting hand.
 */
public class DyeableItemCleanColor {

    public static InteractionResult onRightClickBlock(Player player, Level level, BlockPos pos, ItemStack stack) {
        if (!(stack.getItem() instanceof DyeableItem) || !DyeableItem.hasCustomColor(stack)) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.WATER_CAULDRON)) {
            if (level.isClientSide()) {
                // Let the interaction travel to the server, which owns the stack
                // and the cauldron state.
                return InteractionResult.PASS;
            }

            DyeableItem.removeColor(stack);
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);

            level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
