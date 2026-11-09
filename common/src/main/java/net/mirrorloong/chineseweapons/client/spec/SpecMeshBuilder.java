package net.mirrorloong.chineseweapons.client.spec;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Turns a {@link ModelSpec} into the vanilla mesh builders. The builder API is
 * identical on every supported Minecraft version, so this is the shared half
 * of the {@code ModelFactory} SPI; targets only supply the version-specific
 * {@code ModelLayerLocation} (ResourceLocation vs Identifier).
 */
public final class SpecMeshBuilder {
    private SpecMeshBuilder() {
    }

    public static LayerDefinition layer(ModelSpec spec) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (PartSpec part : spec.parts()) {
            add(root, part);
        }
        return LayerDefinition.create(mesh, spec.textureWidth(), spec.textureHeight());
    }

    private static void add(PartDefinition parent, PartSpec spec) {
        CubeListBuilder cubes = CubeListBuilder.create();
        boolean mirror = false;
        for (CubeSpec cube : spec.cubes()) {
            if (cube.mirror() != mirror) {
                cubes.mirror(cube.mirror());
                mirror = cube.mirror();
            }
            cubes.texOffs(cube.u(), cube.v());
            cubes.addBox(cube.x(), cube.y(), cube.z(), cube.width(), cube.height(), cube.depth(),
                    new CubeDeformation(cube.growX(), cube.growY(), cube.growZ()));
        }
        PartDefinition part = parent.addOrReplaceChild(spec.name(), cubes, pose(spec.pose()));
        for (PartSpec child : spec.children()) {
            add(part, child);
        }
    }

    private static PartPose pose(PartPoseSpec pose) {
        if (pose.rotated()) {
            return PartPose.offsetAndRotation(pose.x(), pose.y(), pose.z(), pose.xRot(), pose.yRot(), pose.zRot());
        }
        return PartPose.offset(pose.x(), pose.y(), pose.z());
    }
}
