package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.rules.CombatRules;
import net.mirrorloong.chineseweapons.rules.JavelinRules;

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
        Item.Properties properties = ItemIds.apply(new Item.Properties()).durability(finalDurability);
        return fireResistant ? properties.fireResistant() : properties;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return JavelinRules.USE_DURATION_TICKS;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return switch (JavelinRules.use(player, hand, stack)) {
            case FAIL -> InteractionResult.FAIL;
            case CONSUME -> InteractionResult.CONSUME;
        };
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        int usedTicks = this.getUseDuration(stack, entity) - timeLeft;
        return JavelinRules.releaseUsing(player, stack, usedTicks, this.attackDamage, this.attackSpeed,
                (shot, stackCopy) -> spawnProjectile(level, player, shot, stackCopy));
    }

    private static void spawnProjectile(Level level, Player player, JavelinRules.Shot shot,
                                        ItemStack stackCopy) {
        SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, stackCopy);
        projectile.setBaseDamage(shot.baseDamage());
        projectile.setCritArrow(shot.critical());
        projectile.setLoyaltyLevel(shot.loyalty());
        projectile.setHasChanneling(shot.channeling());
        projectile.shootFromRotation(player, player.getXRot() - 50.0F, player.getYRot(), 0.0F,
                shot.velocity(), 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        CombatRules.startMountedDrag(target, attacker);
    }
}
