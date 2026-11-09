package net.mirrorloong.chineseweapons.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.RepairEntry;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;

public final class NeoForgeWeaponTier implements Tier {
    private final WeaponDefinition definition;

    public NeoForgeWeaponTier(WeaponDefinition definition) {
        this.definition = definition;
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
        return switch (definition.id().substring(0, definition.id().indexOf('_'))) {
            case "wooden" -> BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case "stone" -> BlockTags.INCORRECT_FOR_STONE_TOOL;
            case "iron" -> BlockTags.INCORRECT_FOR_IRON_TOOL;
            case "golden" -> BlockTags.INCORRECT_FOR_GOLD_TOOL;
            case "diamond" -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            case "netherite", "green" -> BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            default -> throw new IllegalArgumentException("Unknown weapon material: " + definition.id());
        };
    }

    @Override
    public int getEnchantmentValue() {
        return definition.enchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        List<Ingredient> ingredients = new ArrayList<>();
        for (RepairEntry entry : definition.repairs()) {
            if (entry.tag()) {
                ingredients.add(Ingredient.of(ItemTags.create(ResourceLocation.parse(entry.id()))));
            } else {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.id()));
                if (item != null) {
                    ingredients.add(Ingredient.of(new ItemStack(item)));
                }
            }
        }
        if (definition.repairSelf()) {
            Item self = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, definition.id()));
            if (self != null) {
                ingredients.add(Ingredient.of(new ItemStack(self)));
            }
        }
        if (ingredients.isEmpty()) {
            return Ingredient.EMPTY;
        }
        return CompoundIngredient.of(ingredients.toArray(Ingredient[]::new));
    }
}
