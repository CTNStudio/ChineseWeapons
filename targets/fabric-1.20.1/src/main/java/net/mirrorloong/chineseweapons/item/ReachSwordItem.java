package net.mirrorloong.chineseweapons.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * Fabric port of the reach sword base class. Forge's {@code ForgeMod.ENTITY_REACH}
 * attribute has no Fabric equivalent in 1.20.1 (entity interaction range lives in
 * hard-coded game-mode checks), so the reach modifier is not applied; the class
 * hierarchy and constructor signatures are kept for the concrete swords.
 */
public abstract class ReachSwordItem extends SwordItem {
    private final double entityReachBonus;

    protected ReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties, double entityReachBonus) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.entityReachBonus = entityReachBonus;
    }

    public double entityReachBonus() {
        return entityReachBonus;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return super.getDefaultAttributeModifiers(equipmentSlot);
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
