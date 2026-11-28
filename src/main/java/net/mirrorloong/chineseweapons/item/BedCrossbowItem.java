package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID)
public class BedCrossbowItem extends Item {

    public static final int PLACE_DELAY_TICKS = 60;
    private static final double REACH = 6.0D;

    private static final Map<UUID, PendingPlacement> PENDING = new ConcurrentHashMap<>();

    public BedCrossbowItem() {
        super(new Properties().durability(3000));
    }

    public static boolean startPlacement(ServerPlayer player, ItemStack stack) {
        UUID uuid = player.getUUID();
        if (PENDING.containsKey(uuid)) {
            return true;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return false;
        }

        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(REACH));
        BlockHitResult hit = level.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hit.getType() != HitResult.Type.BLOCK || hit.getDirection() != Direction.UP) {
            player.displayClientMessage(Component.translatable(
                    "message.chineseweapons.bed_crossbow.no_space"), true);
            return true;
        }

        BlockPos ground = hit.getBlockPos();
        if (!level.mayInteract(player, ground)) {
            return true;
        }

        double x = ground.getX() + 0.5D;
        double y = hit.getLocation().y;
        double z = ground.getZ() + 0.5D;

        if (!BedCrossbowEntity.canPlaceAt(level, player, x, y, z)) {
            player.displayClientMessage(Component.translatable(
                    "message.chineseweapons.bed_crossbow.no_space"), true);
            return true;
        }

        PendingPlacement pending = new PendingPlacement(
                level, x, y, z, player.getYRot(),
                level.getGameTime(), player.getX(), player.getY(), player.getZ());

        PENDING.put(uuid, pending);
        player.getCooldowns().addCooldown(stack.getItem(), PLACE_DELAY_TICKS);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 1.0F, 0.8F);
        return true;
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || PENDING.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, PendingPlacement>> iterator = PENDING.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, PendingPlacement> entry = iterator.next();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            PendingPlacement pending = entry.getValue();

            if (player == null || !player.isAlive() || player.level() != pending.level) {
                iterator.remove();
                continue;
            }

            if (pending.level.getGameTime() - pending.startTick < PLACE_DELAY_TICKS) {
                freeze(player, pending);
                continue;
            }

            iterator.remove();

            ItemStack held = player.getMainHandItem();
            if (!(held.getItem() instanceof BedCrossbowItem)) {
                continue;
            }
            if (!BedCrossbowEntity.canPlaceAt(pending.level, player, pending.x, pending.y, pending.z)) {
                player.displayClientMessage(Component.translatable(
                        "message.chineseweapons.bed_crossbow.no_space"), true);
                continue;
            }

            BedCrossbowEntity crossbow = ChineseweaponsModEntities.BED_CROSSBOW.get().create(pending.level);
            if (crossbow == null) {
                continue;
            }

            crossbow.moveTo(pending.x, pending.y, pending.z, pending.yaw, 0.0F);
            crossbow.setBaseYaw(pending.yaw);
            crossbow.setStoredItem(held.copy());
            crossbow.setHealth(held.getMaxDamage() - held.getDamageValue());
            crossbow.setYRot(pending.yaw);
            pending.level.addFreshEntity(crossbow);

            pending.level.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
                    pending.x, pending.y + 0.8D, pending.z,
                    18, 0.45D, 0.35D, 0.45D, 0.02D);
            pending.level.playSound(null, pending.x, pending.y, pending.z,
                    SoundEvents.SHIELD_BLOCK, SoundSource.BLOCKS, 1.0F, 0.9F);

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
        }
    }

    private static void freeze(ServerPlayer player, PendingPlacement pending) {
        player.setDeltaMovement(Vec3.ZERO);
        player.fallDistance = 0.0F;
        if (player.distanceToSqr(pending.anchorX, pending.anchorY, pending.anchorZ) > 0.0025D) {
            player.connection.teleport(pending.anchorX, pending.anchorY, pending.anchorZ,
                    player.getYRot(), player.getXRot());
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        PENDING.clear();
    }

    private record PendingPlacement(ServerLevel level, double x, double y, double z,
                                    float yaw, long startTick,
                                    double anchorX, double anchorY, double anchorZ) {
    }
}
