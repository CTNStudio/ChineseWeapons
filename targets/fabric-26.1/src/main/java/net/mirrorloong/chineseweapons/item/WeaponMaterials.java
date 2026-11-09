package net.mirrorloong.chineseweapons.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.mirrorloong.chineseweapons.content.RepairEntry;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;

/**
 * 26.1 replaced the {@code Tier} interface with the {@link ToolMaterial}
 * record, so the per-weapon anonymous tiers the shared sources inline are
 * rebuilt here from the same numbers.
 */
public final class WeaponMaterials {
    private WeaponMaterials() {
    }

    /**
     * Rebuilds the tier for one entry of the shared weapon table. 26.1 keeps the
     * repair ingredient on the material as a tag, so the closest vanilla tool
     * material tag is selected from the definition's repair entries.
     */
    public static ToolMaterial of(WeaponDefinition definition) {
        return switch (materialKey(definition)) {
            case "wood" -> of(definition, BlockTags.INCORRECT_FOR_WOODEN_TOOL, ItemTags.WOODEN_TOOL_MATERIALS);
            case "stone" -> of(definition, BlockTags.INCORRECT_FOR_STONE_TOOL, ItemTags.STONE_TOOL_MATERIALS);
            case "iron" -> of(definition, BlockTags.INCORRECT_FOR_IRON_TOOL, ItemTags.IRON_TOOL_MATERIALS);
            case "gold" -> of(definition, BlockTags.INCORRECT_FOR_GOLD_TOOL, ItemTags.GOLD_TOOL_MATERIALS);
            case "diamond" -> of(definition, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIAMOND_TOOL_MATERIALS);
            case "netherite" -> of(definition, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, ItemTags.NETHERITE_TOOL_MATERIALS);
            default -> throw new IllegalArgumentException("Unknown weapon material for " + definition.id());
        };
    }

    private static ToolMaterial of(WeaponDefinition definition, TagKey<Block> incorrectBlocksForDrops,
            TagKey<Item> repairItems) {
        return new ToolMaterial(incorrectBlocksForDrops, definition.uses(), definition.speed(),
                definition.attackDamageBonus(), definition.enchantmentValue(), repairItems);
    }

    private static String materialKey(WeaponDefinition definition) {
        for (RepairEntry entry : definition.repairs()) {
            String id = entry.id();
            if (entry.tag()) {
                if (id.equals("minecraft:planks")) {
                    return "wood";
                }
                if (id.equals("forge:stone")) {
                    return "stone";
                }
            } else {
                switch (id) {
                    case "minecraft:iron_ingot" -> {
                        return "iron";
                    }
                    case "minecraft:gold_ingot" -> {
                        return "gold";
                    }
                    case "minecraft:diamond" -> {
                        return "diamond";
                    }
                    case "minecraft:netherite_ingot" -> {
                        return "netherite";
                    }
                    default -> {
                    }
                }
            }
        }
        return switch (definition.tierLevel()) {
            case 0 -> "wood";
            case 1 -> "stone";
            case 2 -> "iron";
            case 3 -> "diamond";
            default -> "netherite";
        };
    }

    public static ToolMaterial of(int durability, float speed, float attackDamageBonus, int enchantmentValue,
            TagKey<Block> incorrectBlocksForDrops, TagKey<Item> repairItems) {
        return new ToolMaterial(incorrectBlocksForDrops, durability, speed, attackDamageBonus,
                enchantmentValue, repairItems);
    }

    public static ToolMaterial iron(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_IRON_TOOL, ItemTags.IRON_TOOL_MATERIALS);
    }

    public static ToolMaterial gold(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_GOLD_TOOL, ItemTags.GOLD_TOOL_MATERIALS);
    }

    public static ToolMaterial diamond(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIAMOND_TOOL_MATERIALS);
    }

    public static ToolMaterial netherite(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_NETHERITE_TOOL, ItemTags.NETHERITE_TOOL_MATERIALS);
    }

    public static ToolMaterial stone(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_STONE_TOOL, ItemTags.STONE_TOOL_MATERIALS);
    }

    public static ToolMaterial wood(int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        return of(durability, speed, attackDamageBonus, enchantmentValue,
                BlockTags.INCORRECT_FOR_WOODEN_TOOL, ItemTags.WOODEN_TOOL_MATERIALS);
    }
}
