package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.event.ChineseWeaponsCombatEvents;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

public class SkyPiercingHalberdItem extends FourBlockReachSwordItem {
    private static final float ATTACK_SPEED_MODIFIER = -2.2F;

    public SkyPiercingHalberdItem(Tier tier, int displayedAttackDamage, boolean fireResistant) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.getAttackDamageBonus()),
                ATTACK_SPEED_MODIFIER,
                properties(tier, fireResistant)
        );
    }

    private static Item.Properties properties(Tier tier, boolean fireResistant) {
        Item.Properties properties = new Item.Properties().durability(tier.getUses() + 50);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!result || target.level().isClientSide() || !(attacker instanceof Player player)) {
            return result;
        }

        if (player.isShiftKeyDown()) {
            target.knockback(2.0D, player.getX() - target.getX(), player.getZ() - target.getZ());
            target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.BleedEffectSupplier.get(), 5 * 20, 0, false, true));
        }

        if (player.getVehicle() != null && target.getVehicle() == null) {
            ChineseWeaponsCombatEvents.tryStartMountedDrag(player, target);
        }
        return result;
    }
}
