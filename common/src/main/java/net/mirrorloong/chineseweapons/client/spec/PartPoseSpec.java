package net.mirrorloong.chineseweapons.client.spec;

/**
 * A part's translation and Euler rotation in radians, the values a Blockbench
 * export passed to {@code PartPose.offset} / {@code PartPose.offsetAndRotation}.
 */
public record PartPoseSpec(float x, float y, float z, float xRot, float yRot, float zRot) {
    public static PartPoseSpec offset(float x, float y, float z) {
        return new PartPoseSpec(x, y, z, 0.0F, 0.0F, 0.0F);
    }

    public static PartPoseSpec offsetAndRotation(float x, float y, float z,
                                                 float xRot, float yRot, float zRot) {
        return new PartPoseSpec(x, y, z, xRot, yRot, zRot);
    }

    public boolean rotated() {
        return xRot != 0.0F || yRot != 0.0F || zRot != 0.0F;
    }
}
