package net.mirrorloong.chineseweapons.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.ContentIds;

/** Applies the current registry id to 26.1 item properties. */
public final class ItemIds {
    private ItemIds() {
    }

    public static Item.Properties apply(Item.Properties properties) {
        String id = ContentIds.current();
        if (id != null) {
            properties.setId(ResourceKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, id)));
        }
        return properties;
    }
}
