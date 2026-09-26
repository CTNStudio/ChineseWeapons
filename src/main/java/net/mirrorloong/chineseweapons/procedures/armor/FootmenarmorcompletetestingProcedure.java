package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

public class FootmenarmorcompletetestingProcedure {

    public static boolean isFullFootmenArmorSet(LivingEntity livingEntity) {
        ItemStack head = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = livingEntity.getItemBySlot(EquipmentSlot.FEET);

        boolean checkHead = WeaponScriptSettings.getLogicChance(
                head.getItem(), WeaponScriptSettings.Logic.FOOTMENARMOR_GAIN, true);
        boolean checkChest = WeaponScriptSettings.getLogicChance(
                chest.getItem(), WeaponScriptSettings.Logic.FOOTMENARMOR_GAIN, true);
        boolean checkLegs = WeaponScriptSettings.getLogicChance(
                legs.getItem(), WeaponScriptSettings.Logic.FOOTMENARMOR_GAIN, true);
        boolean checkFeet = WeaponScriptSettings.getLogicChance(
                feet.getItem(), WeaponScriptSettings.Logic.FOOTMENARMOR_GAIN, true);

        boolean validHelmet = !checkHead || head.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRON_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRON_BOHAI_HELMET.get();

        boolean validChest = !checkChest || chest.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_CHESTPLATE.get();

        boolean validLegs = !checkLegs || legs.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_LEGGINGS.get();

        boolean validFeet = !checkFeet || feet.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_BOOTS.get();

        return validHelmet && validChest && validLegs && validFeet;
    }

    public static boolean isHoldingGlaive(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        return mainHand.getItem() == ChineseweaponsModItems.DIAMOND_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.GOLDEN_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.GREENLOONG_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.IRON_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.WOODEN_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.NETHERITE_GLAIVE.get()
                || mainHand.getItem() == ChineseweaponsModItems.STONE_GLAIVE.get();
    }

    public static void execute(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (livingEntity.level().isClientSide()) return;
        if (!isFullFootmenArmorSet(livingEntity)) return;

        if (!(livingEntity.getHealth() > livingEntity.getMaxHealth() * 0.30)) {
            if (!isHoldingGlaive(livingEntity)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 1, false, false));
            }
        }

        if (livingEntity.getHealth() <= livingEntity.getMaxHealth() / 4) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, true));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1, false, true));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2, false, true));
        }

        if (entity.isSprinting() && entity instanceof Player player) {
            checkCollisions(player);
        }
    }

    public static void checkCollisions(Player player) {
        if (player.level().isClientSide()) return;
        Entity collisionEntity = player.getVehicle() instanceof AbstractHorse horse ? horse : player;
        var nearbyEntities = player.level().getEntities(collisionEntity,
                collisionEntity.getBoundingBox().inflate(1.0, 0.5, 1.0));
        boolean hitAnyEntity = false;
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof LivingEntity livingEntity)
                    || entity == player
                    || entity.isPassengerOfSameVehicle(player)) {
                continue;
            }
            if (collisionEntity.getBoundingBox().intersects(entity.getBoundingBox())) {
                handleCollision(player, livingEntity);
                hitAnyEntity = true;
            }
        }
        if (hitAnyEntity) {
            player.causeFoodExhaustion(2.0F);
        }
    }

    private static void handleCollision(Player player, LivingEntity target) {
        DamageSource damageSource = new DamageSource(
                player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.PLAYER_ATTACK));
        target.hurt(damageSource, 1.0f);
        double pushX = player.getLookAngle().x * 1;
        double pushY = 0.5;
        double pushZ = player.getLookAngle().z * 1;
        target.push(pushX, pushY, pushZ);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        DamageSource source = event.getSource();
        Entity directEntity = source.getDirectEntity();

        if (isFullFootmenArmorSet(victim) && directEntity instanceof AbstractArrow arrow) {
            event.setCanceled(true);
            arrow.setDeltaMovement(victim.getLookAngle().scale(1.8D));
            arrow.setOwner(victim);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            return;
        }

        Entity trueAttacker = source.getEntity();
        if (trueAttacker instanceof LivingEntity attackerLiving
                && isFullFootmenArmorSet(attackerLiving)
                && (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))) {
            event.setAmount(event.getAmount() + 1.0F);
        }
    }
}