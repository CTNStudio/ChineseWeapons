package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeHurtFiyyProcedure;
import net.mirrorloong.chineseweapons.spi.DaggerAxeItem;

/**
 * Generic, definition-driven dagger-axe. Replaces the deleted per-material
 * dagger-axe classes; {@link DaggerAxeItemBase} keeps the shared inventory
 * behaviour.
 */
public class FabricDaggerAxeItem extends DaggerAxeItemBase implements DaggerAxeItem {
    private final WeaponDefinition definition;

    public FabricDaggerAxeItem(WeaponDefinition definition) {
        super(new FabricWeaponTier(definition), definition.attackDamageModifier(),
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

    // The crafting-remaining self reference is provided by ItemMixin (vanilla's
    // final getCraftingRemainingItem needs the item before construction).
    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return definition.repairable() && super.isValidRepairItem(stack, repairCandidate);
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
