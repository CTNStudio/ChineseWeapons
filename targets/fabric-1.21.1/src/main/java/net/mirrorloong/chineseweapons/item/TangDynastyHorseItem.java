package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TangDynastyHorseItem extends AnimalArmorItem {
    private final ResourceLocation texture;

    public TangDynastyHorseItem(int defense, ResourceLocation texture) {
        super(HorseArmorMaterials.get(defense), BodyType.EQUESTRIAN, false,
                new Item.Properties().stacksTo(1));
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    public int getMaxDamage(ItemStack stack) {
        return 0;
    }
}
