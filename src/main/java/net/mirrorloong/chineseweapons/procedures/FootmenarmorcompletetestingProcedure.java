package net.mirrorloong.chineseweapons.procedures;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.core.registries.Registries;

import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

public class FootmenarmorcompletetestingProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_HELMET.get()) {

            if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_CHESTPLATE.get()
                    || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_CHESTPLATE.get()
                    || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_CHESTPLATE.get()
                    || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_CHESTPLATE.get()) {

                if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_LEGGINGS.get()
                        || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_LEGGINGS.get()
                        || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_LEGGINGS.get()
                        || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_LEGGINGS.get()) {

                    if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_BOOTS.get()
                            || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_BOOTS.get()
                            || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_BOOTS.get()
                            || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_BOOTS.get()) {

                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 1, false, false));
                        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 4) {
                            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                                _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, true));
                            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                                _entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1, false, true));
                            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                                _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2, false, true));
                        }
                        if (entity.isSprinting()) {
                            if (entity instanceof Player) {
                                checkCollisions((Player) entity);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void checkCollisions(Player player) {
        var nearbyEntities = player.level().getEntities(player,
                player.getBoundingBox().inflate(1.0, 0.5, 1.0));

        boolean hitAnyEntity = false;

        for (Entity entity : nearbyEntities) {
            if (entity == player || !(entity instanceof LivingEntity)) continue;

            if (player.getBoundingBox().intersects(entity.getBoundingBox())) {
                handleCollision(player, (LivingEntity) entity);
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
}
