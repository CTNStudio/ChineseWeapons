
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;

public class NetheriteglaiveItem extends GlaiveItemBase {
	public NetheriteglaiveItem() {
		super(new Tier() {
			public int getUses() {
				return 2844;
			}

			public float getSpeed() {
				return 12f;
			}

			public float getAttackDamageBonus() {
				return 5f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.NETHERITE_INGOT), new ItemStack(ChineseweaponsModItems.NETHERITE_GLAIVE.get()));
			}
		}, 3, -3f, new Item.Properties().fireResistant());
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
