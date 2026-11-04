package net.mirrorloong.chineseweapons.item;

import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;

/**
 * Dyeable variant of {@link FabricArmorItem}. Fabric's dye recipe only accepts
 * items that implement {@link DyeableItem}, so the definition's dyeable flag
 * selects between this class and the plain one.
 */
public class FabricDyeableArmorItem extends FabricArmorItem implements DyeableItem {
    public FabricDyeableArmorItem(ArmorDefinition definition, ArmorSlot slot) {
        super(definition, slot);
    }
}
