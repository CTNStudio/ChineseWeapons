package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.spi.ModelFactory;

/**
 * Fabric 26.1 implementation of the shared model factory. 26.1 renamed
 * ResourceLocation to Identifier; the mesh builder API is unchanged.
 */
public final class FabricModelFactory implements ModelFactory {
    public static final FabricModelFactory INSTANCE = new FabricModelFactory();

    private FabricModelFactory() {
    }

    @Override
    public ModelLayerLocation location(ModelSpec spec) {
        return new ModelLayerLocation(
                Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, spec.layerId()), "main");
    }
}
