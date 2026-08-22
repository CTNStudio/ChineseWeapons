package net.mirrorloong.chineseweapons.item;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.event.ChineseWeaponsCombatEvents;
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

            if(!target.hasEffect(ChineseWeaponsModEffects.BleedEffectSupplier.get())){
                target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.BleedEffectSupplier.get(), 5 * 20, 0, false, true));
            }

            if (player.getServer() != null) {
                Advancement adv = player.getServer().getAdvancements().getAdvancement(
                        ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "get_sky_piercing_halberd/use_fangtian_hengzhan"));

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