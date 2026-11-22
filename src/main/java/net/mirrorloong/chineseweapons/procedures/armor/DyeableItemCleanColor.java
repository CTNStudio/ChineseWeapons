package net.mirrorloong.chineseweapons.procedures.armor;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DyeableItemCleanColor {

    private static boolean registered;

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;

        //old api @SubscribeEvent PlayerInteractEvent.RightClickBlock + setCancellationResult/setCanceled -> new api UseBlockCallback.EVENT
        UseBlockCallback.EVENT.register(DyeableItemCleanColor::onRightClickBlock);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof DyeableItem && DyeableItem.hasCustomColor(stack)) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide()) {
                    DyeableItem.removeColor(stack);

                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);

                    level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }
}
