package net.mirrorloong.chineseweapons.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;

/**
 * 26.1 replaced {@code Tier}/{@code SwordItem} with data components, so weapon
 * classes build their properties through these helpers.
 */
public final class ModWeapons {
    private ModWeapons() {
    }

    public static Item.Properties sword(ToolMaterial material, float attackDamage, float attackSpeed,
            Item.Properties properties) {
        return properties.sword(material, attackDamage, attackSpeed);
    }

    public static Item.Properties sword(ToolMaterial material, float attackDamage, float attackSpeed) {
        return new Item.Properties().sword(material, attackDamage, attackSpeed);
    }

    /**
     * Builds the 26.1 tool material from a shared weapon definition. 26.1 needs
     * the incorrect-for-drops and repair tag keys, which have no counterpart in
     * {@link WeaponDefinition}, so the material family is taken from the
     * registry id prefix (gold shares tier level 0 with wood).
     */
    public static ToolMaterial toolMaterial(WeaponDefinition definition) {
        int uses = definition.uses();
        float speed = definition.speed();
        float damage = definition.attackDamageBonus();
        int enchantment = definition.enchantmentValue();
        return switch (family(definition.id())) {
            case "wooden" -> new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, uses, speed, damage,
                    enchantment, ItemTags.WOODEN_TOOL_MATERIALS);
            case "stone" -> new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, uses, speed, damage,
                    enchantment, ItemTags.STONE_TOOL_MATERIALS);
            case "iron" -> new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, uses, speed, damage,
                    enchantment, ItemTags.IRON_TOOL_MATERIALS);
            case "golden" -> new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, uses, speed, damage,
                    enchantment, ItemTags.GOLD_TOOL_MATERIALS);
            case "diamond" -> new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, uses, speed, damage,
                    enchantment, ItemTags.DIAMOND_TOOL_MATERIALS);
            case "netherite" -> new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, uses, speed, damage,
                    enchantment, ItemTags.NETHERITE_TOOL_MATERIALS);
            default -> throw new IllegalArgumentException("Unknown weapon material: " + definition.id());
        };
    }

    private static String family(String id) {
        if (id.startsWith("green_loong_")) {
            return "netherite";
        }
        return id.substring(0, id.indexOf('_'));
    }
}
