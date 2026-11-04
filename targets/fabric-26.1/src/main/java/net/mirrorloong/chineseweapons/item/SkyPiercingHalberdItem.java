package net.mirrorloong.chineseweapons.item;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.effects.EffectHolders;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;

public class SkyPiercingHalberdItem extends FourBlockReachSwordItem {

    public SkyPiercingHalberdItem(ToolMaterial material, int displayedAttackDamage, float speed,
            boolean fireResistant) {
        super(
                material,
                Math.round(displayedAttackDamage - 1.0F - material.attackDamageBonus()),
                speed - 4.0F,
                properties(material, fireResistant)
        );
    }

    private static Item.Properties properties(ToolMaterial material, boolean fireResistant) {
        Item.Properties properties = new Item.Properties().durability(material.durability() + 50);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (target.level().isClientSide() || !(attacker instanceof ServerPlayer player) || !target.isAlive()) {
            return;
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

            if (!target.hasEffect(EffectHolders.of(ChineseWeaponsModEffects.BleedEffectSupplier.get()))) {
                target.addEffect(new MobEffectInstance(EffectHolders.of(ChineseWeaponsModEffects.BleedEffectSupplier.get()), 5 * 20,
                        0, false, true));
            }

            stack.hurtAndBreak(2, player, InteractionHand.MAIN_HAND);

            awardAdvanced(player);
        }

        if (player.getVehicle() != null && target.getVehicle() == null
                && !ChineseWeaponsCombatEvents.isTethered(target)) {
            ChineseWeaponsCombatEvents.tryStartMountedDrag(player, target);
        }
    }

    private static void awardAdvanced(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        AdvancementHolder advancement = server.getAdvancements().get(
                Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID,
                        "get_sky_piercing_halberd/use_fangtian_hengzhan"));
        if (advancement == null) {
            return;
        }
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) {
            return;
        }
        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }
}
