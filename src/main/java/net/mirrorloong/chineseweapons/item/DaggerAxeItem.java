package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeHurtFiyyProcedure;

public class DaggerAxeItem extends DaggerAxeItemBase {

    public DaggerAxeItem(Tier tier, int displayedAttackDamage, float speed, boolean fireResistant) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.getAttackDamageBonus()),
                speed - 4.0F,
                properties(tier, fireResistant)
        );
    }

    private static Properties properties(Tier tier, boolean fireResistant) {
        Properties properties = new Properties().durability(tier.getUses());
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        if (!entity.level().isClientSide()) {
            DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
            DaggerAxeHurtFiyyProcedure.execute(sourceentity, entity);
        }
        return retval;
    }
}
