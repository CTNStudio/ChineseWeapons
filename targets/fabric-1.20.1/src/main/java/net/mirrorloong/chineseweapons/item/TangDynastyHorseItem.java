package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Vanilla 1.20.1 takes the horse armor texture as a {@code String} and has no
 * per-stack {@code getMaxDamage}/{@code getBurnTime} hooks, so the Forge
 * overrides are reduced to their vanilla equivalents.
 */
public class TangDynastyHorseItem extends HorseArmorItem {
    public TangDynastyHorseItem(int defense, ResourceLocation texture) {
        super(defense, texture.toString(), new Item.Properties()
                .stacksTo(1)
                .durability(0));
    }

    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }
}
