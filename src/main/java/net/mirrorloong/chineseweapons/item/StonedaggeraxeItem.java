
package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraftforge.common.crafting.CompoundIngredient;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeItemFierceHookProcedure;
import net.mirrorloong.chineseweapons.procedures.DaggerAxeAndGlaiveItemjizhongProcedure;

public class StonedaggeraxeItem extends FiveBlockReachSwordItem {
	public StonedaggeraxeItem() {
		super(new Tier() {
			public int getUses() {
				return 262;
			}

			public float getSpeed() {
				return 6f;
			}

			public float getAttackDamageBonus() {
				return 2f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 7;
			}

			public Ingredient getRepairIngredient() {
				return CompoundIngredient.of(Ingredient.of(ItemTags.create(new ResourceLocation("forge", "stone"))), Ingredient.of(new ItemStack(ChineseweaponsModItems.STONE_DAGGER_AXE.get())));
			}
		}, 3, -2.5f, new Item.Properties());
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
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
        return retval;
    }
}
