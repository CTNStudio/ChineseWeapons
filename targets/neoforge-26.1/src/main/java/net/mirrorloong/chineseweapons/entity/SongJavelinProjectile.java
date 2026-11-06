package net.mirrorloong.chineseweapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.item.EnchantmentLevels;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongJavelinProjectile extends AbstractArrow {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongJavelinProjectile.class);

    private static final EntityDataAccessor<String> ITEM_REGISTRY_NAME =
            SynchedEntityData.defineId(SongJavelinProjectile.class, EntityDataSerializers.STRING);

    private ItemStack weaponStack = ItemStack.EMPTY;
    private int loyaltyLevel = 0;
    private boolean hasChanneling = false;
    private boolean isReturning = false;
    private boolean returnSoundPlayed = false;
    private boolean hovering = false;
    private int customKnockback = 0;
    private float baseDamage = 0.0F;

    public SongJavelinProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.pickup = Pickup.ALLOWED;
        this.getEntityData().set(ITEM_REGISTRY_NAME, "");
    }

    public SongJavelinProjectile(Level level, LivingEntity shooter, ItemStack weaponStack) {
        super(ChineseweaponsModEntities.type(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.weaponStack = weaponStack.copy();
        String registryName = BuiltInRegistries.ITEM.getKey(weaponStack.getItem()).getPath();
        this.getEntityData().set(ITEM_REGISTRY_NAME, registryName);
        this.pickup = Pickup.ALLOWED;

        this.loyaltyLevel = EnchantmentLevels.level(level, weaponStack, Enchantments.LOYALTY);
        this.hasChanneling = EnchantmentLevels.level(level, weaponStack, Enchantments.CHANNELING) > 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ITEM_REGISTRY_NAME, "");
    }

    @Override
    public void setBaseDamage(double baseDamage) {
        super.setBaseDamage(baseDamage);
        this.baseDamage = (float) baseDamage;
    }

    public void setLoyaltyLevel(int level) { this.loyaltyLevel = level; }
    public void setKnockback(int knockback) { this.customKnockback = knockback; }
    public void setHasChanneling(boolean has) { this.hasChanneling = has; }

    public Identifier getTextureLocation() {
        String registryName = this.getEntityData().get(ITEM_REGISTRY_NAME);
        if (!registryName.isEmpty()) {
            return Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/" + registryName + ".png");
        }
        if (!weaponStack.isEmpty()) {
            Identifier key = BuiltInRegistries.ITEM.getKey(weaponStack.getItem());
            return Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/" + key.getPath() + ".png");
        }
        LOGGER.warn("SongJavelinProjectile: error. Entity ID: {}, position: {}",
                this.getId(), this.position());
        return Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "textures/entity/diamond_song_javelin.png");
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("Weapon", ItemStack.CODEC).ifPresent(stack -> {
            this.weaponStack = stack;
            if (!stack.isEmpty()) {
                this.getEntityData().set(ITEM_REGISTRY_NAME,
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
            }
        });
        String name = input.getStringOr("ItemName", "");
        if (!name.isEmpty()) {
            this.getEntityData().set(ITEM_REGISTRY_NAME, name);
        }
        this.loyaltyLevel = input.getIntOr("Loyalty", 0);
        this.hasChanneling = input.getBooleanOr("Channeling", false);
        this.isReturning = input.getBooleanOr("Returning", false);
        this.returnSoundPlayed = input.getBooleanOr("ReturnSoundPlayed", false);
        this.hovering = input.getBooleanOr("Hovering", false);
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (!this.weaponStack.isEmpty()) {
            output.store("Weapon", ItemStack.CODEC, this.weaponStack);
        }
        String registryName = this.getEntityData().get(ITEM_REGISTRY_NAME);
        if (!registryName.isEmpty()) {
            output.putString("ItemName", registryName);
        }
        output.putInt("Loyalty", this.loyaltyLevel);
        output.putBoolean("Channeling", this.hasChanneling);
        output.putBoolean("Returning", this.isReturning);
        output.putBoolean("ReturnSoundPlayed", this.returnSoundPlayed);
        output.putBoolean("Hovering", this.hovering);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level().isClientSide()) return;

        Entity hit = result.getEntity();

        //Not LivingEntity can Fiyyyyyyy
        if (!(hit instanceof LivingEntity)) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.lengthSqr() > 1.0E-6) {
                Vec3 hitDir = new Vec3(
                        hit.getX() - this.getX(),
                        hit.getY() - this.getY(),
                        hit.getZ() - this.getZ()
                );

                if (hitDir.lengthSqr() < 1.0E-6) {
                    this.setDeltaMovement(motion.scale(-0.4));
                } else {
                    Vec3 normal = hitDir.normalize();
                    double dot = motion.dot(normal);
                    Vec3 reflected = motion.subtract(normal.scale(2.0 * dot));
                    this.setDeltaMovement(reflected.scale(0.4));
                }
            }

            this.playSound(SoundEvents.ARROW_HIT, 0.8F, 1.0F);

            float damage = this.baseDamage;
            hit.hurt(this.damageSources().thrown(this, this.getOwner()), damage + 2);

            if (this.hasChanneling && this.level().isThundering()) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
                bolt.setPos(hit.getX(), hit.getY(), hit.getZ());
                this.level().addFreshEntity(bolt);
            }
        } else {
            LivingEntity target = (LivingEntity) hit;
            float damage = this.baseDamage;
            target.hurt(this.damageSources().thrown(this, this.getOwner()), damage + 2);

            if (this.customKnockback > 0) {
                Vec3 knockback = this.getDeltaMovement().scale(this.customKnockback * 0.6);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            }

            this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);

            if (this.hasChanneling && this.level().isThundering()) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
                bolt.setPos(target.getX(), target.getY(), target.getZ());
                this.level().addFreshEntity(bolt);
            }
        }

        if (this.loyaltyLevel <= 0) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoGravity(false);
        } else {
            startReturning();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.TRIDENT_HIT_GROUND, 1.0F, 1.0F);

            if (this.hasChanneling) {
                BlockPos blockPos = result.getBlockPos();
                BlockState blockState = this.level().getBlockState(blockPos);
                if (blockState.is(Blocks.LIGHTNING_ROD) && this.level().canSeeSky(blockPos)) {
                    if (this.level().isThundering() || this.level().isRaining()) {
                        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
                        bolt.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                        this.level().addFreshEntity(bolt);
                    }
                }
            }

            if (this.loyaltyLevel > 0) {
                startReturning();
            }
        }
    }

    private void startReturning() {
        this.isReturning = true;
        this.hovering = false;
        this.returnSoundPlayed = false;
        this.setInGround(false);
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        if (this.isReturning && this.getOwner() instanceof LivingEntity owner) {
            if (!this.returnSoundPlayed) {
                this.playSound(SoundEvents.TRIDENT_RETURN, 1.0F, 1.0F);
                this.returnSoundPlayed = true;
            }

            if (!this.hovering) {
                Vec3 targetPos = owner.getEyePosition();
                Vec3 diff = targetPos.subtract(this.position());
                double distance = diff.length();

                if (distance > 0.5) {
                    double speed = 0.5 + this.loyaltyLevel * 0.15;
                    this.setDeltaMovement(diff.normalize().scale(speed));
                    this.setNoGravity(true);
                    this.setNoPhysics(true);
                } else {
                    if (owner instanceof Player player) {
                        if (player.getInventory().add(this.weaponStack)) {
                            this.discard();
                            return;
                        } else {
                            this.hovering = true;
                        }
                    } else {
                        this.spawnAtLocation((net.minecraft.server.level.ServerLevel) this.level(), this.weaponStack);
                        this.discard();
                        return;
                    }
                }
            }

            if (this.hovering) {
                if (owner instanceof Player player) {
                    if (player.getInventory().add(this.weaponStack)) {
                        this.discard();
                        return;
                    }

                    Vec3 hoverPos = player.position().add(0, 2.0, 0);
                    this.setPos(hoverPos.x, hoverPos.y, hoverPos.z);
                    this.setDeltaMovement(Vec3.ZERO);
                    this.setNoGravity(true);
                    this.setNoPhysics(true);
                } else {
                    this.spawnAtLocation((net.minecraft.server.level.ServerLevel) this.level(), this.weaponStack);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void tickDespawn() {
        if (this.isInGround() && !this.isReturning) {
            return;
        }
        super.tickDespawn();
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.weaponStack == null ? ItemStack.EMPTY : this.weaponStack.copy();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return this.weaponStack == null ? ItemStack.EMPTY : this.weaponStack.copy();
    }
}