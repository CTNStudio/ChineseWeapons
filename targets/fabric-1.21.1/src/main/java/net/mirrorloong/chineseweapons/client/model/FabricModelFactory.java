package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.spi.ModelFactory;

/**
 * Fabric 1.21.1 implementation of the shared model factory.
 */
public final class FabricModelFactory implements ModelFactory {
    public static final FabricModelFactory INSTANCE = new FabricModelFactory();

    private FabricModelFactory() {
    }

    @Override
    public ModelLayerLocation location(ModelSpec spec) {
        return new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, spec.layerId()), "main");
    }
}
