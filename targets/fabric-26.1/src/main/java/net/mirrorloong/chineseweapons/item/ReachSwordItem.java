package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

/**
 * Fabric port of the reach sword base class. Forge's {@code ForgeMod.ENTITY_REACH}
 * attribute has no Fabric equivalent (entity interaction range lives in
 * hard-coded game-mode checks), so the reach modifier is not applied; the class
 * hierarchy and constructor signatures are kept for the concrete swords.
 *
 * <p>26.1 removed {@code SwordItem}: a sword is a plain item whose properties
 * carry the tool material, sword attributes and durability.</p>
 */
public abstract class ReachSwordItem extends Item {
    private final double entityReachBonus;

    protected ReachSwordItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties, double entityReachBonus) {
        super(properties.sword(material, attackDamageModifier, attackSpeedModifier));
        this.entityReachBonus = entityReachBonus;
    }

    public double entityReachBonus() {
        return entityReachBonus;
    }
}

abstract class FourBlockReachSwordItem extends ReachSwordItem {
    protected FourBlockReachSwordItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties) {
        super(material, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }
}

abstract class SixBlockReachSwordItem extends ReachSwordItem {
    protected SixBlockReachSwordItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties) {
        super(material, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }

    public abstract boolean canPerformSweepAttack(ItemStack stack);
}

abstract class FiveBlockReachSwordItem extends ReachSwordItem {
    protected FiveBlockReachSwordItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties) {
        super(material, attackDamageModifier, attackSpeedModifier, properties, 2.0D);
    }
}
