
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeItemFierceHookProcedure;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeAndGlaiveItemjizhongProcedure;

public class DiamonddaggeraxeItem extends FiveBlockReachSwordItem {
	public DiamonddaggeraxeItem() {
		super(new Tier() {
			public int getUses() {
				return 3601;
			}

			public float getSpeed() {
				return 10f;
			}

			public float getAttackDamageBonus() {
				return 9f;
			}

			public int getLevel() {
				return 3;
			}

			public int getEnchantmentValue() {
				return 12;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.DIAMOND), new ItemStack(ChineseweaponsModItems.DIAMOND_DAGGER_AXE.get()));
			}
		}, 3, -2.1f, new Item.Properties());
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        DaggerAxeItemFierceHookProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, ar.getObject());
        return ar;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity);
        return retval;
    }

}
