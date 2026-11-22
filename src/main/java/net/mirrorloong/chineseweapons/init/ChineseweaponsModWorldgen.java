package net.mirrorloong.chineseweapons.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public final class ChineseweaponsModWorldgen {

    private ChineseweaponsModWorldgen() {
    }

    public static void init() {
        addFeature("ancient_gem_ore");
        addFeature("deepslate_ancient_gem_ore");
    }

    private static void addFeature(String path) {
        ResourceKey<PlacedFeature> feature = ResourceKey.create(
                Registries.PLACED_FEATURE, new ResourceLocation(ChineseweaponsMod.MODID, path));
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_ORES, feature);
    }
}
