package net.mirrorloong.chineseweapons.rules;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

import java.util.function.Predicate;

/**
 * Armour-set and held-weapon detection shared by all targets.
 *
 * <p>The {@code armorGain} predicate is the target's script setting for the
 * {@code ARMOR_GAIN} logic flag (KubeJS {@code setArmorGain}); it is passed in
 * because the settings store itself is still owned by each target.</p>
 */
public final class ArmorRules {

    public enum Family {
        BLACK_CHUI("black_chui_armor_"),
        EARLY_MING_GUANG("early_tang_dynasty_ming_guang_armor_"),
        MIDDLE_MING_GUANG("middle_tang_dynasty_ming_guang_armor_"),
        LATE_MING_GUANG("late_tang_dynasty_ming_guang_armor_"),
        MOUNTAIN_CHARACTER("mountain_character_armor_"),
        FINE_SCALE("fine_scale_armor_");

        private final String itemPathMarker;

        Family(String itemPathMarker) {
            this.itemPathMarker = itemPathMarker;
        }

        public String itemPathMarker() {
            return this.itemPathMarker;
        }
    }

    private ArmorRules() {
    }

    public static Family primaryFamily(LivingEntity entity, Predicate<ItemStack> armorGain) {
        Family best = null;
        int bestCount = 0;
        for (Family family : Family.values()) {
            int count = countPieces(entity, family, armorGain);
            if (count > bestCount) {
                best = family;
                bestCount = count;
            }
        }
        return best;
    }

    public static int countPieces(LivingEntity entity, Family family, Predicate<ItemStack> armorGain) {
        int count = 0;
        for (GameplayCompat.ArmorSlot slot : GameplayCompat.ArmorSlot.values()) {
            ItemStack armor = ChineseWeapons.gameplay().armorPiece(entity, slot);
            if (isArmorPiece(armor, family, armorGain)) {
                count++;
            }
        }
        if (isMingGuang(family) && isMingGuangHelmetSubstitute(
                ChineseWeapons.gameplay().armorPiece(entity, GameplayCompat.ArmorSlot.HEAD), armorGain)) {
            count++;
        }
        return count;
    }

    public static boolean isMingGuang(Family family) {
        return family == Family.EARLY_MING_GUANG
                || family == Family.MIDDLE_MING_GUANG
                || family == Family.LATE_MING_GUANG;
    }

    public static boolean isFullFineScaleSet(LivingEntity entity, Predicate<ItemStack> armorGain) {
        return countPieces(entity, Family.FINE_SCALE, armorGain) == 4;
    }

    public static boolean isFullFootmenArmorSet(LivingEntity entity, Predicate<ItemStack> armorGain) {
        GameplayCompat compat = ChineseWeapons.gameplay();
        ItemStack head = compat.armorPiece(entity, GameplayCompat.ArmorSlot.HEAD);
        ItemStack chest = compat.armorPiece(entity, GameplayCompat.ArmorSlot.CHEST);
        ItemStack legs = compat.armorPiece(entity, GameplayCompat.ArmorSlot.LEGS);
        ItemStack feet = compat.armorPiece(entity, GameplayCompat.ArmorSlot.FEET);

        boolean validHelmet = armorGain.test(head) && (head.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRON_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get()
                || head.getItem() == ChineseweaponsModItems.IRON_BOHAI_HELMET.get());

        boolean validChest = armorGain.test(chest) && (chest.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_CHESTPLATE.get()
                || chest.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_CHESTPLATE.get());

        boolean validLegs = armorGain.test(legs) && (legs.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_LEGGINGS.get()
                || legs.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_LEGGINGS.get());

        boolean validFeet = armorGain.test(feet) && (feet.getItem() == ChineseweaponsModItems.DIAMONDFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.IRONFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.GOLDENFOOTMENARMOR_BOOTS.get()
                || feet.getItem() == ChineseweaponsModItems.NETHERITEFOOTMENARMOR_BOOTS.get());

        return validHelmet && validChest && validLegs && validFeet;
    }

    public static boolean isHoldingGlaive(LivingEntity entity) {
        return entity.getMainHandItem().getItem() instanceof GlaiveItem;
    }

    /** Raw path-marker set check used by the dagger-axe hook (no script gate). */
    public static boolean isFullArmorSet(LivingEntity entity, String itemPathMarker) {
        int count = 0;
        for (GameplayCompat.ArmorSlot slot : GameplayCompat.ArmorSlot.values()) {
            ItemStack armor = ChineseWeapons.gameplay().armorPiece(entity, slot);
            if (ChineseWeapons.gameplay().itemPath(armor.getItem()).contains(itemPathMarker)) {
                count++;
            }
        }
        return count == 4;
    }

    public static boolean isMingGuangHelmetSubstitute(ItemStack helmet, Predicate<ItemStack> armorGain) {
        if (helmet.isEmpty()) {
            return false;
        }

        boolean isSubstitute = helmet.getItem() == ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.IRON_SUANNI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.DIAMOND_BOHAI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.IRON_BOHAI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get()
                || helmet.getItem() == ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get();

        return isSubstitute && armorGain.test(helmet);
    }

    private static boolean isArmorPiece(ItemStack stack, Family family, Predicate<ItemStack> armorGain) {
        if (stack.isEmpty()) {
            return false;
        }

        if (!ChineseWeapons.gameplay().itemPath(stack.getItem()).contains(family.itemPathMarker())) {
            return false;
        }

        return armorGain.test(stack);
    }
}
