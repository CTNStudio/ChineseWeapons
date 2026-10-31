package net.mirrorloong.chineseweapons.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public class SongJavelinItem extends FourBlockReachSwordItem {

    @OnlyIn(Dist.CLIENT)
    public static final class EnumParams {
        public static final EnumProxy<HumanoidModel.ArmPose> CHARGING_JAVELIN_POSE_PROXY =
                new EnumProxy<>(
                        HumanoidModel.ArmPose.class,
                        false, // twoHanded
                        (IArmPoseTransformer) (model, entity, arm) -> {
                            if (arm == HumanoidArm.RIGHT) {
                                model.rightArm.xRot = (float) Math.toRadians(-180);
                                model.rightArm.yRot = (float) Math.toRadians(180);
                                model.rightArm.zRot = (float) Math.toRadians(-20);
                            } else {
                                model.leftArm.xRot = (float) Math.toRadians(-180);
                                model.leftArm.yRot = (float) Math.toRadians(180);
                                model.leftArm.zRot = (float) Math.toRadians(20);
                            }
                        }
                );
    }

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
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        HolderLookup.RegistryLookup<Enchantment> enchants =
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int riptideLevel = EnchantmentHelper.getItemEnchantmentLevel(
                enchants.getOrThrow(Enchantments.RIPTIDE), stack);

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
        int useDuration = this.getUseDuration(stack, entity) - timeLeft;
        if (useDuration < 5) return;

        HolderLookup.RegistryLookup<Enchantment> enchants =
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        int riptideLevel = EnchantmentHelper.getItemEnchantmentLevel(
                enchants.getOrThrow(Enchantments.RIPTIDE), stack);
        if (riptideLevel > 0 && player.isInWaterOrRain()) {
            performRiptide(player, stack, riptideLevel);
            return;
        }

        float charge = Math.min(useDuration / 20.0F, 1.0F);
        float velocity = this.attackSpeed * charge;

        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
            }

            //复制并传参
            ItemStack copyStack = stack.copy();
            SongJavelinProjectile projectile = new SongJavelinProjectile(level, player, copyStack);
            projectile.setBaseDamage(this.attackDamage * charge);

            projectile.setCritArrow(charge >= 0.9F);

            int loyalty = EnchantmentHelper.getItemEnchantmentLevel(
                    enchants.getOrThrow(Enchantments.LOYALTY), stack);
            projectile.setLoyaltyLevel(loyalty);
            boolean hasChanneling = EnchantmentHelper.getItemEnchantmentLevel(
                    enchants.getOrThrow(Enchantments.CHANNELING), stack) > 0;
            projectile.setHasChanneling(hasChanneling);

            projectile.shootFromRotation(player, player.getXRot() - 50.0F, player.getYRot(),
                    0.0F, velocity, 1.0F);
            level.addFreshEntity(projectile);

            level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW.value(),
                    SoundSource.PLAYERS, 1.0F, 1.0F);

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

        stack.hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
        switch (riptideLevel) {
            case 1 -> player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TRIDENT_RIPTIDE_1.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            case 2 -> player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TRIDENT_RIPTIDE_2.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            default -> player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TRIDENT_RIPTIDE_3.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
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

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT)
    public static class ClientRegistration {

        @SubscribeEvent
        public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
            event.registerItem(new ClientExtensions(),
                    ChineseweaponsModItems.WOODEN_SONG_JAVENLIN,
                    ChineseweaponsModItems.STONE_SONG_JAVENLIN,
                    ChineseweaponsModItems.GOLDEN_SONG_JAVENLIN,
                    ChineseweaponsModItems.IRON_SONG_JAVENLIN,
                    ChineseweaponsModItems.DIAMOND_SONG_JAVENLIN,
                    ChineseweaponsModItems.NETHERITE_SONG_JAVENLIN);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientExtensions implements IClientItemExtensions {

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!entityLiving.isUsingItem()) {
                return null;
            }
            if (itemStack.getItem() instanceof SongJavelinItem
                    && entityLiving.getUsedItemHand() == hand
                    && entityLiving.getUseItemRemainingTicks() > 0) {
                return HumanoidModel.ArmPose.valueOf("CHINESEWEAPONS_CHARGING_JAVELIN");
            }
            return null;
        }

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
            poseStack.mulPose(Axis.XP.rotationDegrees(-55.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(k * 35.3F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(k * -9.785F));

            float useDuration = itemInHand.getUseDuration(player);
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
            poseStack.mulPose(Axis.YN.rotationDegrees(k * 45.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-5.0F));

            return true;
        }
    }
}