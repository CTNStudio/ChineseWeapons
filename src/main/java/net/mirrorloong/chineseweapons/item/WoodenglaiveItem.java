
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraftforge.common.crafting.CompoundIngredient;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.GlaiveItemGetLostKillProcedure;

public class WoodenglaiveItem extends GlaiveItemBase {
	public WoodenglaiveItem() {
		super(new Tier() {
			public int getUses() {
				return 83;
			}

			public float getSpeed() {
				return 5f;
			}

			public float getAttackDamageBonus() {
				return 1f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			public Ingredient getRepairIngredient() {
				return CompoundIngredient.of(Ingredient.of(ItemTags.create(new ResourceLocation("minecraft","planks"))), Ingredient.of(new ItemStack(ChineseweaponsModItems.WOODEN_GLAIVE.get())));
			}
		}, 3, -2.2f, new Item.Properties());
	}

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
        return retval;
    }

}
