package net.mirrorloong.chineseweapons.spi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.client.spec.SpecMeshBuilder;

/**
 * Client-only SPI: builds baked model parts from the shared
 * {@link ModelSpec} tables. Each target implements the one genuinely
 * version-specific piece, {@link #location(ModelSpec)}; mesh building and
 * layer baking are shared.
 */
public interface ModelFactory {
    /** Registry location of the layer, e.g. {@code chineseweapons:bohai_helmet#main}. */
    ModelLayerLocation location(ModelSpec spec);

    /** Bake from the registered layer, so loader-side layer handling still applies. */
    default ModelPart bake(ModelSpec spec) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(location(spec));
    }

    /** Geometry for loader layer registration. */
    default LayerDefinition layer(ModelSpec spec) {
        return SpecMeshBuilder.layer(spec);
    }
}
