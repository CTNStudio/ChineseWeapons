package net.mirrorloong.chineseweapons.client.spec;

import java.util.List;

/**
 * One named part of a {@link ModelSpec}, mirroring a Blockbench part bone:
 * a pose relative to its parent, its own cubes, and its nested parts.
 */
public record PartSpec(String name, PartPoseSpec pose, List<CubeSpec> cubes, List<PartSpec> children) {
    public PartSpec {
        cubes = List.copyOf(cubes);
        children = List.copyOf(children);
    }

    public static PartSpec of(String name, PartPoseSpec pose, List<CubeSpec> cubes, List<PartSpec> children) {
        return new PartSpec(name, pose, cubes, children);
    }
}
