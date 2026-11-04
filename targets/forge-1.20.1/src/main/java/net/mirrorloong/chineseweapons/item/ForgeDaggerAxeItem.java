package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeHurtFiyyProcedure;
import net.mirrorloong.chineseweapons.spi.DaggerAxeItem;

public class ForgeDaggerAxeItem extends DaggerAxeItemBase implements DaggerAxeItem {
    private final WeaponDefinition definition;

    public ForgeDaggerAxeItem(WeaponDefinition definition) {
        super(new ForgeWeaponTier(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties(definition));
        this.definition = definition;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean result = super.hurtEnemy(itemstack, entity, sourceentity);
        if (!entity.level().isClientSide()) {
            DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
            DaggerAxeHurtFiyyProcedure.execute(sourceentity, entity);
        }
        return result;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return definition.craftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return definition.craftingRemainingItem() ? new ItemStack(this) : ItemStack.EMPTY;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return definition.repairable();
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
