package net.mirrorloong.chineseweapons.client.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public final class ArmPoseEvents {

    public interface Handler {
        void apply(HumanoidModel<?> model, LivingEntity entity);
    }

    public interface PreHandler {
        void apply(HumanoidModel<?> model, LivingEntity entity);
    }

    private static final List<Handler> HANDLERS = new ArrayList<>();
    private static final List<PreHandler> PRE_HANDLERS = new ArrayList<>();

    private ArmPoseEvents() {
    }

    public static void register(Handler handler) {
        HANDLERS.add(handler);
    }

    public static void registerPre(PreHandler handler) {
        PRE_HANDLERS.add(handler);
    }

    public static void invokePre(HumanoidModel<?> model, LivingEntity entity) {
        for (int i = 0; i < PRE_HANDLERS.size(); i++) {
            PRE_HANDLERS.get(i).apply(model, entity);
        }
    }

    public static void invokeAll(HumanoidModel<?> model, LivingEntity entity) {
        for (int i = 0; i < HANDLERS.size(); i++) {
            HANDLERS.get(i).apply(model, entity);
        }
    }
}
