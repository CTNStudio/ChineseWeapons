package net.mirrorloong.chineseweapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongJavelinProjectile extends AbstractArrow {
    private static final Logger LOGGER = LoggerFactory.getLogger(SongJavelinProjectile.class);
    private static final EntityDataAccessor<String> ITEM_REGISTRY_NAME = SynchedEntityData.defineId(SongJavelinProjectile.class, EntityDataSerializers.STRING);
    private ItemStack weaponStack = ItemStack.EMPTY;
    private int loyaltyLevel = 0;
    private boolean hasChanneling = false;
    private boolean isReturning = false;
    private boolean returnSoundPlayed = false;
    private boolean hovering = false;

    public SongJavelinProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.pickup = Pickup.ALLOWED;
        this.getEntityData().set(ITEM_REGISTRY_NAME, "");
    }

    public SongJavelinProjectile(Level level, LivingEntity shooter, ItemStack weaponStack) {
        super(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE.get(), shooter, level, weaponStack.copy(), null);
        this.weaponStack = weaponStack.copy();
        String registryName = BuiltInRegistries.ITEM.getKey(weaponStack.getItem()).getPath();
        this.getEntityData().set(ITEM_REGISTRY_NAME, registryName);
        this.pickup = Pickup.ALLOWED;
        HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchants = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        this.loyaltyLevel = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.LOYALTY), weaponStack);
        this.hasChanneling = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.CHANNELING), weaponStack) > 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ITEM_REGISTRY_NAME, "");
    }

    public void setLoyaltyLevel(int level) {
        this.loyaltyLevel = level;
    }

    public void setHasChanneling(boolean has) {
        this.hasChanneling = has;
    }

    public ResourceLocation getTextureLocation() {
        String registryName = this.getEntityData().get(ITEM_REGISTRY_NAME);
        if (!registryName.isEmpty()) {
            return ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/" + registryName + ".png");
        }
        if (!weaponStack.isEmpty()) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(weaponStack.getItem());
            return ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/" + key.getPath() + ".png");
        }
        LOGGER.warn("SongJavelinProjectile: error. Entity ID: {}, position: {}", this.getId(), this.position());
        return ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/entity/diamond_song_javelin.png");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Weapon")) {
            this.weaponStack = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("Weapon"));
            if (!this.weaponStack.isEmpty()) {
                String name = BuiltInRegistries.ITEM.getKey(this.weaponStack.getItem()).getPath();
                this.getEntityData().set(ITEM_REGISTRY_NAME, name);
            }
        }
        if (tag.contains("ItemName")) {
            String name = tag.getString("ItemName");
            this.getEntityData().set(ITEM_REGISTRY_NAME, name);
        }
        this.loyaltyLevel = tag.getInt("Loyalty");
        this.hasChanneling = tag.getBoolean("Channeling");
        this.isReturning = tag.getBoolean("Returning");
        this.returnSoundPlayed = tag.getBoolean("ReturnSoundPlayed");
        this.hovering = tag.getBoolean("Hovering");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.weaponStack.isEmpty()) {
            tag.put("Weapon", this.weaponStack.save(this.registryAccess(), new CompoundTag()));
        }
        String registryName = this.getEntityData().get(ITEM_REGISTRY_NAME);
        if (!registryName.isEmpty()) {
            tag.putString("ItemName", registryName);
        }
        tag.putInt("Loyalty", this.loyaltyLevel);
        tag.putBoolean("Channeling", this.hasChanneling);
        tag.putBoolean("Returning", this.isReturning);
        tag.putBoolean("ReturnSoundPlayed", this.returnSoundPlayed);
        tag.putBoolean("Hovering", this.hovering);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level().isClientSide) return;
        Entity hit = result.getEntity();
        if (!(hit instanceof LivingEntity)) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.lengthSqr() > 1.0E-6) {
                Vec3 hitDir = new Vec3(hit.getX() - this.getX(), hit.getY() - this.getY(), hit.getZ() - this.getZ());
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
            float damage = (float) this.getBaseDamage();
            hit.hurt(this.damageSources().thrown(this, this.getOwner()), damage + 2);
            if (this.hasChanneling && this.level().isThundering()) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
                bolt.setPos(hit.getX(), hit.getY(), hit.getZ());
                this.level().addFreshEntity(bolt);
            }
        } else {
            LivingEntity target = (LivingEntity) hit;
            float damage = (float) this.getBaseDamage();
            target.hurt(this.damageSources().thrown(this, this.getOwner()), damage + 2);
            HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchants = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            int knockbackLevel = EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(Enchantments.KNOCKBACK), this.weaponStack);
            if (knockbackLevel > 0) {
                Vec3 knockback = this.getDeltaMovement().scale(knockbackLevel * 0.6);
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
        if (!this.level().isClientSide) {
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
        this.inGround = false;
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;
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
                    this.noPhysics = true;
                } else {
                    if (owner instanceof Player player) {
                        if (player.getInventory().add(this.weaponStack)) {
                            this.discard();
                            return;
                        } else {
                            this.hovering = true;
                        }
                    } else {
                        this.spawnAtLocation(this.weaponStack);
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
                    this.noPhysics = true;
                } else {
                    this.spawnAtLocation(this.weaponStack);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void tickDespawn() {
        if (this.inGround && !this.isReturning) {
            return;
        }
        super.tickDespawn();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        if (weaponStack == null || weaponStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return this.weaponStack.copy();
    }

    @Override
    protected ItemStack getPickupItem() {
        if (weaponStack == null || weaponStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return this.weaponStack.copy();
    }
}
