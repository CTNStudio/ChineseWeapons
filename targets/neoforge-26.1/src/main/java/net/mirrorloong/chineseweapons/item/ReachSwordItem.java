package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ReachSwordItem extends Item {
    private final double entityReachBonus;

    protected ReachSwordItem(Item.Properties properties, double entityReachBonus) {
        super(properties);
        this.entityReachBonus = entityReachBonus;
    }

    public double getReachBonus() {
        return entityReachBonus;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}

abstract class FourBlockReachSwordItem extends ReachSwordItem {
    protected FourBlockReachSwordItem(Item.Properties properties) {
        super(properties, 1.0D);
    }
}

abstract class SixBlockReachSwordItem extends ReachSwordItem {
    protected SixBlockReachSwordItem(Item.Properties properties) {
        super(properties, 1.0D);
    }

    public boolean canPerformSweepAttack(ItemStack stack) {
        return true;
    }
}

abstract class FiveBlockReachSwordItem extends ReachSwordItem {
    protected FiveBlockReachSwordItem(Item.Properties properties) {
        super(properties, 2.0D);
    }
}
