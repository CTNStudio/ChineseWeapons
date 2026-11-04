package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class TangDynastyHengSaberItem extends FourBlockReachSwordItem {

    public TangDynastyHengSaberItem(ToolMaterial material, int displayedAttackDamage, float speed,
            boolean fireResistant) {
        super(ModWeapons.sword(material,
                Math.round(displayedAttackDamage - 1.0F - material.attackDamageBonus()),
                speed - 4.0F,
                properties(fireResistant)));
    }

    private static Item.Properties properties(boolean fireResistant) {
        Item.Properties properties = new Item.Properties();
        return fireResistant ? properties.fireResistant() : properties;
    }
}
