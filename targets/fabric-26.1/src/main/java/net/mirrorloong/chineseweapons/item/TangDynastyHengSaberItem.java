package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class TangDynastyHengSaberItem extends FourBlockReachSwordItem {

    public TangDynastyHengSaberItem(ToolMaterial material, int displayedAttackDamage, float speed,
            boolean fireResistant) {
        super(
                material,
                Math.round(displayedAttackDamage - 1.0F - material.attackDamageBonus()),
                speed - 4.0F,
                properties(material, fireResistant)
        );
    }

    private static Item.Properties properties(ToolMaterial material, boolean fireResistant) {
        Item.Properties properties = ItemIds.apply(new Item.Properties()).durability(material.durability());
        return fireResistant ? properties.fireResistant() : properties;
    }
}
