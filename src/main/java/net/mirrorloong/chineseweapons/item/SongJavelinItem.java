package net.mirrorloong.chineseweapons.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.event.ChineseWeaponsCombatEvents;

public class SongJavelinItem extends FourBlockReachSwordItem {

    private final float attackDamage;
    private final float attackSpeed;
    private final int maxDurability;

    public SongJavelinItem(Tier tier, int displayedAttackDamage, float speed,
                           boolean fireResistant, int durabilityOffset) {
        super(
                tier,
                Math.round(displayedAttackDamage + tier.getAttackDamageBonus()),
                speed - 4.0F,
                properties(tier, fireResistant, durabilityOffset)
        );
        this.attackDamage = (float) displayedAttackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = speed;
        this.maxDurability = tier.getUses() + durabilityOffset;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxDurability;
    }

    private static Properties properties(Tier tier, boolean fireResistant, int durabilityOffset) {
        int finalDurability = tier.getUses() + durabilityOffset;
        Properties properties = new Properties().durability(finalDurability);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int riptideLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RIPTIDE, stack);

        if (riptideLevel > 0) {
            if (!player.isInWaterOrRain()) {
                return InteractionResultHolder.fail(stack);
            }
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        int useDuration = this.getUseDuration(stack) - timeLeft;
        if (useDuration < 5) return;

        int riptideLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RIPTIDE, stack);
        if (riptideLevel > 0 && player.isInWaterOrRain()) {
            performRiptide(player, stack, riptideLevel);
            return;
        }

        float charge = Math.min(useDuration / 20.0F, 1.0F);
        float velocity = this.attackSpeed * charge;

        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            }

            //复制并传参
            ItemStack copyStack = stack.copy();
            SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, copyStack);
            projectile.setBaseDamage(this.attackDamage * charge);
            projectile.setKnockback(1);
            projectile.setCritArrow(charge > 1.0F);

            int loyalty = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.LOYALTY, stack);
            projectile.setLoyaltyLevel(loyalty);
            boolean hasChanneling = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.CHANNELING, stack) > 0;
            projectile.setHasChanneling(hasChanneling);
            int pierce = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, stack);
            projectile.setPierceLevel((byte) pierce);

            projectile.shootFromRotation(player, player.getXRot() - 20.0F, player.getYRot(), 0.0F, velocity, 1.0F);
            level.addFreshEntity(projectile);

            level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.getCooldowns().addCooldown(this, 8);
    }

    private void performRiptide(Player player, ItemStack stack, int riptideLevel) {
        if (player.level().isClientSide) return;

        //flyyyy

        Vec3 look = player.getLookAngle();
        Vec3 currentVel = player.getDeltaMovement();
        double factor = 0.75 * (riptideLevel + 1);
        Vec3 newVel = look.scale(factor).add(currentVel);
        player.setDeltaMovement(newVel);
        player.hurtMarked = true;

        stack.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));

        switch (riptideLevel) {
            case 1 -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0F, 1.0F);
            case 2 -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_2, SoundSource.PLAYERS, 1.0F, 1.0F);
            default -> player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        player.getCooldowns().addCooldown(this, 20);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!result || target.level().isClientSide() || !(attacker instanceof ServerPlayer player) || !target.isAlive()) {
            return result;
        }

        if (player.getVehicle() != null && target.getVehicle() == null
                && !ChineseWeaponsCombatEvents.isTethered(target)) {
            ChineseWeaponsCombatEvents.tryStartMountedDrag(player, target);
        }
        return result;
    }
}