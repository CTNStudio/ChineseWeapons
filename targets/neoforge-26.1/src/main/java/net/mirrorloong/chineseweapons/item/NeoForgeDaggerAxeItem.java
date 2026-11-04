package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeHurtFiyyProcedure;
import net.mirrorloong.chineseweapons.spi.DaggerAxeItem;

public class NeoForgeDaggerAxeItem extends DaggerAxeItemBase implements DaggerAxeItem {
    public NeoForgeDaggerAxeItem(WeaponDefinition definition) {
        super(properties(definition));
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
        Item.Properties properties = definition.fireResistant()
                ? new Item.Properties().fireResistant()
                : new Item.Properties();
        return ModWeapons.sword(ModWeapons.toolMaterial(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties);
    }
}
