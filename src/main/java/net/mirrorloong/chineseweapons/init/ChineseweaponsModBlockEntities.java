package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;

public class ChineseweaponsModBlockEntities {
    //old api DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID) -> new api DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID)
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ChineseweaponsMod.MODID);

    public static final RegistryObject<BlockEntityType<ArmorCastingTableBlockEntity>> ARMOR_CASTING_TABLE =
            REGISTRY.register("armor_casting_table",
                    () -> BlockEntityType.Builder.of(
                            ArmorCastingTableBlockEntity::new,
                            ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()
                    ).build(null));

            public static void bootstrap() {
    }
}
