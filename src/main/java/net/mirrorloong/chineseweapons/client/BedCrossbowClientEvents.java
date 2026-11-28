package net.mirrorloong.chineseweapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.mirrorloong.chineseweapons.item.BedCrossbowBoltItem;
import net.mirrorloong.chineseweapons.network.BedCrossbowReleasePacket;
import net.mirrorloong.chineseweapons.network.BedCrossbowWindPacket;


@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class BedCrossbowClientEvents {

    private static final float HAND_UP_RADIANS = (float) Math.toRadians(26.0F);
    private static final float HAND_DOWN_RADIANS = (float) Math.toRadians(18.0F);
    private static final float ARM_BASE_X_ROT = -1.08F;
    private static final float ARM_BASE_Y_ROT = 0.38F;
    private static final float ARM_BASE_Z_ROT = -0.10F;
    private static final int WIND_HOLD_TICKS = 5;
    private static final int ENTRY_GLIDE_TICKS = 20;
    private static boolean jumpWasDown;
    private static boolean firstPersonArmsRendered;
    private static int useHeldTicks;
    private static double orbitRadius;
    private static float lastViewYaw;
    private static float lastViewPitch;
    private static boolean viewSensitivityInit;
    private static boolean entryGlideStarted;
    private static int entryGlideTicks;
    private static double glideStartX;
    private static double glideStartY;
    private static double glideStartZ;
    private static double glideEndX;
    private static double glideEndY;
    private static double glideEndZ;
    private static int crossbowShakeTicks;
    private static float crossbowShakeSeed;

    private static final HumanoidModel.ArmPose OPERATING_POSE = HumanoidModel.ArmPose.create(
            "bed_crossbow_operate", false,
            (model, entity, arm) -> applyOperatingArmPose(model, entity, arm));

    private BedCrossbowClientEvents() {
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            return;
        }
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        model.rightArmPose = OPERATING_POSE;
        model.leftArmPose = OPERATING_POSE;
        model.crouching = true;
    }

    private static void applyOperatingArmPose(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (!(entity instanceof Player player)) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            return;
        }

        float partial = Minecraft.getInstance().getFrameTime();
        float stroke = handStroke(crossbow, partial);
        float shake = handShake(crossbow, partial);

        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = ARM_BASE_X_ROT + stroke + shake;
            model.rightArm.yRot = ARM_BASE_Y_ROT;
            model.rightArm.zRot = ARM_BASE_Z_ROT;
        } else {
            model.leftArm.xRot = ARM_BASE_X_ROT + stroke - shake;
            model.leftArm.yRot = -ARM_BASE_Y_ROT;
            model.leftArm.zRot = -ARM_BASE_Z_ROT;
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isInvisible()) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            return;
        }

        event.setCanceled(true);
        if (firstPersonArmsRendered) {
            return;
        }
        firstPersonArmsRendered = true;
        renderFirstPersonArms(event, player);
    }

    private static void renderFirstPersonArms(RenderHandEvent event, LocalPlayer player) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (!(renderer instanceof PlayerRenderer playerRenderer)) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        PartPose rightPose = model.rightArm.storePose();
        PartPose leftPose = model.leftArm.storePose();
        PartPose rightSleevePose = model.rightSleeve.storePose();
        PartPose leftSleevePose = model.leftSleeve.storePose();        boolean rightVisible = model.rightArm.visible;
        boolean leftVisible = model.leftArm.visible;
        boolean rightSleeveVisible = model.rightSleeve.visible;
        boolean leftSleeveVisible = model.leftSleeve.visible;
        float attackTime = model.attackTime;
        boolean crouching = model.crouching;
        float swimAmount = model.swimAmount;
        HumanoidModel.ArmPose rightArmPose = model.rightArmPose;
        HumanoidModel.ArmPose leftArmPose = model.leftArmPose;

        try {
            model.attackTime = 0.0F;
            model.crouching = false;
            model.swimAmount = 0.0F;
            model.rightArmPose = OPERATING_POSE;
            model.leftArmPose = OPERATING_POSE;
            model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0D, 0.36D, -0.42D);
            poseStack.mulPose(Axis.XP.rotationDegrees(4.0F));

            drawArm(poseStack, event.getMultiBufferSource(), event.getPackedLight(), player,
                    model.rightArm, model.rightSleeve);
            drawArm(poseStack, event.getMultiBufferSource(), event.getPackedLight(), player,
                    model.leftArm, model.leftSleeve);

            poseStack.popPose();
        } finally {
            model.rightArm.loadPose(rightPose);
            model.leftArm.loadPose(leftPose);
            model.rightSleeve.loadPose(rightSleevePose);
            model.leftSleeve.loadPose(leftSleevePose);
            model.rightArm.visible = rightVisible;
            model.leftArm.visible = leftVisible;
            model.rightSleeve.visible = rightSleeveVisible;
            model.leftSleeve.visible = leftSleeveVisible;
            model.attackTime = attackTime;
            model.crouching = crouching;
            model.swimAmount = swimAmount;
            model.rightArmPose = rightArmPose;
            model.leftArmPose = leftArmPose;
        }
    }

    private static void drawArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                LocalPlayer player, ModelPart arm, ModelPart sleeve) {
        arm.visible = true;
        sleeve.visible = true;
        arm.render(poseStack,
                buffer.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())),
                packedLight, OverlayTexture.NO_OVERLAY);
        sleeve.render(poseStack,
                buffer.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())),
                packedLight, OverlayTexture.NO_OVERLAY);
    }


    @SubscribeEvent
    public static void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        BedCrossbowEntity operating = BedCrossbowEntity.findOperating(player);
        if (operating == null) {
            return;
        }

        boolean spyglassAllowed = !operating.hasLoadedBolt()
                || operating.getCharge() >= 0.999F;
        boolean spyglass = spyglassAllowed
                && (player.getMainHandItem().getItem() instanceof SpyglassItem
                || player.getOffhandItem().getItem() instanceof SpyglassItem);
        boolean loading = !operating.hasLoadedBolt()
                && player.getMainHandItem().getItem() instanceof BedCrossbowBoltItem;
        if (!spyglass && !loading) {
            event.setCanceled(true);
        }
    }

    private static void applyEntryGlide(LocalPlayer player, BedCrossbowEntity crossbow) {
        if (!entryGlideStarted) {
            entryGlideStarted = true;
            entryGlideTicks = 0;
            Vec3 target = crossbow.position().subtract(crossbow.getForward().scale(1.85D));
            glideStartX = player.getX();
            glideStartY = player.getY();
            glideStartZ = player.getZ();
            glideEndX = target.x;
            glideEndY = crossbow.getY();
            glideEndZ = target.z;
        }
        if (entryGlideTicks >= ENTRY_GLIDE_TICKS) {
            return;
        }
        entryGlideTicks++;
        double t = entryGlideTicks / (double) ENTRY_GLIDE_TICKS;
        double eased = t * t * (3.0D - 2.0D * t);
        player.setPos(
                glideStartX + (glideEndX - glideStartX) * eased,
                glideStartY + (glideEndY - glideStartY) * eased,
                glideStartZ + (glideEndZ - glideStartZ) * eased);
        player.setDeltaMovement(0.0D, player.getDeltaMovement().y, 0.0D);
        if (entryGlideTicks >= ENTRY_GLIDE_TICKS) {
            orbitRadius = 0.0D;
        }
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            return;
        }

        Input input = event.getInput();
        input.forwardImpulse = 0.0F;
        input.leftImpulse = 0.0F;

        if (entryGlideTicks < ENTRY_GLIDE_TICKS) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        boolean left = minecraft.options.keyLeft.isDown();
        boolean right = minecraft.options.keyRight.isDown();
        if (left == right) {
            return;
        }

        Vec3 center = crossbow.getWinchAxleCenter();
        double dx = player.getX() - center.x;
        double dz = player.getZ() - center.z;
        double radius = Math.sqrt(dx * dx + dz * dz);
        if (radius < 0.8D || radius > 4.0D) {
            return;
        }
        if (orbitRadius <= 0.0D) {
            orbitRadius = radius;
        }

        double angle = Math.atan2(dx, dz);
        double dir = right ? 1.0D : -1.0D;
        double tx = Math.cos(angle) * dir;
        double tz = -Math.sin(angle) * dir;

        double ux = dx / radius;
        double uz = dz / radius;
        double error = (orbitRadius - radius) * 0.35D;
        tx += ux * error;
        tz += uz * error;
        double length = Math.sqrt(tx * tx + tz * tz);
        if (length < 1.0E-4D) {
            return;
        }
        tx /= length;
        tz /= length;

        float yaw = player.getYRot();
        double fx = -Math.sin(Math.toRadians(yaw));
        double fz = Math.cos(Math.toRadians(yaw));
        double lx = Math.cos(Math.toRadians(yaw));
        double lz = Math.sin(Math.toRadians(yaw));
        input.forwardImpulse = (float) (tx * fx + tz * fz);
        input.leftImpulse = (float) (tx * lx + tz * lz);
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (crossbowShakeTicks <= 0) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        float remain = crossbowShakeTicks / 12.0F;
        float strength = remain * remain;
        float phase = (player.tickCount + (float) event.getPartialTick()) * 2.0F + crossbowShakeSeed;
        event.setYaw(event.getYaw() + Mth.sin(phase) * 0.45F * strength);
        event.setPitch(event.getPitch() + Mth.cos(phase * 1.3F) * 0.30F * strength);
        event.setRoll(event.getRoll() + Mth.sin(phase * 1.7F) * 0.18F * strength);
    }

    public static void triggerCrossbowShake() {
        crossbowShakeTicks = 12;
        crossbowShakeSeed = (float) (Math.random() * Math.PI * 2.0D);
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            firstPersonArmsRendered = false;
            smoothOperatingView();
        }
    }

    private static void smoothOperatingView() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            viewSensitivityInit = false;
            return;
        }
        if (!viewSensitivityInit) {
            lastViewYaw = player.getYRot();
            lastViewPitch = player.getXRot();
            viewSensitivityInit = true;
            return;
        }

        float yawDelta = Mth.wrapDegrees(player.getYRot() - lastViewYaw);
        player.setYRot(lastViewYaw + yawDelta * 0.35F);
        float pitchDelta = player.getXRot() - lastViewPitch;
        player.setXRot(lastViewPitch + pitchDelta * 0.25F);

        clampViewYaw(player, crossbow);
        lastViewYaw = player.getYRot();
        lastViewPitch = player.getXRot();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        if (crossbowShakeTicks > 0) {
            crossbowShakeTicks--;
        }

        BedCrossbowEntity operating = BedCrossbowEntity.findOperating(player);
        if (operating == null) {
            jumpWasDown = false;
            useHeldTicks = 0;
            orbitRadius = 0.0D;
            viewSensitivityInit = false;
            entryGlideStarted = false;
            entryGlideTicks = 0;
            return;
        }

        applyEntryGlide(player, operating);

        boolean jumpDown = minecraft.options.keyJump.isDown();
        if (jumpDown && !jumpWasDown) {
            ChineseweaponsMod.CHANNEL.sendToServer(new BedCrossbowReleasePacket());
        }
        jumpWasDown = jumpDown;

        boolean useDown = minecraft.options.keyUse.isDown();
        if (useDown) {
            useHeldTicks++;
        } else {
            useHeldTicks = 0;
        }
        if (useDown && useHeldTicks >= WIND_HOLD_TICKS) {
            ChineseweaponsMod.CHANNEL.sendToServer(new BedCrossbowWindPacket(true));
        } else {
            ChineseweaponsMod.CHANNEL.sendToServer(new BedCrossbowWindPacket(false));
        }
    }

    private static void clampViewYaw(LocalPlayer player, BedCrossbowEntity crossbow) {
        Vec3 center = crossbow.getWinchAxleCenter();
        double dx = player.getX() - center.x;
        double dz = player.getZ() - center.z;
        float radialYaw = dx * dx + dz * dz < 1.0E-4D
                ? crossbow.getYRot()
                : (float) (Math.atan2(-dx, dz) * (180.0D / Math.PI)) + 180.0F;
        float diff = Mth.wrapDegrees(player.getYRot() - radialYaw);
        if (diff > 30.0F) {
            player.setYRot(radialYaw + 30.0F);
        } else if (diff < -30.0F) {
            player.setYRot(radialYaw - 30.0F);
        }

        player.setXRot(Mth.clamp(player.getXRot(), -30.0F, 20.0F));
    }

    private static float handStroke(BedCrossbowEntity crossbow, float partialTick) {
        if (crossbow.getCharge() >= 1.0F) {
            return 0.0F;
        }

        float phase = crossbow.getCharge() * (float) (Math.PI * 8.0D);
        float sine = Mth.sin(phase);
        float amplitude = sine >= 0.0F ? HAND_UP_RADIANS : HAND_DOWN_RADIANS;
        return -sine * amplitude;
    }

    private static float handShake(BedCrossbowEntity crossbow, float partialTick) {
        byte shake = crossbow.getShake();
        if (shake <= 0) {
            return 0.0F;
        }
        float amplitude = shake == 1 ? 0.04F : 0.09F;
        float speed = shake == 1 ? 0.9F : 1.2F;
        return Mth.sin((crossbow.tickCount + partialTick) * speed) * amplitude;
    }
}
