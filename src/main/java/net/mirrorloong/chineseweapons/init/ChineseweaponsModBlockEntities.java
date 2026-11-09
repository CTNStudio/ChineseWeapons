package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;

public class ChineseweaponsModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChineseweaponsMod.MODID);

    public static final RegistryObject<BlockEntityType<ArmorCastingTableBlockEntity>> ARMOR_CASTING_TABLE =
            REGISTRY.register("armor_casting_table",
                    () -> BlockEntityType.Builder.of(
                            ArmorCastingTableBlockEntity::new,
                            ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()
                    ).build(null));
}