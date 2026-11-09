package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public abstract class ReachSwordItem extends SwordItem {
    public static final ResourceLocation ENTITY_REACH_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "weapon_entity_reach");

    private final double entityReachBonus;

    protected ReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
            Item.Properties properties, double entityReachBonus) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
        this.entityReachBonus = entityReachBonus;
    }

    public double getReachBonus() {
        return WeaponScriptSettings.getReachBonus(this, this.entityReachBonus);
    }

    public boolean canPerformSweepAttack(ItemStack stack) {
        return true;
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
