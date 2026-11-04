package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.spi.ModelFactory;

/**
 * Forge 1.18.2-1.20.1 implementation of the shared model factory. Only the
 * layer location differs between API families; the mesh itself comes from
 * {@code SpecMeshBuilder} in common.
 */
public final class ForgeModelFactory implements ModelFactory {
    public static final ForgeModelFactory INSTANCE = new ForgeModelFactory();

    private ForgeModelFactory() {
    }

    @Override
    public ModelLayerLocation location(ModelSpec spec) {
        return new ModelLayerLocation(new ResourceLocation(ChineseWeapons.MOD_ID, spec.layerId()), "main");
    }
}
