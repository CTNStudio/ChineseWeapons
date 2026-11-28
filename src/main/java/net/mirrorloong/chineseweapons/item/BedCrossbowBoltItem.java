package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class BedCrossbowBoltItem extends Item {

    public enum BoltKind {
        SPEAR_THREE_SWORD("spear_three_sword_bolt", 120.0F, 45.0F),
        TREAD_PEG("tread_peg_bolt", 80.0F, 30.0F);

        private final String name;
        private final float range;
        private final float damage;

        BoltKind(String name, float range, float damage) {
            this.name = name;
            this.range = range;
            this.damage = damage;
        }

        public String getName() {
            return this.name;
        }

        public float getRange() {
            return this.range;
        }

        public float getDamage() {
            return this.damage;
        }

        public static BoltKind byId(int id) {
            BoltKind[] values = values();
            if (id < 0 || id >= values.length) {
                return SPEAR_THREE_SWORD;
            }
            return values[id];
        }
    }

    private final BoltKind kind;

    public BedCrossbowBoltItem(BoltKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    public BoltKind getKind() {
        return this.kind;
    }

    public static BoltKind kindOf(ItemStack stack) {
        if (stack.getItem() instanceof BedCrossbowBoltItem bolt) {
            return bolt.getKind();
        }
        return BoltKind.SPEAR_THREE_SWORD;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        ResourceKey<Enchantment> key = enchantment.unwrapKey().orElse(null);
        if (key == Enchantments.SHARPNESS
                || key == Enchantments.FIRE_ASPECT
                || key == Enchantments.POWER
                || key == Enchantments.PUNCH
                || key == Enchantments.FLAME
                || key == Enchantments.INFINITY) {
            return true;
        }
        return stack.is(Items.ENCHANTED_BOOK) || enchantment.value().isSupportedItem(stack);
    }
}
