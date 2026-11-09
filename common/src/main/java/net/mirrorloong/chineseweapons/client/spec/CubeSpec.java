package net.mirrorloong.chineseweapons.client.spec;

/**
 * A single textured box. {@code u}/{@code v} are the texture offsets,
 * {@code width}/{@code height}/{@code depth} the box size and the three
 * {@code grow} values the {@code CubeDeformation} applied to each axis.
 */
public record CubeSpec(int u, int v, float x, float y, float z,
                       float width, float height, float depth,
                       float growX, float growY, float growZ, boolean mirror) {
    public static CubeSpec of(int u, int v, float x, float y, float z,
                              float width, float height, float depth,
                              float growX, float growY, float growZ, boolean mirror) {
        return new CubeSpec(u, v, x, y, z, width, height, depth, growX, growY, growZ, mirror);
    }
}
