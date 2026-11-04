package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.rules.GuDuoRules;

public class GuDuoItem extends SixBlockReachSwordItem {

    private final int maxDurability;

    public GuDuoItem(ToolMaterial tier, int displayedAttackDamage, float speed,
                     boolean fireResistant, int durabilityOffset) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.attackDamageBonus()),
                speed - 4.0F,
                makeProperties(fireResistant, durabilityOffset)
        );
        this.maxDurability = durabilityOffset;
    }

    public int getMaxDamage(ItemStack stack) {
        return maxDurability;
    }

    private static Item.Properties makeProperties(boolean fireResistant, int durability) {
        Item.Properties properties = new Item.Properties().durability(durability);
        if (fireResistant) {
            properties.fireResistant();
        }
        return properties;
    }

    @Override
    public boolean canPerformSweepAttack(ItemStack stack) {
        return false;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        GuDuoRules.onHurtEnemy(stack, target, attacker, settings(stack));
        ChineseWeapons.gameplay().damageItem(stack, 2, attacker);
        super.hurtEnemy(stack, target, attacker);
    }

    private static GuDuoRules.Settings settings(ItemStack stack) {
        return new GuDuoRules.Settings(
                WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_ARMORBLINDNESS, 4.0),
                WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_HURTDOWNWARD, 1.0),
                (int) Math.round(WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_HURTSKILLTICK, 10.0)),
                WeaponScriptSettings.getChance(
                        stack.getItem(), WeaponScriptSettings.Skill.GUDUO_STOPRIDING, 0.3F));
    }
}
