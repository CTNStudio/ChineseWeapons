package net.mirrorloong.chineseweapons.procedures.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.rules.TangSaberRules;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Forge 1.20.1 event wiring for the heng saber stab. */
@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        Player player = (Player) event.getEntity();
        boolean tracked = getCanSet().contains(player.getMainHandItem().getItem());

        float baseDamage = (float) ChineseWeapons.gameplay().itemAttackDamage(player.getMainHandItem());
        TangSaberRules.rightClick(player, tracked, horseOwnedBy(player), baseDamage);;
    }

    @SubscribeEvent
    public static void tickUpdate(TickEvent.PlayerTickEvent event) {
        TangSaberRules.tick(event.player);
    }

    public static float getAnimationProgress(UUID uid) {
        return TangSaberRules.animationProgress(uid);
    }

    private static java.util.function.Predicate<Entity> horseOwnedBy(Player player) {
        return entity -> entity instanceof Horse horse
                && horse.isTamed()
                && horse.getOwnerUUID() != null
                && horse.getOwnerUUID().equals(player.getUUID());
    }
}
