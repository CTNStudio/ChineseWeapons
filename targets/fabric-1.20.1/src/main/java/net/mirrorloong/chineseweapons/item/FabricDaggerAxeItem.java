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

    // Vanilla 1.20.1 exposes the crafting-remaining item only through
    // Item.Properties#craftingRemainingItem, which needs the item instance
    // before construction, and makes getCraftingRemainingItem() final. Forge's
    // stack-based hooks do not exist on Fabric, so the definition's
    // craftingRemainingItem flag cannot be applied here.
    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return definition.repairable() && super.isValidRepairItem(stack, repairCandidate);
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
