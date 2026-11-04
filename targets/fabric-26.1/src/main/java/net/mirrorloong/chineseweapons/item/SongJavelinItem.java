package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;


public class SongJavelinItem extends FourBlockReachSwordItem {

    private final float attackDamage;
    private final float attackSpeed;
    private final int maxDurability;

    public SongJavelinItem(ToolMaterial tier, int displayedAttackDamage, float speed,
                           boolean fireResistant, int durabilityOffset) {
        super(
                tier,
                Math.round(displayedAttackDamage + tier.attackDamageBonus()),
                speed - 4.0F,
                properties(tier, fireResistant, durabilityOffset)
        );
        this.attackDamage = (float) displayedAttackDamage + tier.attackDamageBonus();
        this.attackSpeed = speed;
        this.maxDurability = tier.durability() + durabilityOffset;
    }

    public int getMaxDamage(ItemStack stack) {
        return maxDurability;
    }

    private static Item.Properties properties(ToolMaterial tier, boolean fireResistant, int durabilityOffset) {
        int finalDurability = tier.durability() + durabilityOffset;
        Item.Properties properties = new Item.Properties().durability(finalDurability);
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
        int riptideLevel = enchantLevel(level, Enchantments.RIPTIDE, stack);

        if (riptideLevel > 0) {
            if (!player.isInWaterOrRain()) {
                return InteractionResult.FAIL;
            }
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResult.FAIL;
        }

        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    private static int enchantLevel(Level level, net.minecraft.resources.ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key,
            ItemStack stack) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack))
                .orElse(0);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return false;
        int useDuration = this.getUseDuration(stack, entity) - timeLeft;
        if (useDuration < 5) return false;

        int riptideLevel = enchantLevel(level, Enchantments.RIPTIDE, stack);
        if (riptideLevel > 0 && player.isInWaterOrRain()) {
            performRiptide(player, stack, riptideLevel);
            return true;
        }

        float charge = Math.min(useDuration / 20.0F, 1.0F);
        float velocity = this.attackSpeed * charge;

        if (!level.isClientSide()) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(2, player, player.getUsedItemHand());
            }

            //复制并传参
            ItemStack copyStack = stack.copy();
            SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, copyStack);
            projectile.setBaseDamage(this.attackDamage * charge);
            projectile.setCritArrow(charge >= 0.9F);

            int loyalty = enchantLevel(level, Enchantments.LOYALTY, stack);
            projectile.setLoyaltyLevel(loyalty);
            boolean hasChanneling = enchantLevel(level, Enchantments.CHANNELING, stack) > 0;
            projectile.setHasChanneling(hasChanneling);

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

        //flyyyy
        Vec3 look = player.getLookAngle();
        Vec3 currentVel = player.getDeltaMovement();
        double factor = 0.75 * (riptideLevel + 1);
        Vec3 newVel = look.scale(factor).add(currentVel);
        player.setDeltaMovement(newVel);
        player.hurtMarked = true;

        stack.hurtAndBreak(2, player, player.getUsedItemHand());

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
