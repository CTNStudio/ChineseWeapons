package net.mirrorloong.chineseweapons.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public class SongStandingShieldEntity extends Entity {

    private static final EntityDataAccessor<Float> DATA_HP =
            SynchedEntityData.defineId(SongStandingShieldEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_YAW =
            SynchedEntityData.defineId(SongStandingShieldEntity.class, EntityDataSerializers.FLOAT);

    private CompoundTag storedItemNbt = new CompoundTag();

    private int rotateTicks = 0;
    private float rotateDelta = 0;
    private float rotateStart = 0;

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

    public float getHealth() { return this.entityData.get(DATA_HP); }

    boolean setHealthLogic = WeaponScriptSettings.getLogicChance(
            null,
            WeaponScriptSettings.Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY, true);

    public void setHealth(float hp) {
        if (setHealthLogic) {
            this.entityData.set(DATA_HP, hp);

            MutableComponent name = Component.translatable("entity.standing_shield.hp", (int) hp);
            if (hp <= 30f) {
                name = name.withStyle(ChatFormatting.RED);
            }

            this.setCustomName(name);
            this.setCustomNameVisible(true);
        }
    }

    public float getShieldYaw() { return this.entityData.get(DATA_YAW); }

    public void setShieldYaw(float yaw) {
        this.entityData.set(DATA_YAW, yaw);
        this.setYRot(yaw);
        this.yRotO = yaw;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (this.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // 实体位置朝向
        Vec3 selfPos = this.position();
        float yaw = this.getShieldYaw();

        // 实体正前方单位向量
        double fx = -Math.sin(Math.toRadians(yaw));
        double fz =  Math.cos(Math.toRadians(yaw));

        // 实体右侧单位向量
        double rx = -fz;
        double rz =  fx;

        // 相对实体的向量
        Vec3 toPlayer = player.position().subtract(selfPos);
        double dot = toPlayer.x * rx + toPlayer.z * rz;


        double ANGLE = WeaponScriptSettings.getArmorThreshold(
                null,
                WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_ANGLE, 15f);

        float newYaw = (float) (yaw + (dot > 0 ? -ANGLE : ANGLE));

        this.setShieldYaw(newYaw);

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.WOODEN_BUTTON_CLICK_ON, this.getSoundSource(), 1.3F, 0.8F);

        return InteractionResult.CONSUME;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) return false;
        if (source.is(DamageTypeTags.IS_FIRE)) return false;
        if (source.is(DamageTypeTags.IS_EXPLOSION)) return false;
        if (source.getDirectEntity() instanceof ThrownPotion) return false;
        if (source.getDirectEntity() instanceof AreaEffectCloud) return false;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SHIELD_BLOCK, this.getSoundSource(), 1.0F, 1.0F);

        int unbreaking = 0;
        ItemStack stored = this.getStoredItem();
        if (!stored.isEmpty()) {
            unbreaking = EnchantmentHelper.getTagEnchantmentLevel(
                    Enchantments.UNBREAKING, stored);
        }

        float actualDamage = amount / (unbreaking + 1);

        float newHp = this.getHealth() - actualDamage;
        if (newHp <= 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHIELD_BREAK, this.getSoundSource(), 1.0F, 1.0F);
            this.discard();
        } else {
            this.setHealth(newHp);
        }
        return true;
    }

    @Override
    public boolean isPickable() { return true; }
    @Override
    public boolean isPushable() { return false; }
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
            zhuannimation();
            return;
        }

        // 只保留 Y 方向速度
        double vy = this.getDeltaMovement().y;
        vy = (vy - 0.08) * 0.98;
        if (vy < -1.0) vy = -1.0;
        this.setDeltaMovement(0, vy, 0);

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPos(this.getX(), this.getY(), this.getZ());
            this.setYRot(this.getShieldYaw());
            this.yRotO = this.getShieldYaw();
        }

        zhuannimation();
    }


    private void zhuannimation() {
        if (rotateDelta != 0) {
            if (rotateTicks % 4 == 0) {
                float progress = rotateTicks / 4f;
                if (progress > 15f) {
                    this.setShieldYaw(rotateStart + Math.signum(rotateDelta) * 15f);
                    rotateDelta = 0;
                    rotateTicks = 0;
                } else {
                    this.setShieldYaw(rotateStart + Math.signum(rotateDelta) * progress);
                }
            }
            rotateTicks++;
        }
    }

    public void handleExplosion(ExplosionEvent.Detonate event) {
        if (this.level().isClientSide) return;

        Vec3 shieldPos = this.position();
        Vec3 expCenter = event.getExplosion().getPosition();

        float yaw = this.getShieldYaw();
        double fx = -Math.sin(Math.toRadians(yaw));
        double fz =  Math.cos(Math.toRadians(yaw));
        Vec3 forward = new Vec3(fx, 0, fz).normalize();

        Vec3 toExplosion = expCenter.subtract(shieldPos);
        if (toExplosion.lengthSqr() < 1.0E-6) return;
        if (toExplosion.normalize().dot(forward) <= 0) return;

        double YBehindCenter = WeaponScriptSettings.getArmorThreshold(
                null,
                WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, 4.0);

        Vec3 behindCenter = shieldPos.subtract(forward.scale(YBehindCenter));
        AABB safeZone = new AABB(
                behindCenter.x - 4, behindCenter.y - 4, behindCenter.z - 4,
                behindCenter.x + 4, behindCenter.y + 4, behindCenter.z + 4
        );

        event.getAffectedBlocks().removeIf(pos -> safeZone.contains(Vec3.atCenterOf(pos)));
        event.getAffectedEntities().removeIf(ent -> safeZone.contains(ent.position()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("StoredItem")) storedItemNbt = tag.getCompound("StoredItem");
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