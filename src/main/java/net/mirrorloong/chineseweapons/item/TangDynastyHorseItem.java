package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

/**
 * Tang Dynasty horse armor. Extends {@link AnimalArmorItem} so {@link net.minecraft.world.entity.animal.horse.Horse#isBodyArmorItem}
 * accepts it and it renders on the horse's armor slot. The custom texture is returned via {@link #getTexture()}.
 */
public class TangDynastyHorseItem extends AnimalArmorItem {
    private final ResourceLocation texture;

    public TangDynastyHorseItem(int defense, ResourceLocation texture, Holder<ArmorMaterial> material) {
        super(material, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().stacksTo(1));
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture() {
        return this.texture;
    }
}
