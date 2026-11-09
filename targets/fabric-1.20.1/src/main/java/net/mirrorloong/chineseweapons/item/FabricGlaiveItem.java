package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeAndGlaiveItemjizhongProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

/**
 * Generic, definition-driven glaive. Replaces the deleted per-material glaive
 * classes; {@link net.mirrorloong.chineseweapons.item.GlaiveItemBase} keeps the
 * shared use/inventory behaviour.
 */
public class FabricGlaiveItem extends GlaiveItemBase implements GlaiveItem {
    public FabricGlaiveItem(WeaponDefinition definition) {
        super(new FabricWeaponTier(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties(definition));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean result = super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
        return result;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemstack, Player player, LivingEntity target,
                                                  InteractionHand hand) {
        GlaiveItemStopRunProcedure.execute(target, player, itemstack);
        return InteractionResult.SUCCESS;
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
