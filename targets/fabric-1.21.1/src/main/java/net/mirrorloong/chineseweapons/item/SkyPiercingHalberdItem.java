package net.mirrorloong.chineseweapons.item;

import net.mirrorloong.chineseweapons.compat.EffectHolders;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

public class SkyPiercingHalberdItem extends FourBlockReachSwordItem {

    public SkyPiercingHalberdItem(Tier tier, int displayedAttackDamage, float speed, boolean fireResistant) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.getAttackDamageBonus()),
                speed - 4.0F,
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
        if (!result || target.level().isClientSide() || !(attacker instanceof ServerPlayer player) || !target.isAlive()) {
            return result;
        }

        if (player.isShiftKeyDown()) {
            double dx = player.getX() - target.getX();
            double dz = player.getZ() - target.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0.01) {
                dx /= distance;
                dz /= distance;
            } else {
                float yaw = player.getYRot();
                dx = -Math.sin(Math.toRadians(yaw));
                dz = Math.cos(Math.toRadians(yaw));
            }

            target.knockback(2.0D, dx, dz);

            if(!target.hasEffect(EffectHolders.holder(ChineseWeaponsModEffects.BleedEffectSupplier))){
                target.addEffect(new MobEffectInstance(EffectHolders.holder(ChineseWeaponsModEffects.BleedEffectSupplier), 5 * 20, 0, false, true));
            }

            stack.hurtAndBreak(2, player, EquipmentSlot.MAINHAND);

            if (player.getServer() != null) {
                AdvancementHolder adv = player.getServer().getAdvancements().get(
                        ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "get_sky_piercing_halberd/use_fangtian_hengzhan"));

                if (adv != null) {
                    AdvancementProgress prog = player.getAdvancements().getOrStartProgress(adv);
                    if (!prog.isDone()) {
                        for (String criterion : prog.getRemainingCriteria()) {
                            player.getAdvancements().award(adv, criterion);
                        }
                    }
                }
            }
        }

        if (player.getVehicle() != null && target.getVehicle() == null
                && !ChineseWeaponsCombatEvents.isTethered(target)) {
            ChineseWeaponsCombatEvents.tryStartMountedDrag(player, target);
        }
        return result;
    }
}