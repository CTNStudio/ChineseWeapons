package net.mirrorloong.chineseweapons.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.content.RepairEntry;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;

public final class ForgeWeaponTier implements Tier {
    private final WeaponDefinition definition;

    public ForgeWeaponTier(WeaponDefinition definition) {
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
    public int getLevel() {
        return definition.tierLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return definition.enchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        Item self = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ChineseWeapons.MOD_ID, definition.id()));
        List<RepairEntry> repairs = definition.repairs();
        boolean hasTag = repairs.stream().anyMatch(RepairEntry::tag);

        if (!hasTag) {
            List<ItemStack> stacks = new ArrayList<>();
            for (RepairEntry entry : repairs) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.id()));
                if (item != null) {
                    stacks.add(new ItemStack(item));
                }
            }
            if (self != null) {
                stacks.add(new ItemStack(self));
            }
            return Ingredient.of(stacks.toArray(ItemStack[]::new));
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (RepairEntry entry : repairs) {
            if (entry.tag()) {
                ingredients.add(Ingredient.of(ItemTags.create(new ResourceLocation(entry.id()))));
            } else {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.id()));
                if (item != null) {
                    ingredients.add(Ingredient.of(new ItemStack(item)));
                }
            }
        }
        if (self != null) {
            ingredients.add(Ingredient.of(new ItemStack(self)));
        }
        return CompoundIngredient.of(ingredients.toArray(Ingredient[]::new));
    }
}
