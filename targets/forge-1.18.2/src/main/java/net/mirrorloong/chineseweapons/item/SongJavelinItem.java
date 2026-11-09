package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.rules.CombatRules;
import net.mirrorloong.chineseweapons.rules.JavelinRules;

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
        return JavelinRules.USE_DURATION_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return switch (JavelinRules.use(player, hand, stack)) {
            case FAIL -> InteractionResultHolder.fail(stack);
            case CONSUME -> InteractionResultHolder.consume(stack);
        };
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }
        int usedTicks = this.getUseDuration(stack) - timeLeft;
        JavelinRules.releaseUsing(player, stack, usedTicks, this.attackDamage, this.attackSpeed,
                (shot, stackCopy) -> spawnProjectile(level, player, shot, stackCopy));
    }

    private static void spawnProjectile(Level level, Player player, JavelinRules.Shot shot,
                                        ItemStack stackCopy) {
        SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, stackCopy);
        projectile.setBaseDamage(shot.baseDamage());
        projectile.setKnockback(1);
        projectile.setCritArrow(shot.critical());
        projectile.setLoyaltyLevel(shot.loyalty());
        projectile.setHasChanneling(shot.channeling());
        projectile.setPierceLevel((byte) shot.pierce());
        projectile.shootFromRotation(player, player.getXRot() - 50.0F, player.getYRot(), 0.0F,
                shot.velocity(), 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!result) {
            return false;
        }
        CombatRules.startMountedDrag(target, attacker);
        return true;
    }
}
