package net.mirrorloong.chineseweapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.item.SongStandingShield;

import java.util.Optional;
import java.util.function.Supplier;

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
            if (main.getItem() instanceof SongStandingShield) {
                SongStandingShield.placeShield(player, main);
            }
        });
        ctx.get().setPacketHandled(true);
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

            AABB box = shield.getBoundingBox().inflate(0.5);
            Optional<Vec3> hit = box.clip(eye, end);

            double dist;
            if (hit.isPresent()) {
                dist = eye.distanceToSqr(hit.get());
            } else {
                Vec3 center = shield.position().add(0, shield.getBbHeight() / 2, 0);
                Vec3 toEntity = center.subtract(eye);
                double proj = toEntity.dot(look);
                if (proj < 0 || proj > reach) continue;
                Vec3 closest = eye.add(look.scale(proj));
                dist = closest.distanceToSqr(center);
                if (dist > 1.5 * 1.5) continue;
            }

            if (dist < bestDist) {
                bestDist = dist;
                best = shield;
            }
        }
        return best;
    }
}