package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public abstract class ReachSwordItem extends SwordItem {
            private final double entityReachBonus;

    protected ReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties, double entityReachBonus) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.entityReachBonus = entityReachBonus;
    }

        public double getEntityReachBonus() {
        return WeaponScriptSettings.getReachBonus(this, this.entityReachBonus);
    }
}

abstract class FourBlockReachSwordItem extends ReachSwordItem {
    protected FourBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }
}

abstract class SixBlockReachSwordItem extends ReachSwordItem {
    protected SixBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }

    public abstract boolean canPerformSweepAttack(ItemStack stack);
}

abstract class FiveBlockReachSwordItem extends ReachSwordItem {
    protected FiveBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 2.0D);
    }
}
