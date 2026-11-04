package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.mirrorloong.chineseweapons.rules.HalberdRules;

public class SkyPiercingHalberdItem extends FourBlockReachSwordItem {

    public SkyPiercingHalberdItem(ToolMaterial material, int displayedAttackDamage, float speed,
            boolean fireResistant) {
        super(
                material,
                Math.round(displayedAttackDamage - 1.0F - material.attackDamageBonus()),
                speed - 4.0F,
                properties(material, fireResistant)
        );
    }

    private static Item.Properties properties(ToolMaterial material, boolean fireResistant) {
        Item.Properties properties = new Item.Properties().durability(material.durability() + 50);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        HalberdRules.onHitEnemy(stack, target, attacker);
    }
}
