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
 * Generic 26.1 glaive driven by {@link WeaponDefinition}. The reach, offhand
 * clearing and spin-start behaviour stays on {@link GlaiveItemBase}; this class
 * only adds the per-family hit/use hooks the old per-material classes repeated.
 */
public class FabricGlaiveItem extends GlaiveItemBase implements GlaiveItem {
    public FabricGlaiveItem(WeaponDefinition definition) {
        super(WeaponMaterials.of(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties(definition));
    }

    @Override
    public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        super.hurtEnemy(itemstack, entity, sourceentity);
        DaggerAxeAndGlaiveItemjizhongProcedure.execute(entity, itemstack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemstack, Player player, LivingEntity target,
            InteractionHand hand) {
        GlaiveItemStopRunProcedure.execute(target, player, itemstack);
        return InteractionResult.SUCCESS;
    }

    private static Item.Properties properties(WeaponDefinition definition) {
        Item.Properties properties = ItemIds.apply(new Item.Properties());
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
