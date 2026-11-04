package net.mirrorloong.chineseweapons.item;

import java.util.Map;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * 26.1 removed {@code HorseArmorItem}: horse armour is a regular item whose
 * properties reference an {@link ArmorMaterial} for the BODY slot and an
 * equipment asset for the texture.
 */
public class TangDynastyHorseItem extends Item {
    public TangDynastyHorseItem(int defense, Identifier texture) {
        super(new Item.Properties().stacksTo(1).horseArmor(createMaterial(defense, texture)));
    }

    private static ArmorMaterial createMaterial(int defense, Identifier texture) {
        String path = texture.getPath();
        String name = path.substring(path.lastIndexOf('/') + 1);
        if (name.endsWith(".png")) {
            name = name.substring(0, name.length() - 4);
        }
        return new ArmorMaterial(
                0,
                Map.of(ArmorType.BODY, defense),
                0,
                SoundEvents.HORSE_ARMOR,
                0.0F,
                0.0F,
                ItemTags.REPAIRS_LEATHER_ARMOR,
                EquipmentAssets.createId("chineseweapons:" + name));
    }
}
