package net.mirrorloong.chineseweapons.client.spec;

/**
 * One armour set's model plus the part names the {@code HumanoidModel} slots
 * are filled from. A {@code null} part name means that slot is empty for this
 * model, exactly like the former per-target {@code ArmorModels} switches.
 *
 * @param model     backed geometry
 * @param head      part used for helmet models
 * @param body      part used for chestplate models
 * @param leftArm   left arm part used for chestplate models
 * @param rightArm  right arm part used for chestplate models
 * @param leftLeg   leg part used for leggings models
 * @param rightLeg  right leg part used for leggings models
 * @param leftBoot  boot part used for boots models
 * @param rightBoot right boot part used for boots models
 */
public record ArmorModelSpec(ModelSpec model, String head, String body,
                             String leftArm, String rightArm,
                             String leftLeg, String rightLeg,
                             String leftBoot, String rightBoot) {
}
