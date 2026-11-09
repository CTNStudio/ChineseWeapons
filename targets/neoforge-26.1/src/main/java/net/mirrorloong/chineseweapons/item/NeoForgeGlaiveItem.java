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

public class NeoForgeGlaiveItem extends GlaiveItemBase implements GlaiveItem {
    public NeoForgeGlaiveItem(WeaponDefinition definition) {
        super(properties(definition));
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
        Item.Properties properties = definition.fireResistant()
                ? ItemIds.apply(new Item.Properties()).fireResistant()
                : ItemIds.apply(new Item.Properties());
        return ModWeapons.sword(ModWeapons.toolMaterial(definition), definition.attackDamageModifier(),
                definition.attackSpeedModifier(), properties);
    }
}
