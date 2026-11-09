package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.rules.TangSaberRules;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** NeoForge 1.21.1 event wiring for the heng saber stab. */
@EventBusSubscriber(modid = ChineseWeapons.MOD_ID)
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

    @SubscribeEvent
    public static void rightClickTrigger(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        boolean tracked = getCanSet().contains(player.getMainHandItem().getItem());

        float baseDamage = (float) ChineseWeapons.gameplay().itemAttackDamage(player.getMainHandItem());
        TangSaberRules.rightClick(player, tracked, horseOwnedBy(player), baseDamage);;
    }

    @SubscribeEvent
    public static void tickUpdate(PlayerTickEvent.Post event) {
        TangSaberRules.tick(event.getEntity());
    }

    public static float getAnimationProgress(UUID uid) {
        return TangSaberRules.animationProgress(uid);
    }

    private static java.util.function.Predicate<Entity> horseOwnedBy(Player player) {
        return entity -> entity instanceof Horse horse
                && horse.isTamed()
                && horse.getOwnerReference() != null
                && player.getUUID().equals(horse.getOwnerReference().getUUID());
    }
}
