package net.mirrorloong.chineseweapons.block;

import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;

import net.minecraftforge.network.NetworkHooks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ArmorcastingtableBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 12, 0, 16, 16, 16),
            box(0, 0, 0, 16, 4, 16),
            box(1, 4, 1, 15, 12, 15)
    );

    public ArmorcastingtableBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(3f, 10f)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArmorCastingTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof ArmorCastingTableBlockEntity table) {
                ArmorCastingTableBlockEntity.serverTick(lvl, pos, st, table);
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ArmorCastingTableBlockEntity table && level instanceof ServerLevel sl) {
                table.dropContents(sl, pos);
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);
        if (entity instanceof ServerPlayer player) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof ArmorCastingTableBlockEntity)) {
                return InteractionResult.PASS;
            }
            NetworkHooks.openScreen(player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("block.chineseweapons.armor_casting_table");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return new WeaponsCastingTableTypeGuiMenu(id, inventory, pos);
                }
            }, pos);
        }
        return InteractionResult.SUCCESS;
    }
}