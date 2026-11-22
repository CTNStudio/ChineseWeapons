package net.mirrorloong.chineseweapons.client.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public final class ArmPoseEvents {

    public interface Handler {
        void apply(HumanoidModel<?> model, LivingEntity entity);
    }

    private static final List<Handler> HANDLERS = new ArrayList<>();

    private ArmPoseEvents() {
    }

    public static void register(Handler handler) {
        HANDLERS.add(handler);
    }

    public static void invokeAll(HumanoidModel<?> model, LivingEntity entity) {
        for (int i = 0; i < HANDLERS.size(); i++) {
            HANDLERS.get(i).apply(model, entity);
        }
    }
}
