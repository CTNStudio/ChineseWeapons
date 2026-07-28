
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeAndGlaiveItemjizhongProcedure;

public class GreenloongglaiveItem extends GlaiveItemBase {
	public GreenloongglaiveItem() {
		super(new Tier() {
			public int getUses() {
				return 2855;
			}

			public float getSpeed() {
				return 14f;
			}

			public float getAttackDamageBonus() {
				return 10f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 20;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(ChineseweaponsModItems.GREENLOONG_GLAIVE.get()));
			}
		}, 3, -2.3f, new Item.Properties().fireResistant());
	}

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity);
        return retval;
    }
}
