package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;

public class SongJavelinItem extends FourBlockReachSwordItem {

    private final float attackDamage;
    private final float attackSpeed;

    public SongJavelinItem(ToolMaterial material, int displayedAttackDamage, float speed,
                           boolean fireResistant, int durabilityOffset) {
        super(ModWeapons.sword(material,
                Math.round(displayedAttackDamage - 1.0F - material.attackDamageBonus()),
                speed - 4.0F,
                properties(material, fireResistant, durabilityOffset)));
        this.attackDamage = (float) displayedAttackDamage + material.attackDamageBonus();
        this.attackSpeed = speed;
    }

    private static Properties properties(ToolMaterial material, boolean fireResistant, int durabilityOffset) {
        Properties properties = new Properties().durability(material.durability() + durabilityOffset);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int riptideLevel = EnchantmentLevels.level(level, stack, Enchantments.RIPTIDE);

        if (riptideLevel > 0 && !player.isInWaterOrRain()) {
            return InteractionResult.FAIL;
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResult.FAIL;
        }

        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return false;
        int useDuration = this.getUseDuration(stack, entity) - timeLeft;
        if (useDuration < 5) return false;

        int riptideLevel = EnchantmentLevels.level(level, stack, Enchantments.RIPTIDE);
        if (riptideLevel > 0 && player.isInWaterOrRain()) {
            performRiptide(player, stack, riptideLevel);
            return true;
        }

        float charge = Math.min(useDuration / 20.0F, 1.0F);
        float velocity = this.attackSpeed * charge;

        if (!level.isClientSide()) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(2, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND
                        ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            ItemStack copyStack = stack.copy();
            SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, copyStack);
            projectile.setBaseDamage(this.attackDamage * charge);
            projectile.setKnockback(1);
            projectile.setCritArrow(charge >= 0.9F);

            int loyalty = EnchantmentLevels.level(level, stack, Enchantments.LOYALTY);
            projectile.setLoyaltyLevel(loyalty);
            projectile.setHasChanneling(EnchantmentLevels.level(level, stack, Enchantments.CHANNELING) > 0);

            projectile.shootFromRotation(player, player.getXRot() - 50.0F, player.getYRot(), 0.0F, velocity, 1.0F);
            level.addFreshEntity(projectile);

            level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.getCooldowns().addCooldown(stack, 8);
        return true;
    }

    private void performRiptide(Player player, ItemStack stack, int riptideLevel) {
        if (player.level().isClientSide()) return;

        Vec3 look = player.getLookAngle();
        Vec3 currentVel = player.getDeltaMovement();
        double factor = 0.75 * (riptideLevel + 1);
        player.setDeltaMovement(look.scale(factor).add(currentVel));
        player.hurtMarked = true;

        stack.hurtAndBreak(2, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

        switch (riptideLevel) {
            case 1 -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_1.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            case 2 -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_2.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            default -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        player.getCooldowns().addCooldown(stack, 20);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (target.level().isClientSide() || !(attacker instanceof ServerPlayer player) || !target.isAlive()) {
            return;
        }

        if (player.getVehicle() != null && target.getVehicle() == null
                && !ChineseWeaponsCombatEvents.isTethered(target)) {
            ChineseWeaponsCombatEvents.tryStartMountedDrag(player, target);
        }
    }
}
