package net.mirrorloong.chineseweapons.entity;

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
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.ExplosionEvent;
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

    private ItemStack storedItem = ItemStack.EMPTY;

    public SongStandingShieldEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HP, 1f);
        builder.define(DATA_YAW, 0f);
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack.copy();
    }

    public ItemStack getStoredItem() {
        return this.storedItem.copy();
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

    public float getHealth() { return this.entityData.get(DATA_HP); }

    boolean setHealthLogic = WeaponScriptSettings.getLogicChance(
            null,
            WeaponScriptSettings.Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY, true);

    public AABB getSafeZone() {
        return ShieldRules.safeZone(this.position(), this.getShieldYaw(), behindCenter());
    }

    public void setHealth(float hp) {
        this.entityData.set(DATA_HP, hp);
        if (setHealthLogic) {
            this.setCustomName(ShieldRules.healthName(hp));
        }
    }


    public float getShieldYaw() { return this.entityData.get(DATA_YAW); }

    public void setShieldYaw(float yaw) {
        this.entityData.set(DATA_YAW, yaw);
        this.setYRot(yaw);
        this.yRotO = yaw;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack held = player.getItemInHand(hand);

        // 蹲下 + 右键：执行旋转
        if (player.isShiftKeyDown()) {
            double ANGLE = WeaponScriptSettings.getArmorThreshold(
                    null,
                    WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_ANGLE, 15f);

            float newYaw = ShieldRules.rotatedYaw(this.position(), this.getShieldYaw(),
                    player.position(), ANGLE);
            this.setShieldYaw(newYaw);

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.WOODEN_BUTTON_CLICK_ON, this.getSoundSource(), 1.3F, 0.8F);

            return InteractionResult.CONSUME;
        }

        if (held.is(ItemTags.PLANKS)) {
            ItemStack recovered = getStoredItem();
            float hp = this.getHealth();

            float newHp = ShieldRules.repairedHealth(recovered, hp);
            if (newHp < 0) {
                return InteractionResult.PASS;
            }
            if (newHp == hp) {
                return InteractionResult.CONSUME;
            }

            this.setHealth(newHp);

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.WOODEN_BUTTON_CLICK_ON, this.getSoundSource(), 1.3F, 0.8F);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel serverLevel, DamageSource source, float amount) {
        if (this.level().isClientSide()) return false;
        if (source.is(DamageTypeTags.IS_FIRE)) return false;
        if (source.is(DamageTypeTags.IS_EXPLOSION)) return false;
        if (source.getDirectEntity() instanceof AbstractThrownPotion) return false;
        if (source.getDirectEntity() instanceof AreaEffectCloud) return false;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SHIELD_BLOCK, this.getSoundSource(), 1.0F, 1.0F);

        int unbreaking = 0;
        ItemStack stored = this.getStoredItem();
        if (!stored.isEmpty()) {
            unbreaking = ChineseWeapons.gameplay()
                    .enchantmentLevel(this.level(), stored, GameplayCompat.Enchantment.UNBREAKING);
        }

        float newHp = ShieldRules.damagedHealth(this.getHealth(), amount, unbreaking);
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
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
    }


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
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

        this.setCustomNameVisible(true);
    }

    public void handleExplosion(ExplosionEvent.Detonate event) {
        if (this.level().isClientSide()) return;

        Vec3 shieldPos = this.position();
        Vec3 expCenter = event.getExplosion().center();
        if (!ShieldRules.isInFront(shieldPos, this.getShieldYaw(), expCenter)) {
            return;
        }

        AABB safeZone = ShieldRules.explosionSafeZone(shieldPos, this.getShieldYaw(), behindCenter());
        event.getAffectedBlocks().removeIf(pos -> safeZone.contains(Vec3.atCenterOf(pos)));
        event.getAffectedEntities().removeIf(ent -> safeZone.contains(ent.position()));
    }

    private static double behindCenter() {
        return WeaponScriptSettings.getArmorThreshold(
                null,
                WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, 4.0);
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        this.storedItem = input.read("StoredItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        setHealth(input.getFloatOr("HP", 1.0F));
        setShieldYaw(input.getFloatOr("Yaw", 0.0F));
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        output.store("StoredItem", ItemStack.CODEC, this.storedItem);
        output.putFloat("HP", getHealth());
        output.putFloat("Yaw", getShieldYaw());
    }
}

