package net.mirrorloong.chineseweapons.spi;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Loader/version bridge for the small number of vanilla operations the shared
 * gameplay rules need but whose signatures changed between 1.18.2 and 26.1.
 *
 * <p>Rules must not contain reflection or runtime version checks: everything
 * that differs is expressed here and implemented once per target. Operations
 * whose API is stable across the whole matrix (for example
 * {@link ItemStack#isEmpty()}, {@link Entity#position()} or
 * {@link LivingEntity#hurt}) are called directly by the rules.</p>
 */
public interface GameplayCompat {

    enum ArmorSlot {
        HEAD,
        CHEST,
        LEGS,
        FEET
    }

    enum AttributeKind {
        ATTACK_DAMAGE,
        MOVEMENT_SPEED,
        ARMOR
    }

    enum Effect {
        SLOWNESS,
        STRENGTH,
        ABSORPTION,
        SPEED,
        BLINDNESS
    }

    enum ModifierOperation {
        ADD,
        MULTIPLY_TOTAL
    }

    enum Sound {
        RAID_HORN,
        GENERIC_SPLASH,
        TRIDENT_THROW,
        TRIDENT_RIPTIDE_1,
        TRIDENT_RIPTIDE_2,
        TRIDENT_RIPTIDE_3
    }

    /** Vanilla enchantments the shared item rules read by name. */
    enum Enchantment {
        RIPTIDE,
        LOYALTY,
        CHANNELING,
        PIERCING,
        UNBREAKING
    }

    /** {@code entity.level()} / {@code entity.level} depending on the version. */
    Level level(Entity entity);

    /** {@code level.isClientSide()} / {@code level.isClientSide} depending on the version. */
    boolean isClientSide(Level level);

    /** Uniform float in [0, 1) from the level random source. */
    float nextFloat(Level level);

    /** Uniform double in [0, 1) from the level random source. */
    double nextDouble(Level level);

    /** The four armour pieces of {@code entity}, in head-chest-legs-feet order. */
    ItemStack armorPiece(LivingEntity entity, ArmorSlot slot);

    /** Registry path of an item, or an empty string when the item is not registered. */
    String itemPath(Item item);

    /** Whether the stack counts as a sword for the armour combat rules (tag or item type). */
    boolean isSword(ItemStack stack);

    /** Current value of a vanilla attribute, or 0 when the entity has none. */
    double attributeValue(LivingEntity entity, AttributeKind attribute);

    /** Sum of the main-hand attack-damage modifiers of a stack. */
    double itemAttackDamage(ItemStack stack);

    /**
     * Adds a transient attribute modifier. {@code key} names the modifier in the
     * versions that use resource-location ids, {@code legacyId} is the UUID used
     * by the versions that still identify modifiers that way.
     */
    void addModifier(LivingEntity entity, AttributeKind attribute, String key, UUID legacyId,
                     double amount, ModifierOperation operation);

    /** Removes the modifier previously added through {@link #addModifier}. */
    void removeModifier(LivingEntity entity, AttributeKind attribute, String key, UUID legacyId);

    /** Applies one of the vanilla effects the shared rules use. */
    void applyEffect(LivingEntity entity, Effect effect, int duration, int amplifier,
                     boolean ambient, boolean visible);

    /** Applies a mod effect described by a shared registry entry. */
    void applyEffect(LivingEntity entity, RegistryEntry<MobEffect> effect, int duration, int amplifier,
                     boolean ambient, boolean visible);

    /** Whether the entity currently has the mod effect described by the entry. */
    boolean hasEffect(LivingEntity entity, RegistryEntry<MobEffect> effect);

    /** Level of one of the vanilla enchantments the shared item rules read. */
    int enchantmentLevel(Level level, ItemStack stack, Enchantment enchantment);

    /** {@code BlockPos.containing(Vec3)} / {@code new BlockPos(Vec3)} depending on the version. */
    BlockPos blockPos(Vec3 position);

    boolean isOnCooldown(Player player, ItemStack stack);

    float cooldownPercent(Player player, ItemStack stack, float partialTick);

    void addCooldown(Player player, ItemStack stack, int ticks);

    /** Standard "damage the held item" path of the target. */
    void damageItem(ItemStack stack, int amount, LivingEntity holder);

    /** Damages the item in the hand the holder is currently using. */
    void damageItemInUsedHand(ItemStack stack, int amount, LivingEntity holder);

    /**
     * The legacy durability roll some procedures used: {@code Stack#hurt} plus a
     * single-item shrink when the roll succeeds.
     */
    void damageItemRandom(ItemStack stack, int amount, LivingEntity holder);

    /** Hurts {@code target} with a player-attack source; {@code attacker} may be null. */
    boolean hurt(LivingEntity target, Player attacker, float amount);

    /** Hurts {@code target} with the attacker's mob-attack source (any living attacker). */
    boolean hurtMob(LivingEntity target, LivingEntity attacker, float amount);

    /** Hurts {@code target} with a thorns source owned by {@code defender}. */
    boolean hurtThorns(LivingEntity target, LivingEntity defender, float amount);

    /** Plays a vanilla sound at a position. */
    void playSound(Level level, double x, double y, double z, Sound sound, float volume, float pitch);

    /** Plays a mod sound described by a shared registry entry at a position. */
    default void playSound(Level level, double x, double y, double z, RegistryEntry<SoundEvent> sound,
                           float volume, float pitch) {
        level.playSound(null, x, y, z, sound.get(), net.minecraft.sounds.SoundSource.PLAYERS, volume, pitch);
    }

    /** Action-bar style message ({@code displayClientMessage} vs {@code sendOverlayMessage}). */
    void sendOverlayMessage(Player player, Component message);

    /** Translated text with formatting args ({@code Component.translatable} vs {@code new TranslatableComponent}). */
    MutableComponent translatable(String key, Object... args);

    /** Translated, colour-styled text line. */
    default Component translatableWithStyle(String key, ChatFormatting color, Object... args) {
        return translatable(key, args).withStyle(color);
    }

    /** Whether the entity is standing on the ground. */
    boolean isOnGround(Entity entity);

    /** Awards every remaining criterion of the advancement at {@code path}. */
    void grantAdvancement(ServerPlayer player, String path);

    /** Selected hotbar slot ({@code inventory.selected} vs {@code getSelectedSlot()}). */
    int selectedSlot(Player player);

    /** Drops target/hurt-by-player/fear aggro from a mob. */
    void clearAggro(Mob mob);

    /** The ridden {@code AbstractHorse}, or the player itself (sprint collisions). */
    Entity collisionMount(Player player);

    /**
     * Whether the entity is currently dragged by the mounted combat tether. The
     * storage (persistent NBT, attachment or component) is loader-specific, so
     * every target implements it on top of its own event class.
     */
    boolean isTethered(LivingEntity entity);

    /** Starts the mounted-drag tether; the target validates and stores it. */
    boolean tryStartMountedDrag(Player owner, LivingEntity target);

    /**
     * Creates an entity through the version-specific {@code EntityType} factory
     * ({@code create(Level)} before 1.21.2, {@code create(Level, EntitySpawnReason)}
     * afterwards), or {@code null} when the type refuses to spawn.
     */
    Entity createEntity(EntityType<?> type, Level level);

    /** Positions and orients an entity ({@code moveTo} before 26.1, {@code snapTo} afterwards). */
    void moveEntity(Entity entity, double x, double y, double z, float yaw, float pitch);

    /** Configured {@code HOOK_DISMOUNT} chance of the held dagger-axe (0.30 fallback). */
    float daggerAxeHookChance(ItemStack stack);

    /** Per-stack armour-gain roll used by the shared armour rules. */
    boolean armorGain(ItemStack stack);
}
