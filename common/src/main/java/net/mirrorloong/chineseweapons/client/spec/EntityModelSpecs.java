package net.mirrorloong.chineseweapons.client.spec;

import java.util.List;

/**
 * Entity model geometry for the javelin projectile and the standing shield.
 */
public final class EntityModelSpecs {
    private EntityModelSpecs() {
    }

    public static final ModelSpec JAVELIN = ModelSpec.of(
            "song_javelin", 64, 64,
            List.of(
            PartSpec.of("group", PartPoseSpec.offsetAndRotation(0.0f, 7.4041f, -0.0029f, 0.0f, -1.5708f, 0.0f), List.of(
                    CubeSpec.of(4, 0, -0.5f, 17.0959f, -0.4971f, 1.0f, 17.0f, 1.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(0, 0, -0.5f, -18.9041f, -0.4971f, 1.0f, 36.0f, 1.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(8, 9, 0.0f, -16.2041f, -1.9971f, 0.0f, 9.0f, 4.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(8, 0, -1.5f, -7.4041f, -1.4971f, 3.0f, 6.0f, 3.0f, 0.02f, 0.02f, 0.02f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-0.5f, -1.291f, -0.4971f, 0.7854f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(16, 9, -0.5f, -6.3131f, 4.0f, 2.0f, 3.0f, 3.0f, -0.35f, -0.35f, -0.35f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec STANDING_SHIELD = ModelSpec.of(
            "song_standing_shield", 64, 64,
            List.of(
            PartSpec.of("root", PartPoseSpec.offset(-0.0606f, 5.1158f, -5.8758f), List.of(
                    CubeSpec.of(0, 0, -6.4494f, -21.1258f, -2.1342f, 13.02f, 39.02f, 2.02f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(31, 11, -6.4494f, -10.1058f, -2.1342f, 13.02f, 0.0f, 2.02f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(0.8899f, 16.4904f, 6.2758f, 0.0f, 0.3927f, -1.5708f), List.of(
                            CubeSpec.of(31, 38, 5.0f, -2.0f, -1.0f, 7.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false),
                            CubeSpec.of(31, 38, 12.0f, -2.0f, -1.0f, 7.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false),
                            CubeSpec.of(31, 38, -2.0f, -2.0f, -1.0f, 7.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(-3.5687f, -15.5095f, -1.1242f, 0.0f, 0.0f, 0.7854f), List.of(
                            CubeSpec.of(31, 0, -1.0f, -6.0f, -1.0f, 9.0f, 9.0f, 2.0f, -0.01f, -0.01f, -0.01f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r3", PartPoseSpec.offsetAndRotation(1.7313f, -10.3096f, -1.1242f, 0.0f, 0.0f, -0.3927f), List.of(
                            CubeSpec.of(31, 13, 0.0f, -10.0f, -1.0f, 4.0f, 12.0f, 2.0f, -0.02f, -0.02f, -0.02f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r4", PartPoseSpec.offsetAndRotation(-1.6101f, -10.3096f, -1.1242f, 0.0f, 0.0f, 0.3927f), List.of(
                            CubeSpec.of(31, 13, -4.0f, -10.0f, -1.0f, 4.0f, 12.0f, 2.0f, -0.02f, -0.02f, -0.02f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r5", PartPoseSpec.offsetAndRotation(2.2313f, 16.4904f, -1.1242f, 0.0f, 0.0f, 0.7854f), List.of(
                            CubeSpec.of(31, 38, -5.0f, -2.0f, -1.0f, 7.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of()),
                    PartSpec.of("cube_r6", PartPoseSpec.offsetAndRotation(-2.1101f, 16.4904f, -1.1242f, 0.0f, 0.0f, -0.7854f), List.of(
                            CubeSpec.of(31, 38, -2.0f, -2.0f, -1.0f, 7.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            ))));

    public static List<ModelSpec> all() {
        return List.of(JAVELIN, STANDING_SHIELD);
    }
}
