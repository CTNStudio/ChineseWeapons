package net.mirrorloong.chineseweapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.mirrorloong.chineseweapons.item.BedCrossbowBoltItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BedCrossbowBoltEntity extends Entity {

    public static final byte STATE_DOCKED = 0;
    public static final byte STATE_FLYING = 1;
    public static final byte STATE_STUCK = 2;
    public static final byte STATE_PEG = 3;
    public static final byte STATE_FALLING = 4;
    public static final float MODEL_TILT = 10.0F;
    public static final double FLIGHT_GRAVITY = 0.004D;
    private static final double BOX_WIDTH = 0.4D;
    private static final double BOX_HEIGHT = 0.6D;
    private static final double BOX_LENGTH = 2.0D;
    private static final double HALF_WIDTH = BOX_WIDTH / 2.0D;
    private static final double HALF_HEIGHT = BOX_HEIGHT / 2.0D;
    private static final double HALF_LENGTH = BOX_LENGTH / 2.0D;

    private static final EntityDataAccessor<Byte> DATA_STATE =
            SynchedEntityData.defineId(BedCrossbowBoltEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(BedCrossbowBoltEntity.class, EntityDataSerializers.BYTE);

    private ItemStack boltStack = ItemStack.EMPTY;
    private UUID ownerUUID;
    private UUID parentUUID;
    private int parentId = -1;
    private double traveled;
    private float flightPower = 1.0F;
    private int life;
    private final Set<Integer> hitEntities = new HashSet<>();
    private byte clientLastState = STATE_DOCKED;
    private float clientLastYaw;
    private float clientLastPitch;
    private AABB frozenBox;
    private BlockPos stuckSupportPos;

    public BedCrossbowBoltEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public BedCrossbowBoltEntity(Level level, BedCrossbowEntity parent, ItemStack stack) {
        this(ChineseweaponsModEntities.BED_CROSSBOW_BOLT.get(), level);
        this.boltStack = stack.copy();
        this.setBoltType(BedCrossbowBoltItem.kindOf(stack));
        this.parentUUID = parent.getUUID();
        this.parentId = parent.getId();
        this.setState(STATE_DOCKED);
        this.setPos(parent.getBoltAttachPos().x, parent.getBoltAttachPos().y, parent.getBoltAttachPos().z);
        this.setYRot(parent.getYRot());
        this.setXRot(parent.getBoltXRot());
    }

    public BedCrossbowBoltEntity(Level level, LivingEntity owner, ItemStack stack) {
        this(ChineseweaponsModEntities.BED_CROSSBOW_BOLT.get(), level);
        this.boltStack = stack.copy();
        this.setBoltType(BedCrossbowBoltItem.kindOf(stack));
        this.ownerUUID = owner.getUUID();
        this.setState(STATE_FLYING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_STATE, STATE_DOCKED);
        builder.define(DATA_TYPE, (byte) 0);
    }

    public byte getState() {
        return this.entityData.get(DATA_STATE);
    }

    private void setState(byte state) {
        this.entityData.set(DATA_STATE, state);
    }

    public BedCrossbowBoltItem.BoltKind getKind() {
        return BedCrossbowBoltItem.BoltKind.byId(this.entityData.get(DATA_TYPE));
    }

    private void setBoltType(BedCrossbowBoltItem.BoltKind kind) {
        this.entityData.set(DATA_TYPE, (byte) kind.ordinal());
    }

    public ItemStack getBoltStack() {
        return this.boltStack.copy();
    }

    public void setFlightPower(float power) {
        this.flightPower = Mth.clamp(power, 0.05F, 1.0F);
    }

    public float getFlightPower() {
        return this.flightPower;
    }

    public float getEffectiveRange() {
        return this.getKind().getRange() * Math.max(0.2F, this.flightPower);
    }

    public boolean isDockedTo(BedCrossbowEntity crossbow) {
        return this.getState() == STATE_DOCKED
                && this.parentUUID != null
                && this.parentUUID.equals(crossbow.getUUID());
    }

    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID,
                "textures/entity/" + this.getKind().getName() + ".png");
    }

    private LivingEntity getOwnerEntity() {
        if (this.ownerUUID == null) {
            return null;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (player != null) {
                return player;
            }
        }
        return this.level().getPlayerByUUID(this.ownerUUID);
    }

    private BedCrossbowEntity getParentCrossbow() {
        if (this.parentId >= 0) {
            Entity entity = this.level().getEntity(this.parentId);
            if (entity instanceof BedCrossbowEntity crossbow) {
                return crossbow;
            }
        }
        if (this.parentUUID != null) {
            List<BedCrossbowEntity> list = this.level().getEntitiesOfClass(
                    BedCrossbowEntity.class, this.getBoundingBox().inflate(8.0D),
                    crossbow -> crossbow.getUUID().equals(this.parentUUID));
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        this.life++;

        if (this.level().isClientSide) {
            byte state = this.getState();
            byte previousState = this.clientLastState;
            boolean stateChanged = state != this.clientLastState;
            boolean rotationChanged = this.getYRot() != this.clientLastYaw
                    || this.getXRot() != this.clientLastPitch;
            this.clientLastState = state;
            this.clientLastYaw = this.getYRot();
            this.clientLastPitch = this.getXRot();
            if (state == STATE_STUCK && this.frozenBox == null
                    && (previousState == STATE_FLYING || previousState == STATE_FALLING)) {
                this.frozenBox = this.getBoundingBox();
            } else if (state == STATE_FALLING) {
                this.frozenBox = null;
            }
            if (stateChanged || rotationChanged || state == STATE_FLYING
                    || state == STATE_DOCKED || state == STATE_FALLING) {
                this.setBoundingBox(this.makeBoundingBox());
            }
            return;
        }

        switch (this.getState()) {
            case STATE_DOCKED -> this.followParent();
            case STATE_FLYING -> this.moveBolt();
            case STATE_STUCK -> this.checkStuckSupport();
            case STATE_PEG -> {
                //pass
            }
            case STATE_FALLING -> this.tickFalling();
            default -> this.discard();
        }

        if (this.life > 2400) {
            this.discard();
        }
    }

    private void followParent() {
        BedCrossbowEntity parent = this.getParentCrossbow();
        if (parent == null || parent.isRemoved() || !parent.hasLoadedBolt()) {
            this.discard();
            return;
        }
        Vec3 pos = parent.getBoltAttachPos();
        this.setPos(pos.x, pos.y, pos.z);
        this.setYRot(parent.getYRot());
        this.setXRot(parent.getBoltXRot());
    }

    private void checkStuckSupport() {
        if (this.life % 10 != 0) {
            return;
        }
        BlockPos support = this.stuckSupportPos != null
                ? this.stuckSupportPos : this.blockPosition().below();
        if (!this.level().getBlockState(support)
                .getCollisionShape(this.level(), support).isEmpty()) {
            return;
        }
        this.frozenBox = null;
        this.stuckSupportPos = null;
        this.setState(STATE_FALLING);
        this.noPhysics = true;
        this.setNoGravity(false);
        this.setDeltaMovement(0.0D, -0.04D, 0.0D);
    }

    private void tickFalling() {
        Vec3 motion = this.getDeltaMovement().add(0.0D, -0.04D, 0.0D);
        this.setDeltaMovement(motion);
        this.setYRot((float) (Mth.atan2(-motion.x, motion.z) * 180.0D / Math.PI));
        this.setXRot((float) (-Mth.atan2(motion.y,
                Math.sqrt(motion.x * motion.x + motion.z * motion.z))
                * 180.0D / Math.PI) + MODEL_TILT);

        Vec3 from = this.position();
        Vec3 to = from.add(motion);
        BlockHitResult hit = this.level().clip(new ClipContext(from, to,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() == HitResult.Type.MISS) {
            this.setPos(to.x, to.y, to.z);
            return;
        }

        Direction direction = hit.getDirection();
        this.setPos(hit.getLocation().x + direction.getStepX() * 0.05D,
                hit.getLocation().y + direction.getStepY() * 0.05D,
                hit.getLocation().z + direction.getStepZ() * 0.05D);
        if (this.getKind() == BedCrossbowBoltItem.BoltKind.SPEAR_THREE_SWORD) {
            this.discard();
            return;
        }
        this.setDeltaMovement(Vec3.ZERO);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.stuckSupportPos = hit.getBlockPos();
        this.frozenBox = this.makeBoundingBox();
        this.setState(STATE_STUCK);
    }

    private void moveBolt() {
        Vec3 current = this.getDeltaMovement();
        Vec3 motion = current.add(0.0D, -FLIGHT_GRAVITY, 0.0D);
        this.setDeltaMovement(motion);

        double speed = motion.length();
        if (speed < 0.01D) {
            this.frozenBox = null;
            this.setState(STATE_FALLING);
            this.noPhysics = true;
            this.setNoGravity(false);
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }

        this.setYRot((float) (Mth.atan2(-motion.x, motion.z) * 180.0D / Math.PI));
        this.setXRot((float) (-Mth.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z))
                * 180.0D / Math.PI) + MODEL_TILT);

        int steps = Math.max(1, (int) Math.ceil(speed / 0.35D));
        Vec3 step = motion.scale(1.0D / steps);

        for (int i = 0; i < steps; i++) {
            Vec3 from = this.position();
            Vec3 to = from.add(step);

            AABB sweep = this.getBoundingBox().expandTowards(step).inflate(0.3D);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    this.level(), this, from, to, sweep, this::canHitEntity);
            BlockHitResult blockHit = this.level().clip(new ClipContext(from, to,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            double entityDistance = entityHit == null
                    ? Double.MAX_VALUE : from.distanceToSqr(entityHit.getLocation());
            double blockDistance = blockHit.getType() == HitResult.Type.MISS
                    ? Double.MAX_VALUE : from.distanceToSqr(blockHit.getLocation());

            if (entityHit != null && entityDistance <= blockDistance) {
                this.onHitEntity(entityHit);
            }
            if (blockHit.getType() != HitResult.Type.MISS && blockDistance <= entityDistance) {
                this.setPos(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z);
                this.onHitBlock(blockHit);
                return;
            }

            this.setPos(to.x, to.y, to.z);
            this.traveled += step.length();
        }

        if (this.traveled >= this.getEffectiveRange()) {
            this.setDeltaMovement(motion.add(0.0D, -0.02D, 0.0D).scale(0.985D));
        }
    }

    private boolean canHitEntity(Entity entity) {
        return entity instanceof LivingEntity living
                && living.isAlive()
                && !living.isSpectator()
                && !living.getUUID().equals(this.ownerUUID)
                && !this.hitEntities.contains(entity.getId());
    }

    private void onHitEntity(EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity target)) {
            return;
        }
        this.hitEntities.add(target.getId());

        BedCrossbowBoltItem.BoltKind kind = this.getKind();
        float falloff = 1.0F - 0.5F * Mth.clamp((float) (this.traveled / this.getEffectiveRange()), 0.0F, 1.0F);
        float damage = kind.getDamage() * falloff * (0.5F + 0.5F * this.flightPower);

        HolderLookup.RegistryLookup<Enchantment> enchants =
                this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        int sharpness = EnchantmentHelper.getTagEnchantmentLevel(enchants.getOrThrow(Enchantments.SHARPNESS), this.boltStack);
        if (sharpness > 0) {
            damage += 0.5F * sharpness + 0.5F;
        }
        int power = EnchantmentHelper.getTagEnchantmentLevel(enchants.getOrThrow(Enchantments.POWER), this.boltStack);
        if (power > 0) {
            damage += 1.25F * power;
        }

        DamageSource source = this.damageSources().thrown(this, this.getOwnerEntity());
        target.hurt(source, damage);

        int fireAspect = EnchantmentHelper.getTagEnchantmentLevel(enchants.getOrThrow(Enchantments.FIRE_ASPECT), this.boltStack);
        if (fireAspect > 0) {
            target.igniteForSeconds(4 * fireAspect);
        }
        int flame = EnchantmentHelper.getTagEnchantmentLevel(enchants.getOrThrow(Enchantments.FLAME), this.boltStack);
        if (flame > 0) {
            target.igniteForSeconds(100);
        }

        target.setDeltaMovement(target.getDeltaMovement().add(this.getDeltaMovement().scale(0.06D)));
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.0F);
    }

    private void onHitBlock(BlockHitResult result) {
        this.setDeltaMovement(Vec3.ZERO);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.stuckSupportPos = result.getBlockPos();

        if (this.getKind() == BedCrossbowBoltItem.BoltKind.SPEAR_THREE_SWORD) {
            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.0F);
            this.discard();
            return;
        }

        if (this.getKind() == BedCrossbowBoltItem.BoltKind.TREAD_PEG
                && result.getDirection() == Direction.UP) {
            Vec3 center = makePegCenter(result);
            this.setState(STATE_PEG);
            this.setPos(center.x, center.y, center.z);
            this.setBoundingBox(this.makeBoundingBox());
            this.noPhysics = false;
        } else {
            Direction direction = result.getDirection();
            this.setPos(result.getLocation().x + direction.getStepX() * 0.05D,
                    result.getLocation().y + direction.getStepY() * 0.05D,
                    result.getLocation().z + direction.getStepZ() * 0.05D);
            this.frozenBox = this.getBoundingBox();
            this.setState(STATE_STUCK);
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoGravity(true);
            this.noPhysics = true;
            this.setBoundingBox(this.frozenBox);
        }

        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.0F);
    }

    private static Vec3 makePegCenter(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        return new Vec3(pos.getX() + 0.5D, pos.getY() + 1.0625D, pos.getZ() + 0.5D);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (this.level().isClientSide) {
            return this.isPickable() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        if (this.getState() == STATE_STUCK || this.getState() == STATE_PEG) {
            this.spawnAtLocation(this.boltStack.copy());
            this.playSound(SoundEvents.ITEM_PICKUP, 0.8F, 1.0F);
            this.discard();
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return this.getState() == STATE_STUCK || this.getState() == STATE_PEG;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.getState() == STATE_STUCK || this.getState() == STATE_PEG;
    }

    @Override
    protected AABB makeBoundingBox() {
        if (this.getState() == STATE_STUCK && this.frozenBox != null) {
            return this.frozenBox;
        }
        if (this.getState() == STATE_PEG) {
            return new AABB(
                    this.getX() - 0.5D, this.getY() - 0.0625D, this.getZ() - 0.5D,
                    this.getX() + 0.5D, this.getY() + 0.0625D, this.getZ() + 0.5D);
        }

        float yaw = (float) Math.toRadians(this.getYRot());
        float pitch = (float) Math.toRadians(this.getXRot());
        Vec3 motion = this.getDeltaMovement();
        Vec3 forward = motion.lengthSqr() > 1.0E-6D
                ? motion.normalize()
                : new Vec3(
                        -Math.sin(yaw) * Math.cos(pitch),
                        -Math.sin(pitch),
                        Math.cos(yaw) * Math.cos(pitch)).normalize();
        Vec3 right = new Vec3(Math.cos(yaw), 0.0D, Math.sin(yaw)).normalize();
        Vec3 up = forward.cross(right).normalize();
        Vec3 center = this.position().subtract(forward.scale(HALF_LENGTH));

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sy = -1; sy <= 1; sy += 2) {
                for (int sz = -1; sz <= 1; sz += 2) {
                    Vec3 corner = center
                            .add(right.scale(sx * HALF_WIDTH))
                            .add(up.scale(sy * HALF_HEIGHT))
                            .add(forward.scale(sz * HALF_LENGTH));
                    minX = Math.min(minX, corner.x);
                    minY = Math.min(minY, corner.y);
                    minZ = Math.min(minZ, corner.z);
                    maxX = Math.max(maxX, corner.x);
                    maxY = Math.max(maxY, corner.y);
                    maxZ = Math.max(maxZ, corner.z);
                }
            }
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        if (this.getState() == STATE_PEG) {
            return this.makeBoundingBox().inflate(0.5D);
        }
        return this.makeBoundingBox();
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

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Bolt")) {
            this.boltStack = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Bolt"));
        }
        this.entityData.set(DATA_TYPE, tag.getByte("Type"));
        this.setState(tag.getByte("State"));
        this.traveled = tag.getDouble("Traveled");
        this.flightPower = tag.contains("Power") ? tag.getFloat("Power") : 1.0F;
        this.life = tag.getInt("Life");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        if (tag.hasUUID("Parent")) {
            this.parentUUID = tag.getUUID("Parent");
        }
        if (tag.contains("SupportPos")) {
            this.stuckSupportPos = BlockPos.of(tag.getLong("SupportPos"));
        }
        if (this.getState() != STATE_FLYING) {
            this.setNoGravity(true);
        }
        this.noPhysics = this.getState() != STATE_PEG;
        this.setBoundingBox(this.makeBoundingBox());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!this.boltStack.isEmpty()) {
            tag.put("Bolt", this.boltStack.save(this.level().registryAccess(), new CompoundTag()));
        }
        tag.putByte("Type", this.entityData.get(DATA_TYPE));
        tag.putByte("State", this.getState());
        tag.putDouble("Traveled", this.traveled);
        tag.putFloat("Power", this.flightPower);
        tag.putInt("Life", this.life);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        if (this.parentUUID != null) {
            tag.putUUID("Parent", this.parentUUID);
        }
        if (this.stuckSupportPos != null) {
            tag.putLong("SupportPos", this.stuckSupportPos.asLong());
        }
    }
}
