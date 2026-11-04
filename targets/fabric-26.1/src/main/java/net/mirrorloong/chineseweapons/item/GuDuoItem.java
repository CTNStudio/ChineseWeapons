package net.mirrorloong.chineseweapons.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.ModSounds;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.effects.EffectHolders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mirrorloong.chineseweapons.spi.ModPacket;

public class GuDuoItem extends SixBlockReachSwordItem {

    // 状态
    public static final Map<Integer, Long> SQUASH = new ConcurrentHashMap<>();
    public static final long T1 = 300L, T2 = 800L;
    public static final float MIN = 0.6F;

    private final int maxDurability;

    public GuDuoItem(ToolMaterial tier, int displayedAttackDamage, float speed,
                     boolean fireResistant, int durabilityOffset) {
        super(
                tier,
                Math.round(displayedAttackDamage - 1.0F - tier.attackDamageBonus()),
                speed - 4.0F,
                makeProperties(fireResistant, durabilityOffset)
        );
        this.maxDurability = durabilityOffset;
    }

    public int getMaxDamage(ItemStack stack) {
        return maxDurability;
    }

    private static Item.Properties makeProperties(boolean fireResistant, int durability) {
        Item.Properties p = new Item.Properties().durability(durability);
        if (fireResistant) p.fireResistant();
        return p;
    }

    @Override
    public boolean canPerformSweepAttack(ItemStack stack) {
        return false;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            if (attacker instanceof Player player && !player.getCooldowns().isOnCooldown(stack)) {
                if (!attacker.onGround()) {
                    HurtFlyyy(stack, target, attacker);
                } else if (attacker.isShiftKeyDown()) {
                    HurtStopHo(stack, target, attacker);
                } else {
                    DFHurt(stack, target, attacker);
                }
            }
        }
        stack.hurtAndBreak(2, attacker, InteractionHand.MAIN_HAND);
        super.hurtEnemy(stack, target, attacker);
    }

    private static void DFHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(EffectHolders.of(ChineseWeaponsModEffects.BleedEffectSupplier.get()), 60, 0));

        double GL = WeaponScriptSettings.getArmorThreshold(
                stack.getItem(),
                WeaponScriptSettings.ALLWeapon.GUDUO_ARMORBLINDNESS, 4.0);

        if (attacker.getAttributeValue(Attributes.ARMOR) <= GL) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
        }
    }

    private static void HurtFlyyy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.level().playSound(
                null,
                attacker.blockPosition(),
                ModSounds.GUDUO.get(),
                SoundSource.PLAYERS,
                0.2F,
                1.0F
        );

        float weaponDamage = attacker instanceof Player p
                ? (float) p.getAttributeValue(Attributes.ATTACK_DAMAGE)
                : 2.0F;
        target.hurt(attacker.damageSources().mobAttack(attacker), weaponDamage + 2.0F);

        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 0));

        if (target.isInWater()) {
            target.setAirSupply(target.getAirSupply() - 3);
            target.push(0.0D, -4D, 0.0D);
        } else {
            Vec3 dir = target.position().subtract(attacker.position());
            Vec3 back = new Vec3(dir.x, 0, dir.z).normalize().scale(1D);

            target.teleportTo(
                    target.getX() + back.x,
                    target.getY() + 1.5D,
                    target.getZ() + back.z
            );

            double downward_o = WeaponScriptSettings.getArmorThreshold(
                    stack.getItem(),
                    WeaponScriptSettings.ALLWeapon.GUDUO_HURTDOWNWARD, 1D);

            double downward = 0.3D + attacker.level().getRandom().nextDouble() * (downward_o - 0.1D);
            target.push(0.0D, -downward, 0.0D);

            // 压扁
            if (target.level() instanceof ServerLevel sl) {
                SquashPacket pkt = new SquashPacket(target.getId());
                for (ServerPlayer sp : sl.players()) {
                    if (sp.distanceToSqr(target) < 4096) {
                        ChineseWeapons.network().sendToPlayer(sp, pkt);
                    }
                }
            }
        }

        double tick = WeaponScriptSettings.getArmorThreshold(
                stack.getItem(),
                WeaponScriptSettings.ALLWeapon.GUDUO_HURTSKILLTICK, 10D);

        if (attacker instanceof Player p) {
            p.getCooldowns().addCooldown(stack, (int) Math.round(tick));
        }
    }

    private static void HurtStopHo(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.isPassenger()) return;

        float chance = WeaponScriptSettings.getChance(
                stack.getItem(),
                WeaponScriptSettings.Skill.GUDUO_STOPRIDING, 0.3F);
        if (attacker.level().getRandom().nextFloat() >= chance) return;

        target.stopRiding();

        float damage = attacker instanceof Player p
                ? (float) p.getAttributeValue(Attributes.ATTACK_DAMAGE)
                : 1.0F;
        target.hurt(attacker.damageSources().mobAttack(attacker), damage);

        if (attacker instanceof Player p) {
            p.getCooldowns().addCooldown(stack, 60);
        }

        if (target.getVehicle() != null) {
            target.getVehicle().hurt(attacker.damageSources().mobAttack(attacker), damage);
        }
    }

    //心跳包
    public static float scaleY(long t) {
        if (t < T1) {
            // easeOutCubic
            float x = t / (float) T1;
            float ease = 1.0F - (float) Math.pow(1.0F - x, 3);
            return 1.0F - (1.0F - MIN) * ease;
        }
        // easeInCubic
        float x = (t - T1) / (float) (T2 - T1);
        return MIN + (1.0F - MIN) * (x * x * x);
    }

    //数据包
    public record SquashPacket(int id) implements ModPacket {
        @Override
        public void encode(FriendlyByteBuf b) {
            b.writeVarInt(id);
        }

        public static SquashPacket decode(FriendlyByteBuf b) {
            return new SquashPacket(b.readVarInt());
        }

        @Override
        public void handle(ServerPlayer sender) {
            SQUASH.put(id, System.currentTimeMillis());
        }
    }
}
