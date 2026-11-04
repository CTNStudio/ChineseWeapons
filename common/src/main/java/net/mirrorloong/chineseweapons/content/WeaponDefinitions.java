package net.mirrorloong.chineseweapons.content;

import java.util.Arrays;

/**
 * Data table replacing the deleted per-material glaive and dagger-axe classes.
 */
public final class WeaponDefinitions {
    private WeaponDefinitions() {
    }

    public static final WeaponDefinition WOODEN_GLAIVE = weapon(
            "wooden_glaive", WeaponFamily.GLAIVE, 83, 5f, 3f, 0, 18, 3, -2.2f, false, false, true,
            RepairEntry.tag("minecraft:planks"));
    public static final WeaponDefinition STONE_GLAIVE = weapon(
            "stone_glaive", WeaponFamily.GLAIVE, 185, 7f, 4f, 1, 8, 3, -2.4f, false, false, true,
            RepairEntry.tag("forge:stone"));
    public static final WeaponDefinition IRON_GLAIVE = weapon(
            "iron_glaive", WeaponFamily.GLAIVE, 350, 7f, 5f, 2, 5, 3, -2.6f, false, false, true,
            RepairEntry.item("minecraft:iron_ingot"));
    public static final WeaponDefinition GOLDEN_GLAIVE = weapon(
            "golden_glaive", WeaponFamily.GLAIVE, 45, 15f, 3f, 0, 25, 3, -2.2f, false, false, true,
            RepairEntry.item("minecraft:gold_ingot"));
    public static final WeaponDefinition DIAMOND_GLAIVE = weapon(
            "diamond_glaive", WeaponFamily.GLAIVE, 2186, 11f, 6f, 3, 13, 3, -2.8f, false, false, true,
            RepairEntry.item("minecraft:diamond"));
    public static final WeaponDefinition NETHERITE_GLAIVE = weapon(
            "netherite_glaive", WeaponFamily.GLAIVE, 2844, 12f, 7f, 4, 18, 3, -3f, true, false, true,
            RepairEntry.item("minecraft:netherite_ingot"));
    public static final WeaponDefinition GREENLOONG_GLAIVE = weapon(
            "green_loong_glaive", WeaponFamily.GLAIVE, 2855, 14f, 8f, 4, 20, 3, -3f, true, false, true);

    public static final WeaponDefinition WOODEN_DAGGER_AXE = weapon(
            "wooden_dagger_axe", WeaponFamily.DAGGER_AXE, 118, 4f, 3f, 0, 17, 3, -2.1f, false, true, false,
            RepairEntry.tag("minecraft:planks"));
    public static final WeaponDefinition STONE_DAGGER_AXE = weapon(
            "stone_dagger_axe", WeaponFamily.DAGGER_AXE, 262, 6f, 4f, 1, 7, 3, -2.5f, false, false, true,
            RepairEntry.tag("forge:stone"));
    public static final WeaponDefinition IRON_DAGGER_AXE = weapon(
            "iron_dagger_axe", WeaponFamily.DAGGER_AXE, 500, 6f, 5f, 2, 4, 3, -2.5f, false, false, true,
            RepairEntry.item("minecraft:iron_ingot"));
    public static final WeaponDefinition GOLDEN_DAGGER_AXE = weapon(
            "golden_dagger_axe", WeaponFamily.DAGGER_AXE, 64, 14f, 3f, 0, 24, 3, -2.5f, false, false, true,
            RepairEntry.item("minecraft:gold_ingot"));
    public static final WeaponDefinition DIAMOND_DAGGER_AXE = weapon(
            "diamond_dagger_axe", WeaponFamily.DAGGER_AXE, 3601, 10f, 6f, 3, 12, 3, -2.1f, false, false, true,
            RepairEntry.item("minecraft:diamond"));
    public static final WeaponDefinition NETHERITE_DAGGER_AXE = weapon(
            "netherite_dagger_axe", WeaponFamily.DAGGER_AXE, 3047, 11f, 7f, 4, 17, 3, -2.1f, true, false, true,
            RepairEntry.item("minecraft:netherite_ingot"));

    private static WeaponDefinition weapon(String id, WeaponFamily family, int uses, float speed,
                                           float attackDamageBonus, int tierLevel, int enchantmentValue,
                                           int attackDamageModifier, float attackSpeedModifier,
                                           boolean fireResistant, boolean craftingRemainingItem,
                                           boolean repairable, RepairEntry... repairs) {
        return new WeaponDefinition(id, family, uses, speed, attackDamageBonus, tierLevel,
                enchantmentValue, Arrays.asList(repairs), true, attackDamageModifier,
                attackSpeedModifier, fireResistant, craftingRemainingItem, repairable);
    }
}
