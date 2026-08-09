package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class TangDynastyHengSaberItem extends FourBlockReachSwordItem {

    public TangDynastyHengSaberItem(Tier tier, int displayedAttackDamage, float speed, boolean fireResistant) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.getAttackDamageBonus()),
                speed - 4.0F,
                properties(tier, fireResistant)
        );
    }

    private static Item.Properties properties(Tier tier, boolean fireResistant) {
        Item.Properties properties = new Item.Properties().durability(tier.getUses());
        return fireResistant ? properties.fireResistant() : properties;
    }
}
