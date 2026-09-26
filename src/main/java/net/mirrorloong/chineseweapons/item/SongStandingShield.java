package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;


public class SongStandingShield extends ShieldItem {

    public SongStandingShield() {
        super(new Item.Properties().durability(1670));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return Ingredient.of(new ItemStack(Items.OAK_PLANKS)).test(repair);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public static void placeShield(Player player, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide) return;

        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(5));
        BlockHitResult hit = level.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (hit.getType() == HitResult.Type.MISS) return;

        BlockPos pos = BlockPos.containing(hit.getLocation());
        if (!level.mayInteract(player, pos)) return;

        SongStandingShieldEntity entity = ChineseweaponsModEntities.STANDING_SHIELD.get().create(level);
        if (entity == null) return;

        Vec3 spawn = hit.getLocation();
        entity.moveTo(spawn.x, spawn.y, spawn.z, player.getYRot(), 0);
        entity.setShieldYaw(player.getYRot());
        entity.setStoredItem(stack.copy());
        entity.setHealth(stack.getMaxDamage() - stack.getDamageValue());
        level.addFreshEntity(entity);

        stack.shrink(1);
    }
}