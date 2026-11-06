package net.mirrorloong.chineseweapons.block;

import net.mirrorloong.chineseweapons.ChineseWeapons;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ArmorcastingtableBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 12, 0, 16, 16, 16),
            box(0, 0, 0, 16, 4, 16),
            box(1, 4, 1, 15, 12, 15)
    );

    public ArmorcastingtableBlock(ResourceKey<Block> id) {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(3f, 10f)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false).setId(id));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state) {
        return true;
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
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ArmorCastingTableBlockEntity table) {
            table.dropContents(level, pos);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, moved);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
            Player entity, InteractionHand hand, BlockHitResult hit) {
        if (entity instanceof ServerPlayer player) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof ArmorCastingTableBlockEntity)) {
                return InteractionResult.PASS;
            }
            ChineseWeapons.platform().openCastingTable(player, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
