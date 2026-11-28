package net.mirrorloong.chineseweapons.procedures.weapon;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import java.util.*;

public class TangDynastyHengSabeCutsProcedures {

    private static final List<RegistryObject<Item>> CAN_REGISTRY_OBJECTS = List.of(
            ChineseweaponsModItems.WOODEN_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.STONE_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.IRON_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.NETHERITE_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.GOLDEN_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.DIAMOND_TANG_DYNASTY_HENG_SABER
    );

    private static Set<Item> canSet;

    private static boolean registered;

    private static Set<Item> getCanSet() {
        if (canSet == null) {
            Set<Item> items = new HashSet<>();
            for (RegistryObject<Item> regObj : CAN_REGISTRY_OBJECTS) {
                items.add(regObj.get());
            }
            canSet = Collections.unmodifiableSet(items);
        }
        return canSet;
    }

    private static final Map<UUID, Long> lastRightClick = new HashMap<>();
    private static final Map<UUID, Boolean> hasFirstClick = new HashMap<>();

    private record StabAnimData(Item bindItem, int tickLeft, boolean playing) {}
    private static final Map<UUID, StabAnimData> itemAnimData = new HashMap<>();

            private static final float SPEED_MULTIPLIER = 1.5F;
    private static final int DH_TICK = Math.round(46 / SPEED_MULTIPLIER);
    private static final int PHASE_START_TICK = Math.max(1, Math.round(8 / SPEED_MULTIPLIER));
    private static final int PHASE_HOLD_END_TICK = Math.round(38 / SPEED_MULTIPLIER);
    private static final int COOLDOWN_TICKS = Math.max(1, Math.round(10 / SPEED_MULTIPLIER));

        public static void init() {
        if (registered) {
            return;
        }
        registered = true;

        //old api @SubscribeEvent PlayerInteractEvent.RightClickItem -> new api UseItemCallback.EVENT
        UseItemCallback.EVENT.register(TangDynastyHengSabeCutsProcedures::onRightClickItem);

                ServerTickEvents.END_SERVER_TICK.register(TangDynastyHengSabeCutsProcedures::onServerTick);
    }

    public static InteractionResultHolder<ItemStack> onRightClickItem(Player player, Level world, InteractionHand hand) {
        UUID uuid = player.getUUID();
        long now = System.currentTimeMillis();
        ItemStack mainHand = player.getMainHandItem();
        Item holdItem = mainHand.getItem();

        if (!getCanSet().contains(holdItem)) return InteractionResultHolder.pass(player.getItemInHand(hand));

        if (player.getCooldowns().isOnCooldown(holdItem)) return InteractionResultHolder.pass(player.getItemInHand(hand));
        if (itemAnimData.containsKey(uuid)) return InteractionResultHolder.pass(player.getItemInHand(hand));

        boolean firstPressed = hasFirstClick.getOrDefault(uuid, false);
        long lastTime = lastRightClick.getOrDefault(uuid, 0L);

        if (!firstPressed) {
            hasFirstClick.put(uuid, true);
            lastRightClick.put(uuid, now);
        } else {
            if (now - lastTime <= 400) {
                executeStab(player, holdItem);
            }
            hasFirstClick.put(uuid, false);
        }
                return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private static void executeStab(Player player, Item bindWeapon) {
        ItemStack handItem = player.getMainHandItem();
        if (handItem.isEmpty()) return;
        if (!getCanSet().contains(handItem.getItem())) return;

        player.getCooldowns().addCooldown(bindWeapon, COOLDOWN_TICKS);

        Vec3 look = player.getLookAngle();
        Vec3 playerPos = player.position();

        AABB killBox = new AABB(
                playerPos.x - 0.5F,
                playerPos.y,
                playerPos.z - 0.5F,
                playerPos.x + 0.5F + look.x * 3,
                playerPos.y + 2,
                playerPos.z + 0.5F + look.z * 3
        );

        player.level().getEntities(player, killBox, e -> e instanceof LivingEntity living && living != player && !(living instanceof Horse horse && horse.isTamed() && horse.getOwnerUUID() != null && horse.getOwnerUUID().equals(player.getUUID())))
                .forEach(entity -> {
                    LivingEntity target = (LivingEntity) entity;
                    float baseDamage = (float) handItem.getAttributeModifiers(EquipmentSlot.MAINHAND)
                            .get(Attributes.ATTACK_DAMAGE)
                            .stream()
                            .mapToDouble(AttributeModifier::getAmount)
                            .sum();
                    float dmg = baseDamage + 2;
                    target.hurt(player.damageSources().playerAttack(player), dmg);
                    handItem.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(), 60, 0, false, true));

                    if (player instanceof ServerPlayer sp) {
                        ResourceLocation rootRl = new ResourceLocation(ChineseweaponsMod.MODID, "get_ancient_gem/xiao_tang_dynasty_heng_saber");
                        Advancement rootAdv = sp.server.getAdvancements().getAdvancement(rootRl);
                        if(rootAdv != null) {
                            AdvancementProgress ap = sp.getAdvancements().getOrStartProgress(rootAdv);
                            if (!ap.isDone()) {
                                for (String criteria : ap.getRemainingCriteria()) {
                                    sp.getAdvancements().award(rootAdv, criteria);
                                }
                            }
                        }
                    }
                });

        itemAnimData.put(player.getUUID(), new StabAnimData(bindWeapon, DH_TICK, true));
    }

        private static void onServerTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            tickUpdate(player);
        }
    }

    public static void tickUpdate(Player player) {
        UUID uid = player.getUUID();
        if (!itemAnimData.containsKey(uid)) return;
        StabAnimData anim = itemAnimData.get(uid);

        boolean interrupt = !player.getMainHandItem().is(anim.bindItem())
                || player.getMainHandItem().isEmpty()
                || player.swinging
                || !player.getOffhandItem().isEmpty();

        if (interrupt) {
            itemAnimData.remove(uid);
            return;
        }

        int tickLeft = anim.tickLeft() - 1;
        if (tickLeft <= 0) {
            itemAnimData.remove(uid);
            return;
        }
        itemAnimData.put(uid, new StabAnimData(anim.bindItem(), tickLeft, true));
    }

    public static float getAnimationProgress(UUID uid) {
        StabAnimData anim = itemAnimData.get(uid);
        if (anim == null) return 0.0F;

        int tickLeft = anim.tickLeft();
        int passed = DH_TICK - tickLeft;
        if (passed <= PHASE_START_TICK) {
            return (float) passed / PHASE_START_TICK;
        }
        if (passed <= PHASE_HOLD_END_TICK) {
            return 1.0F;
        }

        int retractPass = passed - PHASE_HOLD_END_TICK;
        int retractTicks = Math.max(1, DH_TICK - PHASE_HOLD_END_TICK);
        return 1.0F - ((float) retractPass / retractTicks);
    }
}
