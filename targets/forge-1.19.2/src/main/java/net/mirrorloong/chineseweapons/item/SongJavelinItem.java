package net.mirrorloong.chineseweapons.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.rules.CombatRules;
import net.mirrorloong.chineseweapons.rules.JavelinRules;

import java.util.function.Consumer;

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
        return UseAnim.CUSTOM;
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

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ClientExtensions());
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientExtensions implements IClientItemExtensions {

        //3
        private static final HumanoidModel.ArmPose CHARGING_JAVELIN_POSE =
                HumanoidModel.ArmPose.create("CHARGING_JAVELIN", false, (model, entity, arm) -> {
                    if (arm == HumanoidArm.RIGHT) {
                        model.rightArm.xRot = (float) Math.toRadians(-180);
                        model.rightArm.yRot = (float) Math.toRadians(180);
                        model.rightArm.zRot = (float) Math.toRadians(-20);
                    } else {
                        model.leftArm.xRot = (float) Math.toRadians(-180);
                        model.leftArm.yRot = (float) Math.toRadians(180);
                        model.leftArm.zRot = (float) Math.toRadians(20);
                    }
                });

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (itemStack.getItem() instanceof SongJavelinItem
                    && entityLiving.getUsedItemHand() == hand
                    && entityLiving.getUseItemRemainingTicks() > 0) {
                return CHARGING_JAVELIN_POSE;
            }
            return null;
        }

        //1
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
                                               ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            if (!(itemInHand.getItem() instanceof SongJavelinItem) || !player.isUsingItem()) {
                return false;
            }
            InteractionHand usingHand = player.getUsedItemHand();
            boolean isRightArm = arm == HumanoidArm.RIGHT;
            boolean usingRightArm = (usingHand == InteractionHand.MAIN_HAND) == isRightArm;
            if (!usingRightArm) return false;

            int k = isRightArm ? 1 : -1;

            poseStack.translate(k * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);

            poseStack.translate(k * -0.5F, 0.7F, 0.1F);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(k * 35.3F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(k * -9.785F));

            float useDuration = itemInHand.getUseDuration();
            float f7 = useDuration - (player.getUseItemRemainingTicks() - partialTick + 1.0F);
            float f11 = f7 / 10.0F;
            if (f11 > 1.0F) f11 = 1.0F;

            if (f11 > 0.1F) {
                float f14 = Mth.sin((f7 - 0.1F) * 1.3F);
                float f17 = f11 - 0.1F;
                float f19 = f14 * f17;
                poseStack.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
            }

            poseStack.translate(0.0F, 0.0F, f11 * 0.2F);
            poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(k * 45.0F));

            //-5
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-5.0F));

            return true;
        }
    }
}