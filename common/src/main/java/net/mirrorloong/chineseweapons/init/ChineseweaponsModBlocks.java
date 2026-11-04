package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.level.block.Block;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public class ChineseweaponsModBlocks {
    private static final RegistryBuilder<Block> REGISTRY = ChineseWeapons.registration().builder("minecraft:block");

    public static final RegistryEntry<Block> ARMOR_CASTING_TABLE =
            REGISTRY.register("armor_casting_table", () -> ChineseWeapons.platform().content().armorCastingTable());
    public static final RegistryEntry<Block> ANCIENT_GEM_ORE =
            REGISTRY.register("ancient_gem_ore", () -> ChineseWeapons.platform().content().ancientGemOre());
    public static final RegistryEntry<Block> DeepslateANCIENT_GEM_ORE =
            REGISTRY.register("deepslate_ancient_gem_ore", () -> ChineseWeapons.platform().content().deepslateAncientGemOre());
    public static final RegistryEntry<Block> ANCIENT_GEM_BLOCK =
            REGISTRY.register("ancient_gem_block", () -> ChineseWeapons.platform().content().ancientGemBlock());

    public static void init() {
    }
}
