package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChineseweaponsModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChineseweaponsMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArmorCastingTableBlockEntity>>
            ARMOR_CASTING_TABLE =
            REGISTRY.register("armor_casting_table",
                    () -> BlockEntityType.Builder.of(
                            ArmorCastingTableBlockEntity::new,
                            ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()
                    ).build(null));
}