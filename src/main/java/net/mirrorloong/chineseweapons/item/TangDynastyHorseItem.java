package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;

public class TangDynastyHorseItem extends HorseArmorItem {
        public TangDynastyHorseItem(int defense, ResourceLocation texture) {
        super(defense, texture.toString(), new Item.Properties()
                .stacksTo(1)
                .durability(0));
        }

        @Override
    public boolean canBeDepleted() {
        return false;
    }
}
