package net.mirrorloong.chineseweapons.item;

import net.minecraft.world.item.Item;

public class TangDynastyHorseItem extends Item {
    public TangDynastyHorseItem(int defense) {
        super(HorseArmorMaterials.apply(ItemIds.apply(new Item.Properties()).stacksTo(1), defense));
    }
}
