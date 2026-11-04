package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.forge.ForgeGameplayCompat;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.rules.ArmorRules;
import net.mirrorloong.chineseweapons.rules.CombatRules;

/**
 * Forge 1.18.2 event wiring for the shared armour combat rules. The 1.18
 * damage API has no {@code DamageTypes} keys, so the sweet-berry hit is
 * recognised by its message id exactly as before.
 */
@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ChineseWeaponsCombatEvents {
    private static final String TETHER_OWNER = "chineseweapons:tether_owner";
    private static final String TETHER_UNTIL = "chineseweapons:tether_until";

    private ChineseWeaponsCombatEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventTetheredAttacks(LivingAttackEvent event) {
        LivingEntity target = (LivingEntity) event.getEntity();
        CombatRules.IncomingDamage damage = describe(event.getSource(), event.getAmount());
        if (!CombatRules.allowDamage(target, damage, ForgeGameplayCompat.ARMOR_GAIN,
                ChineseWeaponsCombatEvents::isTethered, false)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void applyArmorCombatRules(LivingHurtEvent event) {
        LivingEntity wearer = (LivingEntity) event.getEntity();
        CombatRules.Adjustment adjustment = CombatRules.adjustDamage(wearer,
                describe(event.getSource(), event.getAmount()), ForgeGameplayCompat.ARMOR_GAIN);
        if (adjustment.cancel()) {
            event.setCanceled(true);
        } else {
            event.setAmount(adjustment.amount());
        }
    }

    @SubscribeEvent
    public static void applyArmorTickRules(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        CombatRules.playerTick(event.player, ForgeGameplayCompat.ARMOR_GAIN);
    }

    @SubscribeEvent
    public static void tickTetheredEntity(LivingEvent.LivingUpdateEvent event) {
        LivingEntity target = (LivingEntity) event.getEntity();
        if (!(target.level instanceof ServerLevel serverLevel) || !isTethered(target)) {
            return;
        }

        CompoundTag data = target.getPersistentData();
        if (!data.hasUUID(TETHER_OWNER)) {
            clearTether(target);
            return;
        }

        Entity owner = serverLevel.getEntity(data.getUUID(TETHER_OWNER));
        if (!(owner instanceof Player playerOwner)
                || !owner.isAlive()
                || serverLevel.getGameTime() >= data.getLong(TETHER_UNTIL)) {
            clearTether(target);
            return;
        }

        CombatRules.pullTowardsOwner(target, playerOwner);
    }

    @SubscribeEvent
    public static void tryEscapeTether(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!ChineseWeapons.gameplay().isClientSide(entity.level)
                && isTethered(entity)
                && entity.getRandom().nextFloat() < 0.30F) {
            clearTether(entity);
        }
    }

    public static boolean tryStartMountedDrag(Player owner, LivingEntity target) {
        if (!CombatRules.canStartTether(owner, target, ForgeGameplayCompat.ARMOR_GAIN)) {
            return false;
        }

        CompoundTag data = target.getPersistentData();
        data.putUUID(TETHER_OWNER, owner.getUUID());
        data.putLong(TETHER_UNTIL, owner.level.getGameTime() + CombatRules.TETHER_DURATION_TICKS);
        ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.CusEffectSupplier,
                CombatRules.TETHER_DURATION_TICKS, 0, false, true);
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return ArmorRules.isFullFineScaleSet(entity, ForgeGameplayCompat.ARMOR_GAIN);
    }

    private static void clearTether(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        data.remove(TETHER_OWNER);
        data.remove(TETHER_UNTIL);
    }

    public static boolean isTethered(LivingEntity entity) {
        return entity.getPersistentData().contains(TETHER_UNTIL);
    }

    private static CombatRules.IncomingDamage describe(DamageSource source, float amount) {
        LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
        Entity direct = source.getDirectEntity();
        boolean thrownTrident = direct instanceof ThrownTrident;
        boolean arrow = direct instanceof AbstractArrow && !thrownTrident;
        boolean fireArrow = arrow && ((AbstractArrow) direct).isOnFire();
        boolean sweetBerryBush = "sweetBerryBush".equals(source.getMsgId());
        Runnable deflect = arrow
                ? () -> {
                    AbstractArrow shot = (AbstractArrow) direct;
                    shot.setDeltaMovement(shot.getDeltaMovement().scale(10.0D));
                    shot.hurtMarked = true;
                }
                : null;

        return new CombatRules.IncomingDamage(amount, attacker, arrow, fireArrow,
                CombatRules.isSwordOrTrident(attacker, thrownTrident),
                CombatRules.isMaceSmash(source.getMsgId(), attacker),
                sweetBerryBush, deflect);
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        var level = event.getWorld();
        var center = event.getExplosion().getPosition();
        AABB area = new AABB(center, center).inflate(32);
        level.getEntitiesOfClass(SongStandingShieldEntity.class, area)
                .forEach(shield -> shield.handleExplosion(event));
    }
}
