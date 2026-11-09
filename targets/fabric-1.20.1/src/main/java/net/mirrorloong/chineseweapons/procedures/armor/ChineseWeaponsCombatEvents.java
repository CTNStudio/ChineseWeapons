package net.mirrorloong.chineseweapons.procedures.armor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.fabric.FabricGameplayCompat;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.rules.ArmorRules;
import net.mirrorloong.chineseweapons.rules.CombatRules;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fabric 1.20.1 wiring for the shared armour combat rules. Fabric has no
 * per-entity tick event and no persistent-data API, so the tether state lives
 * in a server-side map and expires from the world tick; the damage logic itself
 * is the shared {@link CombatRules}.
 */
public final class ChineseWeaponsCombatEvents {

    private static final Map<UUID, Tether> TETHERS = new ConcurrentHashMap<>();

    private ChineseWeaponsCombatEvents() {
    }

    private record Tether(UUID owner, long until) {
    }

    public static boolean allowDamage(LivingEntity target, DamageSource source, float amount) {
        CombatRules.IncomingDamage damage = describe(source, amount);
        return CombatRules.allowDamage(target, damage, FabricGameplayCompat.ARMOR_GAIN,
                ChineseWeaponsCombatEvents::isTethered, true);
    }

    public static float modifyDamageAmount(LivingEntity wearer, DamageSource source, float amount) {
        return CombatRules.adjustDamage(wearer, describe(source, amount), FabricGameplayCompat.ARMOR_GAIN).amount();
    }

    public static void playerTick(Player player) {
        CombatRules.playerTick(player, FabricGameplayCompat.ARMOR_GAIN);
    }

    public static void tickTethered(ServerLevel level) {
        if (TETHERS.isEmpty()) {
            return;
        }

        long now = level.getGameTime();
        for (Iterator<Map.Entry<UUID, Tether>> iterator = TETHERS.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<UUID, Tether> entry = iterator.next();
            Tether tether = entry.getValue();

            Entity targetEntity = level.getEntity(entry.getKey());
            if (!(targetEntity instanceof LivingEntity target)) {
                if (now >= tether.until()) {
                    iterator.remove();
                }
                continue;
            }

            if (now >= tether.until()) {
                iterator.remove();
                continue;
            }

            Entity ownerEntity = level.getEntity(tether.owner());
            if (!(ownerEntity instanceof Player playerOwner) || !ownerEntity.isAlive()) {
                iterator.remove();
                continue;
            }

            CombatRules.pullTowardsOwner(target, playerOwner);
        }
    }

    public static boolean tryStartMountedDrag(Player owner, LivingEntity target) {
        if (!CombatRules.canStartTether(owner, target, FabricGameplayCompat.ARMOR_GAIN)) {
            return false;
        }

        TETHERS.put(target.getUUID(),
                new Tether(owner.getUUID(), owner.level().getGameTime() + CombatRules.TETHER_DURATION_TICKS));
        ChineseWeapons.gameplay().applyEffect(target, ChineseWeaponsModEffects.CusEffectSupplier,
                CombatRules.TETHER_DURATION_TICKS, 0, false, true);
        return true;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity) {
        return ArmorRules.isFullFineScaleSet(entity, FabricGameplayCompat.ARMOR_GAIN);
    }

    /** Original {@code LivingJumpEvent}: a tethered entity has a 30% chance to escape on jump. */
    public static void tryEscapeTether(LivingEntity entity) {
        if (ChineseWeapons.gameplay().isClientSide(ChineseWeapons.gameplay().level(entity))) {
            return;
        }
        if (!isTethered(entity)) {
            return;
        }
        if (ChineseWeapons.gameplay().nextFloat(ChineseWeapons.gameplay().level(entity)) >= 0.30F) {
            return;
        }
        TETHERS.remove(entity.getUUID());
    }

    public static boolean isTethered(LivingEntity entity) {
        return TETHERS.containsKey(entity.getUUID());
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
}
