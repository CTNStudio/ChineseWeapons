package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeHurtFiyyProcedure;
import net.mirrorloong.chineseweapons.spi.DaggerAxeItem;

/**
 * Generic 26.1 dagger-axe driven by {@link WeaponDefinition}. 26.1 removed the
 * per-item crafting-remainder and repairability overrides, so the definition's
 * {@code craftingRemainingItem} / {@code repairable} flags cannot be applied
 * here (they were not applied by the former per-material classes either).
 */
public class FabricDaggerAxeItem extends DaggerAxeItemBase implements DaggerAxeItem {
    public FabricDaggerAxeItem(WeaponDefinition definition) {
        super(WeaponMaterials.of(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties(definition));
    }

    @Override
    public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        super.hurtEnemy(itemstack, entity, sourceentity);
        if (!entity.level().isClientSide()) {
            DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
            DaggerAxeHurtFiyyProcedure.execute(sourceentity, entity);
        }
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
