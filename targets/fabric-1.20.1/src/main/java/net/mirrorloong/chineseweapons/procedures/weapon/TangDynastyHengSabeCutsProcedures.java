package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

import net.minecraft.world.InteractionResultHolder;

import java.util.*;

public class TangDynastyHengSabeCutsProcedures {

    private static final List<RegistryEntry<Item>> CAN_REGISTRY_OBJECTS = List.of(
            ChineseweaponsModItems.WOODEN_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.STONE_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.IRON_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.NETHERITE_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.GOLDEN_TANG_DYNASTY_HENG_SABER,
            ChineseweaponsModItems.DIAMOND_TANG_DYNASTY_HENG_SABER
    );

    private static Set<Item> canSet;

    private static Set<Item> getCanSet() {
        if (canSet == null) {
            Set<Item> items = new HashSet<>();
            for (RegistryEntry<Item> regObj : CAN_REGISTRY_OBJECTS) {
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

    private static final int DH_TICK = 46;


    public static InteractionResultHolder<ItemStack> rightClickTrigger(Player player, InteractionHand hand) {
        UUID uuid = player.getUUID();
        long now = System.currentTimeMillis();
        ItemStack mainHand = player.getMainHandItem();
        Item holdItem = mainHand.getItem();

        if (!getCanSet().contains(holdItem)) return InteractionResultHolder.pass(mainHand);

        if (player.getCooldowns().isOnCooldown(holdItem)) return InteractionResultHolder.pass(mainHand);
        if (itemAnimData.containsKey(uuid)) return InteractionResultHolder.pass(mainHand);

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
        return InteractionResultHolder.pass(mainHand);
    }

    private static void executeStab(Player player, Item bindWeapon) {
        ItemStack handItem = player.getMainHandItem();
        if (handItem.isEmpty()) return;
        if (!getCanSet().contains(handItem.getItem())) return;

        player.getCooldowns().addCooldown(bindWeapon, 10);
        handItem.hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));

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
                    target.addEffect(new MobEffectInstance(ChineseWeaponsModEffects.CusEffectSupplier.get(), 60, 0, false, true));

                    if (player instanceof ServerPlayer sp) {
                        ResourceLocation rootRl = new ResourceLocation(ChineseWeapons.MOD_ID, "get_ancient_gem/xiao_tang_dynasty_heng_saber");
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
        if (passed <= 8) {
            return (float) passed / 8;
        }
        if (passed <= 38) {
            return 1.0F;
        }

        int retractPass = passed - 38;
        return 1.0F - ((float) retractPass / 8);
    }
}
