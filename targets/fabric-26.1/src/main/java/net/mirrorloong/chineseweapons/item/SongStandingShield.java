package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;

public class SongStandingShield extends Item {

    public SongStandingShield() {
        super(new Item.Properties()
                .durability(1670)
                .equippableUnswappable(EquipmentSlot.OFFHAND)
                .component(DataComponents.BLOCKS_ATTACKS, ShieldSupport.BLOCKS));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }

    public static void placeShield(Player player, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(5));
        BlockHitResult hit = level.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (hit.getType() == HitResult.Type.MISS) {
            return;
        }

        BlockPos pos = BlockPos.containing(hit.getLocation());
        if (!level.mayInteract(player, pos)) {
            return;
        }

        SongStandingShieldEntity entity = ChineseweaponsModEntities.<SongStandingShieldEntity>type(ChineseweaponsModEntities.STANDING_SHIELD).create(level,
                net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
        if (entity == null) {
            return;
        }

        float yaw = player.getYRot() + 180.0F;

        Vec3 spawn = hit.getLocation();
        entity.setPos(spawn.x, spawn.y, spawn.z);
        entity.setYRot(yaw);
        entity.setShieldYaw(yaw);
        entity.setStoredItem(stack.copy());
        entity.setHealth(stack.getMaxDamage() - stack.getDamageValue());
        level.addFreshEntity(entity);

        stack.shrink(1);
    }
}
