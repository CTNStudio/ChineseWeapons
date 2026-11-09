package net.mirrorloong.chineseweapons.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.rules.ShieldRules;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.StandingShieldEntity;

public class SongStandingShieldEntity extends Entity implements StandingShieldEntity {

    private static final EntityDataAccessor<Float> DATA_HP =
            SynchedEntityData.defineId(SongStandingShieldEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_YAW =
            SynchedEntityData.defineId(SongStandingShieldEntity.class, EntityDataSerializers.FLOAT);

    private CompoundTag storedItemNbt = new CompoundTag();

    public SongStandingShieldEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HP, 1f);
        this.entityData.define(DATA_YAW, 0f);
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItemNbt = stack.save(new CompoundTag());
    }

    public ItemStack getStoredItem() {
        return ItemStack.of(storedItemNbt);
    }

    @Override
    public ItemStack storedItem() {
        return getStoredItem();
    }

    @Override
    public float shieldHealth() {
        return getHealth();
    }

    @Override
    public void storeShield(ItemStack stack, float health, float yaw) {
        setShieldYaw(yaw);
        setStoredItem(stack);
        setHealth(health);
    }

    public float getHealth() {
        return this.entityData.get(DATA_HP);
    }

    boolean setHealthLogic = WeaponScriptSettings.getLogicChance(
            null,
            WeaponScriptSettings.Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY, true);

    public AABB getSafeZone() {
        double behindCenter = behindCenter();
        return ShieldRules.safeZone(this.position(), this.getShieldYaw(), behindCenter);
    }

    public AABB getExplosionSafeZone() {
        double behindCenter = behindCenter();
        return ShieldRules.explosionSafeZone(this.position(), this.getShieldYaw(), behindCenter);
    }

    public void setHealth(float health) {
        this.entityData.set(DATA_HP, health);
        if (setHealthLogic) {
            this.setCustomName(ShieldRules.healthName(health));
        }
    }

    public float getShieldYaw() {
        return this.entityData.get(DATA_YAW);
    }

    public void setShieldYaw(float yaw) {
        this.entityData.set(DATA_YAW, yaw);
        this.setYRot(yaw);
        this.yRotO = yaw;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (this.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack held = player.getItemInHand(hand);

        // 蹲下 + 右键：执行旋转
        if (player.isShiftKeyDown()) {
            double angle = WeaponScriptSettings.getArmorThreshold(
                    null,
                    WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_ANGLE, 15f);

            float newYaw = ShieldRules.rotatedYaw(this.position(), this.getShieldYaw(),
                    player.position(), angle);
            this.setShieldYaw(newYaw);

            this.playShieldSound(SoundEvents.WOODEN_BUTTON_CLICK_ON, 1.3F, 0.8F);

            return InteractionResult.CONSUME;
        }

        if (held.is(ItemTags.PLANKS)) {
            ItemStack recovered = getStoredItem();
            float health = this.getHealth();

            float newHealth = ShieldRules.repairedHealth(recovered, health);
            if (newHealth < 0) {
                return InteractionResult.PASS;
            }
            if (newHealth == health) {
                return InteractionResult.CONSUME;
            }

            this.setHealth(newHealth);

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }

            this.playShieldSound(SoundEvents.WOODEN_BUTTON_CLICK_ON, 1.3F, 0.8F);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        if (source.getDirectEntity() instanceof ThrownPotion) {
            return false;
        }
        if (source.getDirectEntity() instanceof AreaEffectCloud) {
            return false;
        }

        this.playShieldSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);

        ItemStack stored = this.getStoredItem();
        int unbreaking = stored.isEmpty() ? 0 : ChineseWeapons.gameplay()
                .enchantmentLevel(this.level(), stored, GameplayCompat.Enchantment.UNBREAKING);

        float newHealth = ShieldRules.damagedHealth(this.getHealth(), amount, unbreaking);
        if (newHealth <= 0) {
            this.playShieldSound(SoundEvents.SHIELD_BREAK, 1.0F, 1.0F);
            this.discard();
        } else {
            this.setHealth(newHealth);
        }
        return true;
    }

    private void playShieldSound(net.minecraft.sounds.SoundEvent sound, float volume, float pitch) {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                sound, this.getSoundSource(), volume, pitch);
    }

    private static double behindCenter() {
        return WeaponScriptSettings.getArmorThreshold(
                null,
                WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, 4.0);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void push(Entity e) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        // 只保留 Y 方向速度
        double vy = this.getDeltaMovement().y;
        vy = (vy - 0.08) * 0.98;
        if (vy < -1.0) {
            vy = -1.0;
        }
        this.setDeltaMovement(0, vy, 0);

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPos(this.getX(), this.getY(), this.getZ());
            this.setYRot(this.getShieldYaw());
            this.yRotO = this.getShieldYaw();
        }

        this.setCustomNameVisible(this.level().players().stream()

                .anyMatch(player -> this.getSafeZone().contains(player.position())));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("StoredItem")) {
            storedItemNbt = tag.getCompound("StoredItem");
        }
        setHealth(tag.getFloat("HP"));
        setShieldYaw(tag.getFloat("Yaw"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("StoredItem", storedItemNbt);
        tag.putFloat("HP", getHealth());
        tag.putFloat("Yaw", getShieldYaw());
    }
}
