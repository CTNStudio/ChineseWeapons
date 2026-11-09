package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.item.SongStandingShield;
import net.mirrorloong.chineseweapons.procedures.weapon.DaggerAxeItemFierceHookProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemStopRunProcedure;

import java.util.Optional;
import java.util.function.Supplier;

import static net.mirrorloong.chineseweapons.item.GlaiveItemBase.isGlaiveItem;

public class ShieldActionPacket {

    public ShieldActionPacket() {}
    public ShieldActionPacket(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            SongStandingShieldEntity target = findTarget(player);
            if (target != null) {
                retrieve(player, target);
                return;
            }

            ItemStack main = player.getMainHandItem();

            if (DaggerAxeItemFierceHookProcedure.isDaggerAxe(main)) {
                DaggerAxeItemFierceHookProcedure.execute(
                        player.level(),
                        player.getX(), player.getY(), player.getZ(),
                        player,
                        main
                );
                return;
            }

            if (isGlaiveItem(main)) {
                LivingEntity livingTarget = findLivingTarget(player, 3.0D);
                if (livingTarget != null) {
                    GlaiveItemStopRunProcedure.execute(livingTarget, player, main);
                }
                return;
            }

            if (main.getItem() instanceof SongStandingShield) {
                SongStandingShield.placeShield(player, main);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static LivingEntity findLivingTarget(ServerPlayer player, double reach) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 end = eye.add(look.scale(reach));

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (LivingEntity living : player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(reach),
                e -> e != player && e.isAlive())) {

            AABB box = living.getBoundingBox().inflate(0.3D);
            Optional<Vec3> hit = box.clip(eye, end);
            if (hit.isEmpty()) continue;

            double dist = eye.distanceToSqr(hit.get());
            if (dist < bestDist) {
                bestDist = dist;
                best = living;
            }
        }
        return best;
    }

    // 收回
    private static void retrieve(ServerPlayer player, SongStandingShieldEntity shield) {
        ItemStack recovered = shield.getStoredItem();
        if (!recovered.isEmpty()) {
            int dmg = (int) Math.max(0, Math.min(recovered.getMaxDamage(),
                    recovered.getMaxDamage() - shield.getHealth()));
            recovered.setDamageValue(dmg);

            InteractionHand hand = getShieldHand(player);
            if (hand != null && player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, recovered);
                shield.discard();
                return;
            }

            if (hasEmptySlot(player)) {
                boolean ok = player.addItem(recovered);
                if (!ok || recovered.isEmpty()) {
                    player.drop(recovered, false);
                }
            } else {
                player.drop(recovered, false);
            }
        }
        shield.discard();
    }


    private static boolean hasEmptySlot(ServerPlayer player) {
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i).isEmpty()) return true;
        }
        return false;
    }

    private static InteractionHand getShieldHand(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof SongStandingShield) return InteractionHand.MAIN_HAND;
        if (player.getOffhandItem().getItem() instanceof SongStandingShield) return InteractionHand.OFF_HAND;
        return null;
    }

    private static SongStandingShieldEntity findTarget(ServerPlayer player) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double reach = 5.0D;
        Vec3 end = eye.add(look.scale(reach));

        SongStandingShieldEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (SongStandingShieldEntity shield : player.level().getEntitiesOfClass(
                SongStandingShieldEntity.class, player.getBoundingBox().inflate(reach))) {

            AABB box = shield.getBoundingBox().inflate(0);

            Optional<Vec3> hit = box.clip(eye, end);
            if (hit.isEmpty()) continue;

            double dist = eye.distanceToSqr(hit.get());
            if (dist < bestDist) {
                bestDist = dist;
                best = shield;
            }
        }
        return best;
    }
}