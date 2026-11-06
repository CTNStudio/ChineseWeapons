package net.mirrorloong.chineseweapons.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/** Tier wrapper used when a weapon's durability differs from the base material. */
public final class WeaponTier implements Tier {
    private final Tier base;
    private final int uses;

    private WeaponTier(Tier base, int uses) {
        this.base = base;
        this.uses = uses;
    }

    public static Tier withUses(Tier base, int uses) {
        return new WeaponTier(base, uses);
    }

    @Override public int getUses() { return uses; }
    @Override public float getSpeed() { return base.getSpeed(); }
    @Override public float getAttackDamageBonus() { return base.getAttackDamageBonus(); }
    @Override public TagKey<Block> getIncorrectBlocksForDrops() { return base.getIncorrectBlocksForDrops(); }
    @Override public int getEnchantmentValue() { return base.getEnchantmentValue(); }
    @Override public Ingredient getRepairIngredient() { return base.getRepairIngredient(); }
}
