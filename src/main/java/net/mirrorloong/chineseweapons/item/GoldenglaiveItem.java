
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.GlaiveItemGetLostKillProcedure;

public class GoldenglaiveItem extends GlaiveItemBase {
	public GoldenglaiveItem() {
		super(new Tier() {
			public int getUses() {
				return 45;
			}

			public float getSpeed() {
				return 15f;
			}

			public float getAttackDamageBonus() {
				return 3f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 25;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.GOLD_INGOT), new ItemStack(ChineseweaponsModItems.GOLDEN_GLAIVE.get()));
			}
		}, 3, -2.4f, new Item.Properties());
	}

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity);
        return retval;
    }

}
