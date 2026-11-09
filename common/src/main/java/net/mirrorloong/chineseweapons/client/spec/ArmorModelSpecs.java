package net.mirrorloong.chineseweapons.client.spec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Armour model geometry extracted from the former Blockbench exports.
 * Generated from the 1.20.1 target model classes; the copies in every
 * target were checked to describe the same parts, cubes, poses and layer
 * ids before those classes were removed.
 */
public final class ArmorModelSpecs {
    private ArmorModelSpecs() {
    }

    public static final ModelSpec BLACK_CHUI_ARMOR = ModelSpec.of(
            "modelblack_chui_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -9.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(35, 37, 0.0f, -18.5564f, -2.3549f, 0.0f, 9.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("bone_r1", PartPoseSpec.offsetAndRotation(0.0f, -13.5564f, 0.1451f, 0.0f, 1.5708f, 0.0f), List.of(
                            CubeSpec.of(35, 37, 0.0f, -5.0f, -2.5f, 0.0f, 9.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("bone2_r1", PartPoseSpec.offsetAndRotation(0.9f, -11.5f, 2.0f, 0.1745f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 38, -1.0f, -4.0f, -2.0f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("bone2_r2", PartPoseSpec.offsetAndRotation(0.0f, -13.4113f, 4.2016f, 0.0f, 0.1745f, -1.5708f), List.of(
                            CubeSpec.of(0, 38, 0.0f, -2.5f, -4.5f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-4.5f, -6.0f, -3.5f, 0.0f, 1.1345f, 0.0f), List.of(
                            CubeSpec.of(23, 56, -4.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(4.5f, -6.0f, -3.0f, 0.0f, -1.1345f, 0.0f), List.of(
                            CubeSpec.of(23, 56, 0.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 17, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.0f, 1.0f, 1.0f, false),
                    CubeSpec.of(32, 0, -4.5f, 1.0f, -2.0f, 9.0f, 7.0f, 4.0f, 1.1f, 1.1f, 1.1f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, 0.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of(
                    PartSpec.of("armorLeftArm_r1", PartPoseSpec.offsetAndRotation(4.6f, -1.2f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(7, 54, -1.0f, -2.0f, -2.0f, 2.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, -4.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("armorRightArm_r1", PartPoseSpec.offsetAndRotation(-4.6f, -1.2f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(7, 54, -1.0f, -2.0f, -2.0f, 2.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(48, 43, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("LeftBoots", PartPoseSpec.offset(2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of()),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(48, 43, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, false)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("RightBoots", PartPoseSpec.offset(-2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of())));

    public static final ModelSpec BOHAI_HELMET = ModelSpec.of(
            "bohai_helmet", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 1.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -5.5f, -1.9f, -5.5f, 11.0f, 5.0f, 11.0f, 0.3f, 0.3f, 0.3f, false),
                    CubeSpec.of(0, 32, -4.0f, -11.5f, -4.0f, 8.0f, 2.0f, 8.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(32, 16, -1.5f, -16.4f, -2.0f, 3.0f, 3.0f, 3.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(0, 16, -4.0f, -8.9f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(32, 22, -1.0f, -13.5f, -1.5f, 2.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of())));

    public static final ModelSpec EARLY_TANG_MING_GUANG_ARMOR = ModelSpec.of(
            "modelearly_tang_dynasty_ming_guang_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(0, 16, -4.0f, -10.6f, -4.0f, 8.0f, 2.0f, 8.0f, 0.1f, 0.1f, 0.1f, false),
                    CubeSpec.of(0, 42, -2.5f, -12.3f, -2.5f, 5.0f, 2.0f, 5.0f, 0.2f, 0.2f, 0.2f, false)
            ),
            List.of(
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, 0.0f, 4.5f, 0.2618f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(32, 16, -4.0f, 0.0f, -2.5f, 8.0f, 8.0f, 2.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(48, 9, -3.5f, 3.0f, -4.0f, 2.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, true),
                    CubeSpec.of(48, 9, 1.5f, 3.0f, -4.0f, 2.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(0, 26, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, 1.01f, 1.01f, false),
                    CubeSpec.of(24, 26, -4.0f, 1.4f, -2.0f, 8.0f, 7.0f, 4.0f, 1.5f, 1.5f, 1.5f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(24, 37, 0.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of(
                    PartSpec.of("armorle_r1", PartPoseSpec.offsetAndRotation(3.9926f, 2.4363f, 0.0f, 0.0f, 0.0f, -0.3054f), List.of(
                            CubeSpec.of(44, 48, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.6f, 0.6f, 0.6f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(24, 37, -3.8f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("armorle_r2", PartPoseSpec.offsetAndRotation(-3.9926f, 2.4363f, 0.0f, 0.0f, 0.0f, 0.3054f), List.of(
                            CubeSpec.of(44, 48, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeft", PartPoseSpec.offset(1.8f, 10.5f, 0.0f), List.of(
                    CubeSpec.of(48, 26, -1.8f, 8.0f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of(
                    PartSpec.of("LeftBoots_r1", PartPoseSpec.offsetAndRotation(2.0f, 11.5f, 2.0f, 0.0f, -1.0472f, 0.0f), List.of(
                            CubeSpec.of(48, 0, -2.0f, -2.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(40, 37, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRight", PartPoseSpec.offset(-1.8f, 10.5f, 0.0f), List.of(
                    CubeSpec.of(48, 26, -2.2f, 8.0f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of(
                    PartSpec.of("RightBoots_r1", PartPoseSpec.offsetAndRotation(-2.0f, 11.5f, 2.0f, 0.0f, 1.0472f, 0.0f), List.of(
                            CubeSpec.of(48, 0, -2.0f, -2.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, true)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(40, 37, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec FACE_GUARD = ModelSpec.of(
            "face_guard", 32, 32,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-0.5f, -4.4f, -4.4f, -0.2007f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 20, -1.0f, 1.0f, -2.0f, 3.0f, 2.0f, 3.0f, 0.3f, 0.3f, 0.3f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(0.0f, -6.8f, -4.2f, -0.1571f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 16, -4.0f, -1.0f, -1.0f, 8.0f, 2.0f, 2.0f, 0.3f, 0.3f, 0.3f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r3", PartPoseSpec.offsetAndRotation(0.0f, -4.0f, -4.4f, -0.3491f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(12, 20, -1.0f, -2.0f, -1.0f, 2.0f, 4.0f, 2.0f, 0.3f, 0.3f, 0.3f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec FINE_SCALE_ARMOR = ModelSpec.of(
            "fine_scale_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -9.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(30, 24, -2.5f, -9.0f, -4.3f, 5.0f, 6.0f, 1.0f, 0.4f, 0.4f, 0.4f, false),
                    CubeSpec.of(35, 36, 0.0f, -21.5564f, -2.3549f, 0.0f, 12.0f, 5.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(48, 23, -1.5f, -14.0f, -1.3f, 3.0f, 4.0f, 3.0f, 0.1f, 0.1f, 0.1f, false)
            ),
            List.of(
                    PartSpec.of("bone_r1", PartPoseSpec.offsetAndRotation(0.0f, -13.5564f, 0.1451f, 0.0f, 1.5708f, 0.0f), List.of(
                            CubeSpec.of(35, 36, 0.0f, -8.0f, -2.5f, 0.0f, 12.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-4.5f, -7.0f, -3.5f, 0.0f, 1.1345f, 0.0f), List.of(
                            CubeSpec.of(23, 56, -4.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(4.5f, -7.0f, -3.0f, 0.0f, -1.1345f, 0.0f), List.of(
                            CubeSpec.of(23, 56, 0.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, -4.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("armorRightArm_r1", PartPoseSpec.offsetAndRotation(-3.2f, -2.9f, 0.0f, 0.0f, 0.0f, 1.3701f), List.of(
                            CubeSpec.of(7, 54, -1.0f, -2.0f, -2.0f, 2.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, 0.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of(
                    PartSpec.of("armorLeftArm_r1", PartPoseSpec.offsetAndRotation(3.2f, -2.9f, 0.0f, 0.0f, 0.0f, -1.3701f), List.of(
                            CubeSpec.of(7, 54, -1.0f, -2.0f, -2.0f, 2.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(48, 43, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 17, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.0f, 1.0f, 1.0f, false),
                    CubeSpec.of(32, 0, -4.5f, 1.0f, -2.0f, 9.0f, 7.0f, 4.0f, 1.1f, 1.1f, 1.1f, false)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-5.573f, 9.7776f, 0.0f, 0.0f, 0.0f, 0.384f), List.of(
                            CubeSpec.of(0, 52, 1.5f, 1.0f, -2.0f, 1.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, false)
                    ),
                    List.of()),
                    PartSpec.of("LeftLegArmor_r2", PartPoseSpec.offsetAndRotation(5.573f, 9.7776f, 0.0f, 0.0f, 0.0f, -0.384f), List.of(
                            CubeSpec.of(0, 52, -2.5f, 1.0f, -2.0f, 1.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(48, 43, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, false)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r2", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("LeftBoots", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -1.9f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of(
                    PartSpec.of("LeftBoots_r1", PartPoseSpec.offsetAndRotation(0.5f, 8.8f, 0.0f, 0.0f, -1.5708f, 0.0f), List.of(
                            CubeSpec.of(46, 33, -2.5f, -2.5f, -2.0f, 5.0f, 5.0f, 4.0f, 0.7f, 0.7f, 0.7f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("RightBoots", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -2.1f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of(
                    PartSpec.of("RightBoots_r1", PartPoseSpec.offsetAndRotation(-0.5f, 8.8f, 0.0f, 0.0f, 1.5708f, 0.0f), List.of(
                            CubeSpec.of(46, 33, -2.5f, -2.5f, -2.0f, 5.0f, 5.0f, 4.0f, 0.7f, 0.7f, 0.7f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec FOOTMEN_ARMOR = ModelSpec.of(
            "modelfootmen_armor", 80, 80,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(48, 63, -4.0f, -1.8f, -4.0f, 8.0f, 3.0f, 8.0f, 0.7f, 0.7f, 0.7f, false),
                    CubeSpec.of(0, 16, -4.0f, -7.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.62f, 0.62f, 0.62f, false),
                    CubeSpec.of(0, 0, -4.1f, -9.8f, -4.1f, 8.0f, 2.0f, 8.0f, 0.25f, 0.25f, 0.25f, false),
                    CubeSpec.of(60, 34, -1.1f, -12.3564f, -1.3549f, 2.0f, 2.0f, 2.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("bone2_r1", PartPoseSpec.offsetAndRotation(0.9f, -11.9f, 1.5f, -0.2618f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(56, 0, -1.0f, -2.0f, -3.0f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("bone2_r2", PartPoseSpec.offsetAndRotation(-0.1f, -11.0288f, 2.8195f, 0.0f, 0.2618f, 1.5708f), List.of(
                            CubeSpec.of(56, 0, 0.0f, -2.5f, -4.5f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(-1.0f, -0.3f, 1.0f, 0.0175f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 33, -4.0f, -1.5f, -6.0f, 10.0f, 5.0f, 10.0f, 0.3f, 0.3f, 0.3f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(32, 16, -4.0f, 1.0f, -2.8f, 8.0f, 7.0f, 6.0f, 1.0f, 1.0f, 1.0f, false),
                    CubeSpec.of(32, 0, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.0f, 1.0f, 1.0f, false),
                    CubeSpec.of(26, 63, -2.0f, 7.5f, -4.0f, 4.0f, 4.0f, 1.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of()),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(64, 47, -3.3f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.4f, 0.4f, 0.4f, true)
            ),
            List.of(
                    PartSpec.of("dontTouch_r1", PartPoseSpec.offsetAndRotation(-2.9926f, 3.4363f, 0.0f, 0.0f, 0.0f, 0.3054f), List.of(
                            CubeSpec.of(48, 47, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
                    ),
                    List.of()),
                    PartSpec.of("dontTouch_r2", PartPoseSpec.offsetAndRotation(-2.7679f, 0.7541f, 0.0f, 0.0f, 0.0f, 0.6981f), List.of(
                            CubeSpec.of(60, 14, -2.0f, -3.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.52f, 0.52f, 0.52f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(64, 47, -0.8f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.4f, 0.4f, 0.4f, false)
            ),
            List.of(
                    PartSpec.of("armorle_r1", PartPoseSpec.offsetAndRotation(2.9926f, 3.4363f, 0.0f, 0.0f, 0.0f, -0.3054f), List.of(
                            CubeSpec.of(48, 47, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
                    ),
                    List.of()),
                    PartSpec.of("dontTouch_r3", PartPoseSpec.offsetAndRotation(2.4052f, 0.6297f, 0.0f, 0.0f, 0.0f, -0.6981f), List.of(
                            CubeSpec.of(60, 14, -2.0f, -3.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.52f, 0.52f, 0.52f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeft", PartPoseSpec.offset(2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(60, 25, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 52, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(0, 68, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRight", PartPoseSpec.offset(-2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(60, 25, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of()),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 52, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, true)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(0, 68, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec LATE_TANG_MING_GUANG_ARMOR = ModelSpec.of(
            "late_tang_dynasty_ming_guang_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 1.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.9f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(28, 17, -4.0f, -11.5f, -3.9f, 8.0f, 2.0f, 8.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(28, 58, -1.0f, -15.5f, -1.5f, 2.0f, 4.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-4.5f, -3.5f, 0.5f, 0.0f, 0.0f, -0.2269f), List.of(
                            CubeSpec.of(12, 55, -2.5f, -4.5f, -2.5f, 3.0f, 4.0f, 5.0f, 0.5f, 0.5f, 0.5f, true)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(4.5f, -3.5f, 0.5f, 0.0f, 0.0f, 0.2269f), List.of(
                            CubeSpec.of(12, 55, -0.5f, -4.5f, -2.5f, 3.0f, 4.0f, 5.0f, 0.5f, 0.5f, 0.5f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r3", PartPoseSpec.offsetAndRotation(0.0f, -14.0305f, 0.239f, 1.2479f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 48, -1.5f, -0.9825f, -1.4322f, 3.0f, 4.0f, 3.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 16, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, 1.01f, 1.01f, false),
                    CubeSpec.of(40, 33, -4.0f, 1.4f, -2.0f, 8.0f, 7.0f, 4.0f, 1.5f, 1.5f, 1.5f, false)
            ),
            List.of()),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -3.9f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("dontTouch_r1", PartPoseSpec.offsetAndRotation(-1.1f, -0.7f, 0.0f, 0.0f, 0.0f, 1.1781f), List.of(
                            CubeSpec.of(16, 42, -3.0f, -2.0f, -2.0f, 4.0f, 7.0f, 4.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -0.1f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of(
                    PartSpec.of("dontTouch_r2", PartPoseSpec.offsetAndRotation(1.1f, -0.7f, 0.0f, 0.0f, 0.0f, -1.1781f), List.of(
                            CubeSpec.of(16, 42, -1.0f, -2.0f, -2.0f, 4.0f, 7.0f, 4.0f, 0.6f, 0.6f, 0.6f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("LeftBoots", PartPoseSpec.offset(2.5f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(42, 55, -2.5f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of(
                    PartSpec.of("LeftBoots_r1", PartPoseSpec.offsetAndRotation(2.0f, 8.9f, 0.0f, -1.1519f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(53, 4, -0.5f, -5.5f, -1.5f, 1.0f, 7.0f, 4.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of()),
                    PartSpec.of("LeftBoots_r2", PartPoseSpec.offsetAndRotation(2.5f, 8.9f, 0.0f, -0.7854f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(56, 47, -0.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 32, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("dontTouch_r3", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(36, 44, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("RightBoots", PartPoseSpec.offset(-2.5f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(42, 55, -1.5f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.89f, 0.89f, 0.89f, true)
            ),
            List.of(
                    PartSpec.of("RightBoots_r1", PartPoseSpec.offsetAndRotation(-2.0f, 8.9f, 0.0f, -1.1519f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(53, 4, -0.5f, -5.5f, -1.5f, 1.0f, 7.0f, 4.0f, 0.4f, 0.4f, 0.4f, true)
                    ),
                    List.of()),
                    PartSpec.of("RightBoots_r2", PartPoseSpec.offsetAndRotation(-2.5f, 8.9f, 0.0f, -0.7854f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(56, 47, -0.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, 0.4f, 0.4f, 0.4f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 32, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, true)
            ),
            List.of(
                    PartSpec.of("dontTouch_r4", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(36, 44, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec MIDDLE_TANG_MING_GUANG_ARMOR = ModelSpec.of(
            "modelmiddle_tang_dynasty_ming_guang_armor", 80, 80,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 1.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.9f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(36, 68, -4.0f, -11.5f, -4.0f, 8.0f, 2.0f, 8.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(48, 7, -1.5f, -13.3f, -2.0f, 3.0f, 2.0f, 3.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(16, 32, -1.0f, -15.0f, -1.5f, 2.0f, 2.0f, 2.0f, 0.1f, 0.1f, 0.1f, false)
            ),
            List.of(
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, -2.1631f, 5.18f, 0.2618f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(23, 48, -6.0f, -2.0f, -3.0f, 12.0f, 4.0f, 6.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(48, 32, -4.5f, 3.5f, -4.0f, 2.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, true),
                    CubeSpec.of(48, 32, 2.4f, 3.5f, -4.0f, 2.0f, 2.0f, 1.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(32, 42, -1.5f, 8.3f, -3.8f, 3.0f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(0, 16, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, 1.01f, 1.01f, false),
                    CubeSpec.of(24, 25, -4.0f, 1.4f, -2.0f, 8.0f, 7.0f, 4.0f, 1.5f, 1.5f, 1.5f, false),
                    CubeSpec.of(16, 36, -4.0f, 1.0f, -2.0f, 8.0f, 2.0f, 4.0f, 1.6f, 1.6f, 1.6f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, 0.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of(
                    PartSpec.of("dontTouch_r1", PartPoseSpec.offsetAndRotation(1.6f, -1.7f, 0.0f, 0.0f, 0.0f, -0.48f), List.of(
                            CubeSpec.of(0, 68, -1.0f, -2.0f, -2.0f, 4.0f, 7.0f, 4.0f, 0.6f, 0.6f, 0.6f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -3.9f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of(
                    PartSpec.of("dontTouch_r2", PartPoseSpec.offsetAndRotation(-1.3f, -1.7f, 0.0f, 0.0f, 0.0f, 0.48f), List.of(
                            CubeSpec.of(0, 68, -3.0f, -2.0f, -2.0f, 4.0f, 7.0f, 4.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeft", PartPoseSpec.offset(2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 48, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of(
                    PartSpec.of("LeftBoots_r1", PartPoseSpec.offsetAndRotation(3.0f, 8.9f, 0.0f, -0.7854f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(53, 58, -0.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 32, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.6f, 3.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(40, 36, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRight", PartPoseSpec.offset(-2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 48, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of(
                    PartSpec.of("RightBoots_r1", PartPoseSpec.offsetAndRotation(-3.0f, 8.9f, 0.0f, -0.7854f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(53, 58, -0.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, 0.4f, 0.4f, 0.4f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 32, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, true)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.6f, 3.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(40, 36, -2.5f, -4.0f, -2.0f, 5.0f, 7.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec MOUNTAIN_CHARACTER_ARMOR = ModelSpec.of(
            "modelmountain_character_armor", 128, 128,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.9f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(48, 51, 0.0f, -15.5564f, -2.3549f, 0.0f, 6.0f, 5.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(50, 7, -1.0f, -14.6f, -1.0f, 2.0f, 5.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("bone_r1", PartPoseSpec.offsetAndRotation(0.0f, -12.5564f, 0.1451f, 0.0f, 1.5708f, 0.0f), List.of(
                            CubeSpec.of(48, 51, 0.0f, -3.0f, -2.5f, 0.0f, 6.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("bone2_r1", PartPoseSpec.offsetAndRotation(0.9f, -11.5f, 2.0f, 0.1745f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(32, 0, -1.0f, -4.0f, -2.0f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("bone2_r2", PartPoseSpec.offsetAndRotation(0.0f, -13.4113f, 4.2016f, 0.0f, 0.1745f, -1.5708f), List.of(
                            CubeSpec.of(32, 0, 0.0f, -2.5f, -4.5f, 0.0f, 5.0f, 9.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, 4.1f, 5.6f, 0.48f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(50, 0, -4.0f, -4.0f, -0.5f, 8.0f, 6.0f, 1.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r2", PartPoseSpec.offsetAndRotation(4.518f, 1.7495f, 1.7f, 0.0f, 1.5708f, -0.5672f), List.of(
                            CubeSpec.of(32, 45, -5.5f, -2.5f, -0.5f, 11.0f, 5.0f, 1.0f, 0.68f, 0.68f, 0.68f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r3", PartPoseSpec.offsetAndRotation(-2.6f, 0.6f, 1.7f, 0.0f, -1.5708f, 0.5672f), List.of(
                            CubeSpec.of(32, 45, -5.5f, -0.5f, 0.5f, 11.0f, 5.0f, 1.0f, 0.65f, 0.65f, 0.65f, true)
                    ),
                    List.of()),
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(-4.5f, -6.0f, -3.5f, 0.0f, 1.1345f, 0.0f), List.of(
                            CubeSpec.of(56, 34, -4.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("cube_r2", PartPoseSpec.offsetAndRotation(4.5f, -6.0f, -3.0f, 0.0f, -1.1345f, 0.0f), List.of(
                            CubeSpec.of(56, 34, 0.0f, -3.5f, 0.0f, 4.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(56, 41, -1.5f, 6.2f, -4.2f, 3.0f, 3.0f, 2.0f, 0.01f, 0.01f, 0.01f, false),
                    CubeSpec.of(24, 16, -2.5f, 9.0f, -3.0f, 5.0f, 6.0f, 7.0f, 0.8f, 0.8f, 0.8f, false),
                    CubeSpec.of(32, 51, -3.5f, 0.5f, -3.0f, 7.0f, 7.0f, 1.0f, 0.8f, 0.8f, 0.8f, false),
                    CubeSpec.of(0, 16, -4.0f, 1.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.0f, 1.0f, 1.0f, false)
            ),
            List.of(
                    PartSpec.of("armo_r1", PartPoseSpec.offsetAndRotation(2.5f, 5.8f, -3.2f, 0.0f, 0.0f, -0.3054f), List.of(
                            CubeSpec.of(56, 46, -1.5f, -1.5f, -1.0f, 2.0f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("armo_r2", PartPoseSpec.offsetAndRotation(-2.5f, 5.8f, -3.2f, 0.0f, 0.0f, 0.3054f), List.of(
                            CubeSpec.of(56, 46, -0.5f, -1.5f, -1.0f, 2.0f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(16, 45, -0.7f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.4f, 0.4f, 0.4f, false)
            ),
            List.of(
                    PartSpec.of("armorLeftArm_r1", PartPoseSpec.offsetAndRotation(2.5f, -1.8f, 0.0f, 0.0f, 0.0f, -0.6109f), List.of(
                            CubeSpec.of(56, 26, -2.0f, -2.0f, -2.0f, 3.0f, 4.0f, 4.0f, 0.52f, 0.52f, 0.52f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorle_r1", PartPoseSpec.offsetAndRotation(2.4f, 3.5f, 0.0f, 0.0f, 0.0f, -0.3491f), List.of(
                            CubeSpec.of(40, 29, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(16, 45, -3.3f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.4f, 0.4f, 0.4f, true)
            ),
            List.of(
                    PartSpec.of("armorle_r2", PartPoseSpec.offsetAndRotation(-2.0f, 3.5f, 0.0f, 0.0f, 0.0f, 0.3491f), List.of(
                            CubeSpec.of(40, 29, -2.0f, -6.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorRightArm_r1", PartPoseSpec.offsetAndRotation(-2.5f, -1.8f, 0.0f, 0.0f, 0.0f, 0.6109f), List.of(
                            CubeSpec.of(56, 26, -1.0f, -2.0f, -2.0f, 3.0f, 4.0f, 4.0f, 0.52f, 0.52f, 0.52f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(24, 29, -1.9f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.86f, 0.86f, 0.86f, false)
            ),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.1f, 3.5f, 0.0f, 0.0f, 0.0f, -0.0873f), List.of(
                            CubeSpec.of(48, 14, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeft", PartPoseSpec.offset(2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 48, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of(
                    PartSpec.of("LeftBoots_r1", PartPoseSpec.offsetAndRotation(3.0f, 8.7f, -1.5f, 0.0f, 0.0873f, 0.0f), List.of(
                            CubeSpec.of(16, 32, 0.0f, -4.0f, 0.0f, 0.0f, 7.0f, 4.0f, 0.0f, 0.0f, 0.0f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(24, 29, -2.1f, -0.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.85f, 0.85f, 0.85f, false)
            ),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.1f, 3.5f, 0.0f, 0.0f, 0.0f, 0.0873f), List.of(
                            CubeSpec.of(48, 14, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.911f, 0.911f, 0.911f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRight", PartPoseSpec.offset(-2.0f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(0, 48, -2.0f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.9f, 0.9f, 0.9f, true)
            ),
            List.of(
                    PartSpec.of("RightBoots_r1", PartPoseSpec.offsetAndRotation(-3.0f, 8.7f, -1.5f, 0.0f, -0.0873f, 0.0f), List.of(
                            CubeSpec.of(16, 32, 0.0f, -4.0f, 0.0f, 0.0f, 7.0f, 4.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec SONG_DYNASTY_INFANTRY_ARMOR = ModelSpec.of(
            "song_dynasty_infantry_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.61f, 0.61f, 0.61f, false),
                    CubeSpec.of(32, 33, -4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.7f, 0.7f, 0.7f, false),
                    CubeSpec.of(0, 49, -7.0f, -5.05f, -5.0f, 14.0f, 2.0f, 13.0f, 0.0f, 0.0f, 0.0f, false),
                    CubeSpec.of(0, 0, -1.0f, -10.7f, -1.0f, 2.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(0.5f, -8.7f, 1.2f, -0.2094f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(48, 50, -2.0f, -3.0f, -1.0f, 3.0f, 3.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, -3.55f, -7.3f, -0.3491f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(26, 18, -7.0f, -2.25f, -3.3f, 14.0f, 2.0f, 5.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 16, -4.0f, 0.3f, -2.0f, 8.0f, 12.0f, 4.0f, 0.82f, 0.82f, 0.82f, false)
            ),
            List.of()),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(24, 25, -3.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(24, 25, -0.6f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, true)
            ),
            List.of()),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.9f, 13.0f, 0.0f), List.of(),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(-0.1f, -0.5f, 0.0f, 0.0f, 0.0f, 0.1745f), List.of(
                            CubeSpec.of(0, 32, -1.8054f, -0.0608f, -2.0f, 5.0f, 8.0f, 4.0f, 0.84f, 0.84f, 0.84f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.9f, 13.0f, 0.0f), List.of(),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.1f, -0.5f, 0.0f, 0.0f, 0.0f, -0.1745f), List.of(
                            CubeSpec.of(0, 32, -3.1946f, -0.0608f, -2.0f, 5.0f, 8.0f, 4.0f, 0.85f, 0.85f, 0.85f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("RightBoots", PartPoseSpec.offset(-2.0f, 12.5f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -2.0f, 6.0f, -2.0f, 4.0f, 5.0f, 4.0f, 0.8f, 0.8f, 0.8f, true)
            ),
            List.of()),
            PartSpec.of("LeftBoots", PartPoseSpec.offset(2.0f, 12.5f, 0.0f), List.of(
                    CubeSpec.of(32, 0, -2.0f, 6.0f, -2.0f, 4.0f, 5.0f, 4.0f, 0.8f, 0.8f, 0.8f, false)
            ),
            List.of())));

    public static final ModelSpec SUANNI_HELMET = ModelSpec.of(
            "suanni_helmet", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(0, 30, -0.5f, -8.8f, -3.0f, 1.0f, 1.0f, 7.0f, 0.65f, 0.65f, 0.65f, false)
            ),
            List.of(
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, -12.5f, 0.6f, 0.1745f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(30, 18, -0.5f, -2.0f, -1.5f, 1.0f, 5.0f, 3.0f, 0.5f, 0.5f, 0.5f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r2", PartPoseSpec.offsetAndRotation(0.0f, -6.5f, -1.5f, -0.3927f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(0, 16, -4.5f, -4.0f, -3.5f, 9.0f, 8.0f, 6.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r3", PartPoseSpec.offsetAndRotation(0.0f, -9.4927f, -5.8042f, -0.3403f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(30, 26, -1.5f, 1.5f, -0.5f, 3.0f, 2.0f, 1.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r4", PartPoseSpec.offsetAndRotation(0.0f, -5.1927f, -6.9042f, -0.5323f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(16, 30, -3.5f, -4.5f, -0.5f, 7.0f, 4.0f, 2.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r5", PartPoseSpec.offsetAndRotation(0.0f, -9.4927f, -4.7042f, -0.5323f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(30, 16, -4.5f, -1.5f, -0.5f, 9.0f, 1.0f, 1.0f, 0.4f, 0.4f, 0.4f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r6", PartPoseSpec.offsetAndRotation(0.0f, -8.4927f, -4.8042f, -0.5323f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(32, 0, -0.5f, -1.5f, -0.5f, 1.0f, 2.0f, 1.0f, 0.6f, 0.6f, 0.6f, false)
                    ),
                    List.of())
            ))));

    public static final ModelSpec TANG_DYNASTY_INFANTRY_ARMOR = ModelSpec.of(
            "tang_dynasty_infantry_armor", 64, 64,
            List.of(
            PartSpec.of("bipedHead", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 0, -4.0f, -9.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.6f, 0.6f, 0.6f, false),
                    CubeSpec.of(49, 14, -1.5f, -11.6f, -2.5f, 3.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, false)
            ),
            List.of(
                    PartSpec.of("cube_r1", PartPoseSpec.offsetAndRotation(0.0f, -11.1f, -2.1f, 0.4538f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(39, 10, -3.5f, -1.5f, 0.0f, 7.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r1", PartPoseSpec.offsetAndRotation(0.0f, -12.2f, 1.6f, 0.1745f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(39, 31, -3.8f, -2.3f, -2.2f, 8.0f, 4.0f, 4.0f, 0.7f, 0.7f, 0.7f, true),
                            CubeSpec.of(39, 31, -4.2f, -2.3f, -2.2f, 8.0f, 4.0f, 4.0f, 0.7f, 0.7f, 0.7f, false)
                    ),
                    List.of()),
                    PartSpec.of("armorHead_r2", PartPoseSpec.offsetAndRotation(0.0f, -11.6f, 1.6f, 0.1745f, 0.0f, 0.0f), List.of(
                            CubeSpec.of(29, 20, -4.0f, -2.5f, -2.0f, 8.0f, 5.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedRightArm", PartPoseSpec.offset(-5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, -4.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, 0.5f, 0.5f, false)
            ),
            List.of()),
            PartSpec.of("bipedLeftArm", PartPoseSpec.offset(5.0f, 2.0f, 0.0f), List.of(
                    CubeSpec.of(19, 38, 0.4f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, 0.52f, 0.52f, 0.52f, true)
            ),
            List.of()),
            PartSpec.of("bipedLeftLeg", PartPoseSpec.offset(1.5f, 15.5f, 0.0f), List.of(),
            List.of(
                    PartSpec.of("LeftLegArmor_r1", PartPoseSpec.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.0436f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.91f, 0.91f, 0.91f, true)
                    ),
                    List.of())
            )),
            PartSpec.of("bipedBody", PartPoseSpec.offset(0.0f, 0.0f, 0.0f), List.of(
                    CubeSpec.of(0, 17, -4.0f, -0.5f, -2.0f, 8.0f, 12.0f, 4.0f, 1.0f, 1.0f, 1.0f, false)
            ),
            List.of()),
            PartSpec.of("bipedRightLeg", PartPoseSpec.offset(-1.5f, 15.5f, 0.0f), List.of(),
            List.of(
                    PartSpec.of("RightLegArmor_r1", PartPoseSpec.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0436f), List.of(
                            CubeSpec.of(0, 33, -2.5f, -4.0f, -2.0f, 5.0f, 8.0f, 4.0f, 0.9f, 0.9f, 0.9f, false)
                    ),
                    List.of())
            )),
            PartSpec.of("LeftBoots", PartPoseSpec.offset(1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -1.9f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, false)
            ),
            List.of()),
            PartSpec.of("RightBoots", PartPoseSpec.offset(-1.9f, 12.0f, 0.0f), List.of(
                    CubeSpec.of(34, 55, -2.1f, 6.5f, -2.0f, 4.0f, 5.0f, 4.0f, 0.88f, 0.88f, 0.88f, true)
            ),
            List.of())));


    private static final Map<String, ArmorModelSpec> BY_ID = byId();


    private static Map<String, ArmorModelSpec> byId() {
        Map<String, ArmorModelSpec> map = new LinkedHashMap<>();
        put(map, "bohai_helmet", BOHAI_HELMET, "bipedHead", null, null, null, null, null, null, null);
        put(map, "face_guard", FACE_GUARD, "bipedHead", null, null, null, null, null, null, null);
        put(map, "suanni_helmet", SUANNI_HELMET, "bipedHead", null, null, null, null, null, null, null);
        put(map, "black_chui_armor", BLACK_CHUI_ARMOR, "bipedHead", "bipedBody", "bipedLeftArm", "bipedRightArm",
                "bipedLeftLeg", "bipedRightLeg", "LeftBoots", "RightBoots");
        put(map, "early_tang_dynasty_ming_guang_armor", EARLY_TANG_MING_GUANG_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "bipedLeft", "bipedRight");
        put(map, "footmen_armor", FOOTMEN_ARMOR, "bipedHead", "bipedBody", "bipedLeftArm", "bipedRightArm",
                "bipedLeftLeg", "bipedRightLeg", "bipedLeft", "bipedRight");
        put(map, "late_tang_dynasty_ming_guang_armor", LATE_TANG_MING_GUANG_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "LeftBoots", "RightBoots");
        put(map, "middle_tang_dynasty_ming_guang_armor", MIDDLE_TANG_MING_GUANG_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "bipedLeft", "bipedRight");
        put(map, "mountain_character_armor", MOUNTAIN_CHARACTER_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "bipedLeft", "bipedRight");
        put(map, "fine_scale_armor", FINE_SCALE_ARMOR, "bipedHead", "bipedBody", "bipedLeftArm", "bipedRightArm",
                "bipedLeftLeg", "bipedRightLeg", "LeftBoots", "RightBoots");
        put(map, "song_dynasty_infantry_armor", SONG_DYNASTY_INFANTRY_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "LeftBoots", "RightBoots");
        put(map, "tang_dynasty_infantry_armor", TANG_DYNASTY_INFANTRY_ARMOR, "bipedHead", "bipedBody",
                "bipedLeftArm", "bipedRightArm", "bipedLeftLeg", "bipedRightLeg", "LeftBoots", "RightBoots");
        return Map.copyOf(map);
    }

    private static void put(Map<String, ArmorModelSpec> map, String id, ModelSpec model, String head,
                            String body, String leftArm, String rightArm, String leftLeg, String rightLeg,
                            String leftBoot, String rightBoot) {
        map.put(id, new ArmorModelSpec(model, head, body, leftArm, rightArm, leftLeg, rightLeg,
                leftBoot, rightBoot));
    }

    public static ArmorModelSpec get(String modelId) {
        ArmorModelSpec spec = BY_ID.get(modelId);
        if (spec == null) {
            throw new IllegalArgumentException("Unknown armour model: " + modelId);
        }
        return spec;
    }

    /** Registered armour layers, excluding the two entity models. */
    public static Iterable<ArmorModelSpec> all() {
        return BY_ID.values();
    }
}
