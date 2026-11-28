package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;

public class GlaiveItem extends GlaiveItemBase {

    public GlaiveItem(Tier tier, int displayedAttackDamage, float speed, boolean fireResistant) {
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
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
        return retval;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemstack, Player player, LivingEntity target, InteractionHand hand) {
        GlaiveItemStopRunProcedure.execute(target, player, itemstack);
        return InteractionResult.SUCCESS;
    }
}
