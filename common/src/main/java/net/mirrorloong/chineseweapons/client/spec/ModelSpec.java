package net.mirrorloong.chineseweapons.client.spec;

import java.util.List;

/**
 * Version-neutral description of one baked model layer: a texture sheet size
 * and an ordered list of root parts. Minecraft's {@code LayerDefinition}
 * builder API is identical from 1.18.2 through 26.1, so the geometry can live
 * in the shared tree instead of in one Blockbench export per target.
 *
 * @param layerId       registry path of the {@code ModelLayerLocation}
 * @param textureWidth  texture sheet width in pixels
 * @param textureHeight texture sheet height in pixels
 * @param parts         root parts, in the order their cubes should render
 */
public record ModelSpec(String layerId, int textureWidth, int textureHeight, List<PartSpec> parts) {
    public ModelSpec {
        parts = List.copyOf(parts);
    }

    public static ModelSpec of(String layerId, int textureWidth, int textureHeight, List<PartSpec> parts) {
        return new ModelSpec(layerId, textureWidth, textureHeight, parts);
    }
}
