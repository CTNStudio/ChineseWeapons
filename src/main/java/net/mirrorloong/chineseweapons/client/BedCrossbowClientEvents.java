package net.mirrorloong.chineseweapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.mirrorloong.chineseweapons.item.BedCrossbowBoltItem;
import net.mirrorloong.chineseweapons.network.BedCrossbowReleasePacket;
import net.mirrorloong.chineseweapons.network.BedCrossbowWindPacket;
import net.mirrorloong.chineseweapons.network.NetworkHandler;


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
    private static double lastGroundX;
    private static double lastGroundZ;
    private static boolean registered;

    private BedCrossbowClientEvents() {
    }

    //old api @Mod.EventBusSubscriber(CLIENT) + @SubscribeEvent -> new api init() 显式注册 Fabric 回调
    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        //old api RenderPlayerEvent.Pre + HumanoidModel.ArmPose.create -> new api ArmPoseEvents（HumanoidModelMixin）
        net.mirrorloong.chineseweapons.client.event.ArmPoseEvents.registerPre(BedCrossbowClientEvents::applyOperatingPre);
        net.mirrorloong.chineseweapons.client.event.ArmPoseEvents.register(BedCrossbowClientEvents::applyOperatingPose);
        //old api RenderHandEvent -> new api HandRenderEvents
        net.mirrorloong.chineseweapons.client.event.HandRenderEvents.registerFull(BedCrossbowClientEvents::onRenderHand);
        //old api TickEvent.ClientTickEvent(END) -> new api ClientTickEvents.END_CLIENT_TICK
        ClientTickEvents.END_CLIENT_TICK.register(BedCrossbowClientEvents::onClientTick);
        //old api TickEvent.RenderTickEvent(START) -> new api WorldRenderEvents.START（每帧）
        WorldRenderEvents.START.register(context -> {
            firstPersonArmsRendered = false;
            smoothOperatingView();
        });
        //old api InputEvent.InteractionKeyMappingTriggered -> new api UseItemCallback / UseBlockCallback
        UseItemCallback.EVENT.register(BedCrossbowClientEvents::onUseItem);
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (player instanceof LocalPlayer localPlayer
                    && BedCrossbowEntity.findOperating(localPlayer) != null) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }

    private static void applyOperatingPre(HumanoidModel<?> model, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }
        if (BedCrossbowEntity.findOperating(player) == null) {
            return;
        }
        model.crouching = true;
    }

    private static void applyOperatingPose(HumanoidModel<?> model, LivingEntity entity) {
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

        model.rightArm.xRot = ARM_BASE_X_ROT + stroke + shake;
        model.rightArm.yRot = ARM_BASE_Y_ROT;
        model.rightArm.zRot = ARM_BASE_Z_ROT;
        model.leftArm.xRot = ARM_BASE_X_ROT + stroke - shake;
        model.leftArm.yRot = -ARM_BASE_Y_ROT;
        model.leftArm.zRot = -ARM_BASE_Z_ROT;
    }

    private static boolean onRenderHand(PoseStack poseStack, Player player, InteractionHand hand, net.minecraft.world.item.ItemStack stack,
                                        float partialTick, MultiBufferSource buffer, int packedLight) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null || localPlayer.isInvisible()) {
            return false;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(localPlayer);
        if (crossbow == null) {
            return false;
        }

        if (firstPersonArmsRendered) {
            return true;
        }
        firstPersonArmsRendered = true;
        renderFirstPersonArms(poseStack, buffer, packedLight, localPlayer);
        return true;
    }

    private static void renderFirstPersonArms(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                              LocalPlayer player) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (!(renderer instanceof PlayerRenderer playerRenderer)) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        PartPose rightPose = model.rightArm.storePose();
        PartPose leftPose = model.leftArm.storePose();
        PartPose rightSleevePose = model.rightSleeve.storePose();
        PartPose leftSleevePose = model.leftSleeve.storePose();
        boolean rightVisible = model.rightArm.visible;
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
            model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            poseStack.pushPose();
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0D, 0.36D, -0.42D);
            poseStack.mulPose(Axis.XP.rotationDegrees(4.0F));

            drawArm(poseStack, buffer, packedLight, player,
                    model.rightArm, model.rightSleeve);
            drawArm(poseStack, buffer, packedLight, player,
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

    private static InteractionResultHolder<net.minecraft.world.item.ItemStack> onUseItem(
            Player player, net.minecraft.world.level.Level world, InteractionHand hand) {
        if (!(player instanceof LocalPlayer localPlayer)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        BedCrossbowEntity operating = BedCrossbowEntity.findOperating(localPlayer);
        if (operating == null) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        boolean spyglassAllowed = !operating.hasLoadedBolt()
                || operating.getCharge() >= 0.999F;
        boolean spyglass = spyglassAllowed
                && (player.getMainHandItem().getItem() instanceof SpyglassItem
                || player.getOffhandItem().getItem() instanceof SpyglassItem);
        boolean loading = !operating.hasLoadedBolt()
                && player.getMainHandItem().getItem() instanceof BedCrossbowBoltItem;
        if (!spyglass && !loading) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
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

    //old api MovementInputUpdateEvent -> new api 每 tick 回退水平位移并在 A/D 时绕床弩公转
    private static void applyMovementLock(LocalPlayer player, BedCrossbowEntity crossbow) {
        if (entryGlideTicks < ENTRY_GLIDE_TICKS) {
            lastGroundX = player.getX();
            lastGroundZ = player.getZ();
            return;
        }

        player.setPos(lastGroundX, player.getY(), lastGroundZ);
        player.setDeltaMovement(0.0D, player.getDeltaMovement().y, 0.0D);

        Minecraft minecraft = Minecraft.getInstance();
        boolean left = minecraft.options.keyLeft.isDown();
        boolean right = minecraft.options.keyRight.isDown();
        if (left == right) {
            lastGroundX = player.getX();
            lastGroundZ = player.getZ();
            return;
        }

        Vec3 center = crossbow.getWinchAxleCenter();
        double dx = player.getX() - center.x;
        double dz = player.getZ() - center.z;
        double radius = Math.sqrt(dx * dx + dz * dz);
        if (radius >= 0.8D && radius <= 4.0D) {
            double step = (right ? 1.0D : -1.0D) * 0.035D;
            double angle = Math.atan2(dx, dz) + step;
            player.setPos(center.x + Math.sin(angle) * radius, player.getY(), center.z + Math.cos(angle) * radius);
        }

        lastGroundX = player.getX();
        lastGroundZ = player.getZ();
    }

    private static void onClientTick(Minecraft minecraft) {
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
            lastGroundX = player.getX();
            lastGroundZ = player.getZ();
            return;
        }

        applyEntryGlide(player, operating);
        applyMovementLock(player, operating);

        boolean jumpDown = minecraft.options.keyJump.isDown();
        if (jumpDown && !jumpWasDown) {
            NetworkHandler.sendToServer(new BedCrossbowReleasePacket());
        }
        jumpWasDown = jumpDown;

        boolean useDown = minecraft.options.keyUse.isDown();
        if (useDown) {
            useHeldTicks++;
        } else {
            useHeldTicks = 0;
        }
        if (useDown && useHeldTicks >= WIND_HOLD_TICKS) {
            NetworkHandler.sendToServer(new BedCrossbowWindPacket(true));
        } else {
            NetworkHandler.sendToServer(new BedCrossbowWindPacket(false));
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

    public static void triggerCrossbowShake() {
        crossbowShakeTicks = 12;
        crossbowShakeSeed = (float) (Math.random() * Math.PI * 2.0D);
    }

    public static boolean hasCameraShake() {
        return crossbowShakeTicks > 0;
    }

    public static float shakeYaw(float partialTick) {
        if (crossbowShakeTicks <= 0) {
            return 0.0F;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        float tick = player == null ? 0.0F : player.tickCount;
        float remain = crossbowShakeTicks / 12.0F;
        float strength = remain * remain;
        float phase = (tick + partialTick) * 2.0F + crossbowShakeSeed;
        return Mth.sin(phase) * 0.45F * strength;
    }

    public static float shakePitch(float partialTick) {
        if (crossbowShakeTicks <= 0) {
            return 0.0F;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        float tick = player == null ? 0.0F : player.tickCount;
        float remain = crossbowShakeTicks / 12.0F;
        float strength = remain * remain;
        float phase = (tick + partialTick) * 2.0F + crossbowShakeSeed;
        return Mth.cos(phase * 1.3F) * 0.30F * strength;
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
