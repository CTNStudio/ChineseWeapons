package net.mirrorloong.chineseweapons.content;

import java.util.List;

/**
 * Everything a generic glaive or dagger-axe needs to recreate the old
 * per-material item class.
 */
public record WeaponDefinition(
        String id,
        WeaponFamily family,
        int uses,
        float speed,
        float attackDamageBonus,
        int tierLevel,
        int enchantmentValue,
        List<RepairEntry> repairs,
        boolean repairSelf,
        int attackDamageModifier,
        float attackSpeedModifier,
        boolean fireResistant,
        boolean craftingRemainingItem,
        boolean repairable) {
}
