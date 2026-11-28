package net.mirrorloong.chineseweapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.item.BedCrossbowBoltItem;
import net.mirrorloong.chineseweapons.network.BedCrossbowShakePacket;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;

public class BedCrossbowEntity extends Entity {

    public static final float MAX_HEALTH = 3000.0F;
    public static final int CHARGE_TICKS = 80;
    public static final int SHAKE_TICKS = 80;
    public static final int MEDIUM_SHAKE_TICKS = 140;
    public static final int FORCE_FIRE_TICKS = 170;
    private static final int STRENGTH_I_FORCE_TICKS = 300;
    private static final int STRENGTH_II_FORCE_TICKS = 600;
    public static final int RELEASE_ANIM_TICKS = 13;
    public static final float BOW_BASE_PITCH = 5.0F;
    private static final float MAX_AIM_YAW = 30.0F;
    private static final float MAX_AIM_PITCH_UP = 30.0F;
    private static final float MAX_AIM_PITCH_DOWN = 20.0F;
    private static final double BOLT_LAUNCH_SPEED = 3.0D;
    private static final double MAX_OPERATE_DISTANCE_SQR = 16.0D;
    private static final double BOW_PIVOT_Y = 0.9364D;
    private static final double BOW_PIVOT_FORWARD = -0.5925D;
    private static final double WINCH_AXLE_Y = 0.8754D;
    private static final double WINCH_AXLE_BACK = 0.9194D;

    private static final EntityDataAccessor<Float> DATA_HP =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_CHARGE =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AIM =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> DATA_SHAKE =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_LOADED =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DATA_LOADED_STACK =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<String> DATA_OPERATOR =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_RELEASE =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_WINDING =
            SynchedEntityData.defineId(BedCrossbowEntity.class, EntityDataSerializers.BOOLEAN);

    private CompoundTag storedItem = new CompoundTag();
    private ItemStack loadedStack = ItemStack.EMPTY;

    private UUID operatorId;
    private UUID recoverPlayerId;
    private int recoverTicks;
    private UUID loadedBoltUuid;
    private int loadedBoltId = -1;

    private float baseYaw;
    private int operateTicks;
    private int acheTicks;
    private int foodTicks;
    private int releaseTicks;
    private boolean chargeSoundPlayed;

    public BedCrossbowEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HP, MAX_HEALTH);
        this.entityData.define(DATA_CHARGE, 0.0F);
        this.entityData.define(DATA_AIM, 0.0F);
        this.entityData.define(DATA_SHAKE, (byte) 0);
        this.entityData.define(DATA_LOADED, false);
        this.entityData.define(DATA_LOADED_STACK, ItemStack.EMPTY);
        this.entityData.define(DATA_OPERATOR, "");
        this.entityData.define(DATA_RELEASE, 0);
        this.entityData.define(DATA_WINDING, false);
    }

    public float getHealth() {
        return this.entityData.get(DATA_HP);
    }

    public void setHealth(float health) {
        this.entityData.set(DATA_HP, Mth.clamp(health, 0.0F, MAX_HEALTH));
    }

    public float getCharge() {
        return this.entityData.get(DATA_CHARGE);
    }

    public void setCharge(float charge) {
        this.entityData.set(DATA_CHARGE, Mth.clamp(charge, 0.0F, 1.0F));
    }

    public float getAimPitch() {
        return this.entityData.get(DATA_AIM);
    }

    private void setAimPitch(float pitch) {
        this.entityData.set(DATA_AIM, Mth.clamp(pitch, -MAX_AIM_PITCH_UP, MAX_AIM_PITCH_DOWN));
    }

    public float getBoltXRot() {
        return this.getAimPitch() + BedCrossbowBoltEntity.MODEL_TILT - BOW_BASE_PITCH;
    }

    public float getBowUpAngle() {
        return BOW_BASE_PITCH - this.getAimPitch();
    }

    public boolean isWinding() {
        return this.entityData.get(DATA_WINDING);
    }

    public void setWinding(ServerPlayer player, boolean winding) {
        if (!this.isOperating(player)) {
            return;
        }
        if (winding) {
            this.entityData.set(DATA_WINDING, true);
            this.acheTicks = 0;
            this.setShake((byte) 0);
            return;
        }
        if (!this.isWinding()) {
            return;
        }
        this.entityData.set(DATA_WINDING, false);
        this.acheTicks = 0;
        this.setShake((byte) 0);
    }

    public byte getShake() {
        return this.entityData.get(DATA_SHAKE);
    }

    private void setShake(byte shake) {
        this.entityData.set(DATA_SHAKE, shake);
    }

    public boolean hasLoadedBolt() {
        return this.entityData.get(DATA_LOADED);
    }

    public ItemStack getLoadedStack() {
        return this.entityData.get(DATA_LOADED_STACK);
    }

    private void setLoadedStack(ItemStack stack) {
        this.loadedStack = stack;
        this.entityData.set(DATA_LOADED_STACK, stack);
        this.setLoaded(!stack.isEmpty());
    }

    private void setLoaded(boolean loaded) {
        this.entityData.set(DATA_LOADED, loaded);
    }

    public int getSyncedReleaseTicks() {
        return this.entityData.get(DATA_RELEASE);
    }

    private void setReleaseTicks(int ticks) {
        this.releaseTicks = ticks;
        this.entityData.set(DATA_RELEASE, ticks);
    }

    public void setBaseYaw(float baseYaw) {
        this.baseYaw = baseYaw;
    }

    public boolean isOperating(Player player) {
        return player != null && !this.entityData.get(DATA_OPERATOR).isEmpty()
                && this.entityData.get(DATA_OPERATOR).equals(player.getUUID().toString());
    }

    private ServerPlayer getOperator() {
        if (this.operatorId == null) {
            return null;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().getPlayerList().getPlayer(this.operatorId);
        }
        return null;
    }

    private boolean hasOperator() {
        return !this.entityData.get(DATA_OPERATOR).isEmpty();
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack.save(new CompoundTag());
    }

    public ItemStack getStoredItem() {
        return ItemStack.of(this.storedItem);
    }

    private void giveItem(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.isOperating(player)) {
            boolean spyglass = (!this.hasLoadedBolt() || this.getCharge() >= 0.999F)
                    && (player.getMainHandItem().getItem() instanceof SpyglassItem
                    || player.getOffhandItem().getItem() instanceof SpyglassItem);
            if (spyglass) {
                return InteractionResult.PASS;
            }
            if (!this.level().isClientSide
                    && hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown()
                    && !this.hasLoadedBolt()
                    && player.getMainHandItem().getItem() instanceof BedCrossbowBoltItem) {
                this.loadBolt(player, player.getMainHandItem());
            }
            return InteractionResult.CONSUME;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (this.level().isClientSide) {
            boolean handled = player.isShiftKeyDown() && this.hasLoadedBolt()
                    || held.getItem() instanceof BedCrossbowBoltItem
                    || (!this.hasOperator() && !player.isShiftKeyDown()
                        && held.isEmpty() && this.isBehind(player));
            return handled ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (this.hasLoadedBolt()) {
                this.removeBolt(player);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        if (held.getItem() instanceof BedCrossbowBoltItem) {
            if (this.hasLoadedBolt()) {
                player.displayClientMessage(Component.translatable(
                        "message.chineseweapons.bed_crossbow.already_loaded"), true);
                return InteractionResult.CONSUME;
            }
            this.loadBolt(player, held);
            return InteractionResult.CONSUME;
        }

        if (!this.hasOperator() && held.isEmpty() && this.isBehind(player)) {
            if (this.startOperating(player)) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean isBehind(Player player) {
        Vec3 toPlayer = player.position().subtract(this.position());
        return toPlayer.dot(this.getForward()) < 0.0D && player.distanceToSqr(this) < 16.0D;
    }

    private void loadBolt(Player player, ItemStack held) {
        this.setLoadedStack(held.copyWithCount(1));
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
        this.spawnDockedBolt();
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void removeBolt(Player player) {
        if (this.hasOperator()) {
            this.release(false);
        }
        if (this.isRemoved()) {
            return;
        }
        ItemStack out = this.getLoadedStack().copy();
        this.setLoadedStack(ItemStack.EMPTY);
        this.discardDockedBolt();
        this.giveItem(player, out);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private boolean startOperating(Player player) {
        if (!player.getMainHandItem().isEmpty()) {
            return false;
        }
        if (this.hasOperator() || this.recoverTicks > 0) {
            return false;
        }

        this.operatorId = player.getUUID();
        this.entityData.set(DATA_OPERATOR, this.operatorId.toString());
        this.entityData.set(DATA_WINDING, false);
        this.baseYaw = this.getYRot();
        this.operateTicks = 0;
        this.acheTicks = 0;
        this.foodTicks = 0;
        this.chargeSoundPlayed = false;
        this.setCharge(0.0F);
        this.setShake((byte) 0);

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 1.0F, 0.9F);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable(this.hasLoadedBolt()
                    ? "message.chineseweapons.bed_crossbow.press_back"
                    : "message.chineseweapons.bed_crossbow.load_to_charge"), true);
        }
        return true;
    }

    public void fireFromSkill(ServerPlayer player) {
        if (!this.isOperating(player) || !this.hasLoadedBolt() || this.getCharge() <= 0.001F) {
            return;
        }
        this.shoot(player, this.getCharge());
        this.entityData.set(DATA_WINDING, false);
        this.setCharge(0.0F);
        this.setShake((byte) 0);
        this.acheTicks = 0;
        this.foodTicks = 0;
        this.chargeSoundPlayed = false;
    }

    public void release(boolean fire) {
        if (this.level().isClientSide) {
            return;
        }
        ServerPlayer operator = this.getOperator();
        if (fire && operator != null && this.getCharge() > 0.001F && this.hasLoadedBolt()) {
            this.shoot(operator, this.getCharge());
        }

        this.operatorId = null;
        this.entityData.set(DATA_OPERATOR, "");
        this.entityData.set(DATA_WINDING, false);
        this.setCharge(0.0F);
        this.setShake((byte) 0);
        this.operateTicks = 0;
        this.acheTicks = 0;
        this.foodTicks = 0;
        this.chargeSoundPlayed = false;

        if (this.getHealth() <= 0.0F) {
            this.destroyCrossbow();
        }
    }

    //回收
    public void recover(ServerPlayer player) {
        if (this.isRemoved() || this.recoverTicks > 0) {
            return;
        }
        if (this.getHealth() <= 0.0F) {
            this.destroyCrossbow();
            return;
        }
        this.recoverPlayerId = player.getUUID();
        this.recoverTicks = 60;
        this.release(false);
    }

    private void finishRecover(ServerPlayer player) {
        this.recoverTicks = 0;
        this.recoverPlayerId = null;

        ItemStack out = this.getStoredItem();
        if (out.isEmpty()) {
            out = new ItemStack(ChineseweaponsModItems.BED_CROSSBOW_ITEM.get());
        }
        int max = out.getMaxDamage();
        if (max > 0) {
            out.setDamageValue(Mth.clamp(max - (int) this.getHealth(), 0, max));
        }

        if (this.hasLoadedBolt()) {
            this.giveItem(player, this.getLoadedStack().copy());
            this.setLoadedStack(ItemStack.EMPTY);
            this.discardDockedBolt();
        }

        this.giveItem(player, out);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 0.9F);
        this.discard();
    }

    private void shoot(ServerPlayer operator, float charge) {
        if (!this.hasLoadedBolt()) {
            return;
        }

        float range = BedCrossbowBoltItem.kindOf(this.loadedStack).getRange();
        Vec3 target = this.findAimTarget(operator, range);
        Vec3 muzzle = this.position().add(0.0D, 1.05D, 0.0D).add(this.getForward().scale(0.6D));
        Vec3 velocity = this.solveBallistic(muzzle, target);
        if (velocity == null) {
            Vec3 fallback = new Vec3(target.x - muzzle.x, 0.0D, target.z - muzzle.z);
            velocity = fallback.normalize().scale(BOLT_LAUNCH_SPEED).add(0.0D, 0.9D, 0.0D);
        }

        BedCrossbowBoltEntity bolt = new BedCrossbowBoltEntity(this.level(), operator, this.loadedStack.copy());
        this.setAimPitch(operator.getXRot());
        float boltYaw = (float) (Math.atan2(-velocity.x, velocity.z) * (180.0D / Math.PI));
        float boltPitch = (float) (-Math.atan2(velocity.y,
                Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)) * (180.0D / Math.PI));
        bolt.setYRot(boltYaw);
        bolt.setXRot(boltPitch + BedCrossbowBoltEntity.MODEL_TILT);
        bolt.setFlightPower(charge);
        bolt.setDeltaMovement(velocity);
        bolt.setPos(muzzle.x, muzzle.y, muzzle.z);
        this.level().addFreshEntity(bolt);

        this.setLoadedStack(ItemStack.EMPTY);
        this.discardDockedBolt();
        this.setReleaseTicks(RELEASE_ANIM_TICKS);

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        this.sendFiringShake();

        this.setHealth(this.getHealth() - 1.0F);
        if (this.getHealth() <= 0.0F) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private Vec3 findAimTarget(ServerPlayer operator, float range) {
        double yaw = Math.toRadians(operator.getYRot());
        double pitch = Math.toRadians(Mth.clamp(operator.getXRot(), -MAX_AIM_PITCH_UP, MAX_AIM_PITCH_DOWN));
        Vec3 look = new Vec3(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch),
                Math.cos(yaw) * Math.cos(pitch)).normalize();
        Vec3 eye = operator.getEyePosition();
        Vec3 end = eye.add(look.scale(range));
        BlockHitResult hit = this.level().clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, operator));
        return hit.getType() == HitResult.Type.BLOCK ? hit.getLocation() : end;
    }

    private Vec3 solveBallistic(Vec3 muzzle, Vec3 target) {
        double dx = target.x - muzzle.x;
        double dz = target.z - muzzle.z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        double height = target.y - muzzle.y;
        double v2 = BOLT_LAUNCH_SPEED * BOLT_LAUNCH_SPEED;

        if (horizontal < 1.0E-4D) {
            return new Vec3(0.0D, height >= 0.0D ? BOLT_LAUNCH_SPEED : -BOLT_LAUNCH_SPEED, 0.0D);
        }

        double discriminant = v2 * v2
                - BedCrossbowBoltEntity.FLIGHT_GRAVITY
                * (BedCrossbowBoltEntity.FLIGHT_GRAVITY * horizontal * horizontal + 2.0D * height * v2);
        if (discriminant < 0.0D) {
            return null;
        }

        double angle = Math.atan((v2 - Math.sqrt(discriminant))
                / (BedCrossbowBoltEntity.FLIGHT_GRAVITY * horizontal));
        double horizontalSpeed = BOLT_LAUNCH_SPEED * Math.cos(angle);
        double verticalSpeed = BOLT_LAUNCH_SPEED * Math.sin(angle);
        return new Vec3(dx / horizontal * horizontalSpeed, verticalSpeed, dz / horizontal * horizontalSpeed);
    }

    public Vec3 getForward() {
        float yaw = (float) Math.toRadians(this.getYRot());
        return new Vec3(-Math.sin(yaw), 0.0D, Math.cos(yaw));
    }

    public Vec3 getWinchAxleCenter() {
        return this.position()
                .add(0.0D, WINCH_AXLE_Y, 0.0D)
                .subtract(this.getForward().scale(WINCH_AXLE_BACK));
    }

    public Vec3 getBoltAttachPos() {
        double angle = Math.toRadians(this.getBowUpAngle());
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double up = 0.25D;
        double forwardOffset = 0.9425D;
        double rotatedUp = up * cos + forwardOffset * sin;
        double rotatedForward = forwardOffset * cos - up * sin;

        double y = this.getY() + BOW_PIVOT_Y + rotatedUp;
        double forward = BOW_PIVOT_FORWARD + rotatedForward;
        Vec3 facing = this.getForward();

        return new Vec3(
                this.getX() + facing.x * forward,
                y,
                this.getZ() + facing.z * forward);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getSyncedReleaseTicks() > 0 && this.releaseTicks > 0) {
            this.setReleaseTicks(this.releaseTicks - 1);
        }

        if (this.level().isClientSide) {
            return;
        }

        this.ensureLoadedBolt();

        if (this.recoverTicks > 0) {
            ServerPlayer recoverPlayer = this.recoverPlayerId == null || !(this.level() instanceof ServerLevel serverLevel)
                    ? null
                    : serverLevel.getServer().getPlayerList().getPlayer(this.recoverPlayerId);
            if (recoverPlayer == null || recoverPlayer.level() != this.level()
                    || recoverPlayer.distanceToSqr(this) > 64.0D) {
                this.recoverTicks = 0;
                this.recoverPlayerId = null;
            } else if (--this.recoverTicks <= 0) {
                this.finishRecover(recoverPlayer);
                return;
            } else if (this.recoverTicks % 10 == 0) {
                recoverPlayer.displayClientMessage(Component.translatable(
                        "message.chineseweapons.bed_crossbow.recover_status",
                        String.format("%.1f", this.recoverTicks / 20.0F) + "s"), true);
            }
        }

        ServerPlayer operator = this.getOperator();
        if (this.operatorId != null && operator == null) {
            this.release(false);
            return;
        }
        if (operator == null) {
            return;
        }
        if (operator.level() != this.level() || !operator.isAlive() || operator.isSpectator()) {
            this.release(false);
            return;
        }
        if (operator.distanceToSqr(this) > MAX_OPERATE_DISTANCE_SQR) {
            this.release(false);
            return;
        }
        if (this.operateTicks > 8 && operator.isShiftKeyDown()) {
            this.release(true);
            return;
        }

        Vec3 winch = this.getWinchAxleCenter();
        double dx = operator.getX() - winch.x;
        double dz = operator.getZ() - winch.z;
        float radialYaw = dx * dx + dz * dz < 1.0E-4D
                ? this.baseYaw
                : (float) (Math.atan2(-dx, dz) * (180.0D / Math.PI)) + 180.0F;
        float fineYaw = Mth.clamp(Mth.wrapDegrees(operator.getYRot() - radialYaw),
                -MAX_AIM_YAW, MAX_AIM_YAW);
        this.setYRot(radialYaw + fineYaw);
        this.setAimPitch(Mth.clamp(operator.getXRot(), -MAX_AIM_PITCH_UP, MAX_AIM_PITCH_DOWN));
        this.operateTicks++;

        if (!this.hasLoadedBolt()) {
            this.entityData.set(DATA_WINDING, false);
            this.setCharge(0.0F);
            this.setShake((byte) 0);
            this.acheTicks = 0;
            this.foodTicks = 0;
            return;
        }

        if (this.getCharge() >= 1.0F) {
            this.acheTicks = 0;
            this.setShake((byte) 0);
            return;
        }

        if (this.isWinding()) {
            this.acheTicks = 0;
            this.setShake((byte) 0);
            float charge = Math.min(1.0F, this.getCharge() + 1.0F / CHARGE_TICKS);
            this.setCharge(charge);

            if (operator.getFoodData().getFoodLevel() > 0 && ++this.foodTicks >= 16) {
                this.foodTicks = 0;
                operator.getFoodData().setFoodLevel(
                        Math.max(0, operator.getFoodData().getFoodLevel() - 1));
            }

            if (charge >= 1.0F) {
                if (!this.chargeSoundPlayed) {
                    this.chargeSoundPlayed = true;
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                this.acheTicks = 0;
                this.setShake((byte) 0);
            }
            return;
        }

        if (this.getCharge() <= 0.001F) {
            this.acheTicks = 0;
            this.setShake((byte) 0);
            return;
        }

        int maxAcheTicks = this.getForcedFireTicks(operator);
        if (maxAcheTicks <= 0) {
            this.acheTicks = 0;
            this.setShake((byte) 0);
            return;
        }

        int lightTicks = Math.max(1, Math.round(
                maxAcheTicks * (SHAKE_TICKS / (float) FORCE_FIRE_TICKS)));
        int mediumTicks = Math.max(lightTicks, Math.round(
                maxAcheTicks * (MEDIUM_SHAKE_TICKS / (float) FORCE_FIRE_TICKS)));

        this.acheTicks++;
        if (this.acheTicks >= mediumTicks) {
            this.setShake((byte) 2);
        } else if (this.acheTicks >= lightTicks) {
            this.setShake((byte) 1);
        } else {
            this.setShake((byte) 0);
        }
        if (this.acheTicks >= maxAcheTicks) {
            this.release(true);
        }
    }

    private int getForcedFireTicks(ServerPlayer player) {
        MobEffectInstance strength = player.getEffect(MobEffects.DAMAGE_BOOST);
        if (strength == null) {
            return FORCE_FIRE_TICKS;
        }
        int amplifier = strength.getAmplifier();
        if (amplifier >= 2) {
            return 0;
        }
        return amplifier == 1 ? STRENGTH_II_FORCE_TICKS : STRENGTH_I_FORCE_TICKS;
    }

    private void sendFiringShake() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) <= 144.0D) {
                ChineseweaponsMod.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new BedCrossbowShakePacket());
            }
        }
    }

    private void ensureLoadedBolt() {
        if (this.loadedStack.isEmpty()) {
            if (this.getDockedBolt() != null) {
                this.discardDockedBolt();
            }
            return;
        }
        if (this.getDockedBolt() == null) {
            this.spawnDockedBolt();
        }
    }

    private void spawnDockedBolt() {
        if (this.level().isClientSide || this.loadedStack.isEmpty()) {
            return;
        }
        BedCrossbowBoltEntity bolt = new BedCrossbowBoltEntity(this.level(), this, this.loadedStack.copy());
        this.level().addFreshEntity(bolt);
        this.loadedBoltId = bolt.getId();
        this.loadedBoltUuid = bolt.getUUID();
    }

    private BedCrossbowBoltEntity getDockedBolt() {
        if (this.loadedBoltId >= 0) {
            Entity entity = this.level().getEntity(this.loadedBoltId);
            if (entity instanceof BedCrossbowBoltEntity bolt && bolt.isDockedTo(this)) {
                return bolt;
            }
        }
        List<BedCrossbowBoltEntity> bolts = this.level().getEntitiesOfClass(
                BedCrossbowBoltEntity.class, this.getBoundingBox().inflate(5.0D),
                bolt -> bolt.isDockedTo(this));
        return bolts.isEmpty() ? null : bolts.get(0);
    }

    private void discardDockedBolt() {
        BedCrossbowBoltEntity bolt = this.getDockedBolt();
        if (bolt != null) {
            bolt.discard();
        }
        this.loadedBoltId = -1;
        this.loadedBoltUuid = null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return false;
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
        this.setHealth(this.getHealth() - amount);
        if (this.getHealth() <= 0.0F) {
            this.destroyCrossbow();
        }
        return true;
    }

    private void destroyCrossbow() {
        if (this.isRemoved()) {
            return;
        }
        ItemStack out = this.getStoredItem();
        if (!out.isEmpty()) {
            out.setDamageValue(Math.max(0, out.getMaxDamage()));
            this.spawnAtLocation(out);
        }
        if (this.hasLoadedBolt()) {
            this.spawnAtLocation(this.getLoadedStack().copy());
        }
        this.setLoadedStack(ItemStack.EMPTY);
        this.discardDockedBolt();
        this.operatorId = null;
        this.entityData.set(DATA_OPERATOR, "");
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        this.discard();
    }

    public static boolean canPlaceAt(Level level, double x, double y, double z) {
        return canPlaceAt(level, null, x, y, z);
    }

    public static boolean canPlaceAt(Level level, Entity ignore, double x, double y, double z) {
        AABB box = new AABB(x - 1.2D, y, z - 1.2D, x + 1.2D, y + 1.4D, z + 1.2D);
        if (!level.noCollision(ignore, box)) {
            return false;
        }
        BlockPos below = BlockPos.containing(x, y - 0.01D, z);
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    public static BedCrossbowEntity findOperating(Player player) {
        if (player == null) {
            return null;
        }
        for (BedCrossbowEntity crossbow : player.level().getEntitiesOfClass(
                BedCrossbowEntity.class, player.getBoundingBox().inflate(8.0D))) {
            if (crossbow.isOperating(player)) {
                return crossbow;
            }
        }
        return null;
    }

    public static BedCrossbowEntity findTarget(ServerPlayer player) {
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(5.0D));

        BedCrossbowEntity best = null;
        double bestDistance = Double.MAX_VALUE;

        for (BedCrossbowEntity crossbow : player.level().getEntitiesOfClass(
                BedCrossbowEntity.class, player.getBoundingBox().inflate(5.0D))) {
            var hit = crossbow.getBoundingBox().inflate(0.1D).clip(eye, end);
            if (hit.isEmpty()) {
                continue;
            }
            double distance = eye.distanceToSqr(hit.get());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = crossbow;
            }
        }
        return best;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 16384.0D;
    }

    //存档
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("StoredItem")) {
            this.storedItem = tag.getCompound("StoredItem");
        }
        if (tag.contains("LoadedBolt")) {
            this.setLoadedStack(ItemStack.of(tag.getCompound("LoadedBolt")));
        }
        this.setHealth(tag.getFloat("HP"));
        this.setYRot(tag.getFloat("Yaw"));
        this.setBaseYaw(tag.getFloat("BaseYaw"));
        this.setAimPitch(tag.getFloat("Aim"));
        this.setCharge(0.0F);
        this.setShake((byte) 0);
        this.setReleaseTicks(0);
        this.entityData.set(DATA_WINDING, false);
        this.operatorId = null;
        this.entityData.set(DATA_OPERATOR, "");
        //clean
        this.setCustomName(null);
        this.setCustomNameVisible(false);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("StoredItem", this.storedItem);
        if (!this.loadedStack.isEmpty()) {
            tag.put("LoadedBolt", this.loadedStack.save(new CompoundTag()));
        }
        tag.putFloat("HP", this.getHealth());
        tag.putFloat("Yaw", this.getYRot());
        tag.putFloat("BaseYaw", this.baseYaw);
        tag.putFloat("Aim", this.getAimPitch());
    }
}
