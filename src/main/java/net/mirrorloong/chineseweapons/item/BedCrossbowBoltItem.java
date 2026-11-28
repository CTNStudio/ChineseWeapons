package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SHARPNESS
                || enchantment == Enchantments.FIRE_ASPECT
                || enchantment == Enchantments.POWER_ARROWS
                || enchantment == Enchantments.PUNCH_ARROWS
                || enchantment == Enchantments.FLAMING_ARROWS
                || enchantment == Enchantments.INFINITY_ARROWS) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
