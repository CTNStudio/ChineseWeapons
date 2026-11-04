package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.neoforge.NeoForgeGameplayCompat;
import net.mirrorloong.chineseweapons.rules.ArmorRules;
import net.mirrorloong.chineseweapons.rules.CombatRules;

/** NeoForge 1.21.1 event wiring for the shared armour combat rules. */
@EventBusSubscriber(modid = ChineseWeapons.MOD_ID)
public final class ChineseWeaponsCombatEvents {
    private static final String TETHER_OWNER = "chineseweapons:tether_owner";
    private static final String TETHER_UNTIL = "chineseweapons:tether_until";

    private ChineseWeaponsCombatEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventTetheredAttacks(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        CombatRules.IncomingDamage damage = describe(event.getSource(), event.getAmount());
        if (!CombatRules.allowDamage(target, damage, NeoForgeGameplayCompat.ARMOR_GAIN,
                ChineseWeaponsCombatEvents::isTethered, true)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void applyArmorCombatRules(LivingDamageEvent.Pre event) {
        CombatRules.Adjustment adjustment = CombatRules.adjustDamage(event.getEntity(),
                describe(event.getSource(), event.getNewDamage()), NeoForgeGameplayCompat.ARMOR_GAIN);
        if (!adjustment.cancel()) {
            event.setNewDamage(adjustment.amount());
        }
    }

    @SubscribeEvent
    public static void applyArmorTickRules(PlayerTickEvent.Post event) {
        CombatRules.playerTick(event.getEntity(), NeoForgeGameplayCompat.ARMOR_GAIN);
    }

    @SubscribeEvent
    public static void tickTetheredEntity(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity target)) {
            return;
        }
        if (!(target.level() instanceof ServerLevel serverLevel) || !isTethered(target)) {
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
        LivingEntity entity = event.getEntity();
        if (!ChineseWeapons.gameplay().isClientSide(entity.level())
                && isTethered(entity)
                && entity.getRandom().nextFloat() < 0.30F) {
            clearTether(entity);
        }
    }

    public static boolean tryStartMountedDrag(Player owner, LivingEntity target) {
        if (!CombatRules.canStartTether(owner, target, NeoForgeGameplayCompat.ARMOR_GAIN)) {
            return false;
        }

        CompoundTag data = target.getPersistentData();
        data.putUUID(TETHER_OWNER, owner.getUUID());
        data.putLong(TETHER_UNTIL, owner.level().getGameTime() + CombatRules.TETHER_DURATION_TICKS);
        ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.CusEffectSupplier,
                CombatRules.TETHER_DURATION_TICKS, 0, false, true);
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return ArmorRules.isFullFineScaleSet(entity, NeoForgeGameplayCompat.ARMOR_GAIN);
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
        boolean sweetBerryBush = source.is(DamageTypes.SWEET_BERRY_BUSH);
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
        var level = event.getLevel();
        var center = event.getExplosion().center();
        AABB area = new AABB(center, center).inflate(32);
        level.getEntitiesOfClass(SongStandingShieldEntity.class, area)
                .forEach(shield -> shield.handleExplosion(event));
    }
}
