package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;


@EventBusSubscriber(modid = ChineseweaponsMod.MODID)
public class DyeableItemCleanColor {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof DyeableItem && DyeableItem.hasCustomColor(stack)) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.WATER_CAULDRON)) {
                if (!level.isClientSide()) {
                    DyeableItem.removeColor(stack);

                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);

                    level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
                event.setCanceled(true);
            }
        }
    }
}
