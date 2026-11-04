package net.mirrorloong.chineseweapons.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.rules.GuDuoRules;

public class GuDuoItem extends SixBlockReachSwordItem {

    private final int maxDurability;

    public GuDuoItem(Tier tier, int displayedAttackDamage, float speed,
                     boolean fireResistant, int durabilityOffset) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.getAttackDamageBonus()),
                speed - 4.0F,
                makeProperties(fireResistant, durabilityOffset)
        );
        this.maxDurability = durabilityOffset;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxDurability;
    }

    private static Properties makeProperties(boolean fireResistant, int durability) {
        Properties properties = new Properties().durability(durability);
        if (fireResistant) {
            properties.fireResistant();
        }
        return properties;
    }

    @Override
    public boolean canPerformSweepAttack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        GuDuoRules.onHurtEnemy(stack, target, attacker, settings(stack));
        ChineseWeapons.gameplay().damageItem(stack, 2, attacker);
        return super.hurtEnemy(stack, target, attacker);
    }

    private static GuDuoRules.Settings settings(ItemStack stack) {
        return new GuDuoRules.Settings(
                WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_ARMORBLINDNESS, 4.0),
                WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_HURTDOWNWARD, 1.0),
                (int) Math.round(WeaponScriptSettings.getArmorThreshold(
                        stack.getItem(), WeaponScriptSettings.ALLWeapon.GUDUO_HURTSKILLTICK, 10.0)),
                WeaponScriptSettings.getChance(
                        stack.getItem(), WeaponScriptSettings.Skill.GUDUO_STOPRIDING, 0.3F));
    }

    //客户端渲染
    @Mod.EventBusSubscriber(
            modid = ChineseWeapons.MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.FORGE,
            value = Dist.CLIENT
    )
    public static class Client {

        private static final java.util.Set<Integer> PUSHED = new java.util.HashSet<>();

        @SubscribeEvent
        public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
            LivingEntity entity = event.getEntity();
            int id = entity.getId();

            Long start = GuDuoRules.SQUASH.get(id);
            if (start == null) {
                return;
            }

            long elapsed = System.currentTimeMillis() - start;
            if (elapsed >= GuDuoRules.SQUASH_TOTAL_MS) {
                GuDuoRules.SQUASH.remove(id);
                return;
            }

            float scaleY = GuDuoRules.squashScaleY(elapsed);
            if (scaleY == 1.0F) {
                return;
            }

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.scale(1.0F, scaleY, 1.0F);

            PUSHED.add(id);
        }

        @SubscribeEvent
        public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
            int id = event.getEntity().getId();
            if (PUSHED.remove(id)) {
                event.getPoseStack().popPose();
            }
        }
    }
}
