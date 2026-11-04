package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public class ChineseweaponsModBlockEntities {
    private static final RegistryBuilder<BlockEntityType<?>> REGISTRY =
            ChineseWeapons.registration().builder("minecraft:block_entity_type");

    public static final RegistryEntry<BlockEntityType<?>> ARMOR_CASTING_TABLE =
            REGISTRY.register("armor_casting_table",
                    () -> ChineseWeapons.platform().content().armorCastingTableBlockEntity());

    public static void init() {
    }
}
