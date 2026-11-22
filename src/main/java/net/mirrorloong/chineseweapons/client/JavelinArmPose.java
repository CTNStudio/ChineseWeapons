package net.mirrorloong.chineseweapons.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.mirrorloong.chineseweapons.item.SongJavelinItem;

public final class JavelinArmPose {

    private JavelinArmPose() {
    }

        public static void apply(HumanoidModel<?> model, LivingEntity entity) {
        if (!(entity.getUseItem().getItem() instanceof SongJavelinItem)
                || entity.getUseItemRemainingTicks() <= 0) {
            return;
        }
                HumanoidArm arm = entity.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? entity.getMainArm()
                : entity.getMainArm().getOpposite();
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = (float) Math.toRadians(-180);
            model.rightArm.yRot = (float) Math.toRadians(180);
            model.rightArm.zRot = (float) Math.toRadians(-20);
        } else {
            model.leftArm.xRot = (float) Math.toRadians(-180);
            model.leftArm.yRot = (float) Math.toRadians(180);
            model.leftArm.zRot = (float) Math.toRadians(20);
        }
    }
}
