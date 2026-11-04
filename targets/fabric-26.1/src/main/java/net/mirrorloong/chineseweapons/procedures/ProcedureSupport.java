package net.mirrorloong.chineseweapons.procedures;

import java.util.function.Predicate;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Helpers for 26.1 API moves that affect several shared procedures: the
 * registry types advancements through {@link AdvancementHolder}, the armour
 * slot view no longer exists, and the server field of {@link ServerPlayer} is
 * private.
 */
public final class ProcedureSupport {
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD };

    private ProcedureSupport() {
    }

    public static void award(ServerPlayer player, Identifier id) {
        MinecraftServer server = player.level().getServer();
        if (server == null) {
            return;
        }
        AdvancementHolder advancement = server.getAdvancements().get(id);
        if (advancement == null) {
            return;
        }
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) {
            return;
        }
        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    public static boolean isWearing(LivingEntity entity, Predicate<ItemStack> predicate) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (predicate.test(entity.getItemBySlot(slot))) {
                return true;
            }
        }
        return false;
    }

    public static int countWearing(LivingEntity entity, Predicate<ItemStack> predicate) {
        int count = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (predicate.test(entity.getItemBySlot(slot))) {
                count++;
            }
        }
        return count;
    }
}
