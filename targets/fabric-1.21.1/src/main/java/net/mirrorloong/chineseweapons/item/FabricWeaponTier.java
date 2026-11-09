package net.mirrorloong.chineseweapons.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.RepairEntry;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;

/**
 * Tier built from a {@link WeaponDefinition}. The 1.21 tier interface no longer
 * carries a numeric level, so the incorrect-blocks tag is derived from the
 * definition id instead of {@code tierLevel} (wood and gold share level 0 but
 * use different tags).
 */
public final class FabricWeaponTier implements Tier {
    private final WeaponDefinition definition;
    private final TagKey<Block> incorrectBlocksForDrops;

    public FabricWeaponTier(WeaponDefinition definition) {
        this.definition = definition;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops(definition.id());
    }

    @Override
    public int getUses() {
        return definition.uses();
    }

    @Override
    public float getSpeed() {
        return definition.speed();
    }

    @Override
    public float getAttackDamageBonus() {
        return definition.attackDamageBonus();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return definition.enchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        List<ItemStack> stacks = new ArrayList<>();
        for (RepairEntry entry : definition.repairs()) {
            if (entry.tag()) {
                stacks.addAll(List.of(Ingredient.of(TagKey.create(Registries.ITEM,
                        repairTagId(entry.id()))).getItems()));
            } else {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.id()));
                if (item != null && item != Items.AIR) {
                    stacks.add(new ItemStack(item));
                }
            }
        }
        if (definition.repairSelf()) {
            Item self = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, definition.id()));
            if (self != null && self != Items.AIR) {
                stacks.add(new ItemStack(self));
            }
        }
        return Ingredient.of(stacks.stream());
    }

    /**
     * The shared repair table still names the Forge tag namespace; Fabric and
     * NeoForge 1.21 use the conventional {@code c} namespace, and this target
     * ships {@code data/c/tags/item/stone.json}.
     */
    private static ResourceLocation repairTagId(String id) {
        if (id.startsWith("forge:")) {
            return ResourceLocation.fromNamespaceAndPath("c", id.substring("forge:".length()));
        }
        return ResourceLocation.parse(id);
    }

    private static TagKey<Block> incorrectBlocksForDrops(String id) {
        return switch (id.substring(0, id.indexOf('_'))) {
            case "wooden" -> BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case "stone" -> BlockTags.INCORRECT_FOR_STONE_TOOL;
            case "iron" -> BlockTags.INCORRECT_FOR_IRON_TOOL;
            case "golden" -> BlockTags.INCORRECT_FOR_GOLD_TOOL;
            case "diamond" -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            case "netherite", "green" -> BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            default -> throw new IllegalArgumentException("Unknown weapon material: " + id);
        };
    }
}
