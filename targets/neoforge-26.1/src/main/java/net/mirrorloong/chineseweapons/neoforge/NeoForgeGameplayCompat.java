package net.mirrorloong.chineseweapons.neoforge;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

import java.util.UUID;
import java.util.function.Predicate;

/** NeoForge 26.1 implementation of the shared gameplay bridge. */
public final class NeoForgeGameplayCompat implements GameplayCompat {

    public static final Predicate<ItemStack> ARMOR_GAIN = stack ->
            WeaponScriptSettings.getLogicChance(stack.getItem(), WeaponScriptSettings.Logic.ARMOR_GAIN, true);

    @Override
    public Level level(Entity entity) {
        return entity.level();
    }

    @Override
    public boolean isClientSide(Level level) {
        return level.isClientSide();
    }

    @Override
    public float nextFloat(Level level) {
        return level.getRandom().nextFloat();
    }

    @Override
    public double nextDouble(Level level) {
        return level.getRandom().nextDouble();
    }

    @Override
    public ItemStack armorPiece(LivingEntity entity, ArmorSlot selectedSlot) {
        return entity.getItemBySlot(slotOf(selectedSlot));
    }

    private static EquipmentSlot slotOf(ArmorSlot slot) {
        return switch (slot) {
            case HEAD -> EquipmentSlot.HEAD;
            case CHEST -> EquipmentSlot.CHEST;
            case LEGS -> EquipmentSlot.LEGS;
            case FEET -> EquipmentSlot.FEET;
        };
    }

    @Override
    public String itemPath(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        return id == null ? "" : id.getPath();
    }

    @Override
    public boolean isSword(ItemStack stack) {
        return stack.is(ItemTags.SWORDS) || stack.getItem() instanceof TridentItem;
    }

    @Override
    public double attributeValue(LivingEntity entity, AttributeKind type) {
        return entity.getAttributeValue(attributeOf(type));
    }

    private static Holder<Attribute> attributeOf(AttributeKind type) {
        return switch (type) {
            case ATTACK_DAMAGE -> Attributes.ATTACK_DAMAGE;
            case MOVEMENT_SPEED -> Attributes.MOVEMENT_SPEED;
            case ARMOR -> Attributes.ARMOR;
        };
    }

    @Override
    public double itemAttackDamage(ItemStack stack) {
        double[] total = {0.0D};
        stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            if (attribute.value() == Attributes.ATTACK_DAMAGE.value()) {
                total[0] += modifier.amount();
            }
        });
        return total[0];
    }

    @Override
    public void addModifier(LivingEntity entity, AttributeKind type, String key, UUID legacyId,
                            double amount, ModifierOperation operation) {
        AttributeInstance instance = entity.getAttribute(attributeOf(type));
        Identifier id = Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, key);
        if (instance == null || instance.getModifier(id) != null) {
            return;
        }
        instance.addTransientModifier(new AttributeModifier(id, amount, operationOf(operation)));
    }

    @Override
    public void removeModifier(LivingEntity entity, AttributeKind type, String key, UUID legacyId) {
        AttributeInstance instance = entity.getAttribute(attributeOf(type));
        if (instance != null) {
            instance.removeModifier(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, key));
        }
    }

    private static AttributeModifier.Operation operationOf(ModifierOperation operation) {
        return switch (operation) {
            case ADD -> AttributeModifier.Operation.ADD_VALUE;
            case MULTIPLY_TOTAL -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
        };
    }

    @Override
    public void applyEffect(LivingEntity entity, Effect type, int duration, int amplifier,
                            boolean ambient, boolean visible) {
        entity.addEffect(new MobEffectInstance(effectOf(type), duration, amplifier, ambient, visible));
    }

    private static Holder<MobEffect> effectOf(Effect type) {
        return switch (type) {
            case SLOWNESS -> MobEffects.SLOWNESS;
            case STRENGTH -> MobEffects.STRENGTH;
            case ABSORPTION -> MobEffects.ABSORPTION;
            case SPEED -> MobEffects.SPEED;
            case BLINDNESS -> MobEffects.BLINDNESS;
        };
    }

    @Override
    public void applyEffect(LivingEntity entity, RegistryEntry<MobEffect> type, int duration, int amplifier,
                            boolean ambient, boolean visible) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(type.get());
        entity.addEffect(new MobEffectInstance(holder, duration, amplifier, ambient, visible));
    }

    @Override
    public boolean hasEffect(LivingEntity entity, RegistryEntry<MobEffect> type) {
        return entity.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(type.get()));
    }

    @Override
    public int enchantmentLevel(Level level, ItemStack stack, Enchantment enchantment) {
        net.minecraft.resources.ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key =
                switch (enchantment) {
                    case RIPTIDE -> net.minecraft.world.item.enchantment.Enchantments.RIPTIDE;
                    case LOYALTY -> net.minecraft.world.item.enchantment.Enchantments.LOYALTY;
                    case CHANNELING -> net.minecraft.world.item.enchantment.Enchantments.CHANNELING;
                    case PIERCING -> net.minecraft.world.item.enchantment.Enchantments.PIERCING;
                    case UNBREAKING -> net.minecraft.world.item.enchantment.Enchantments.UNBREAKING;
                };
        return net.mirrorloong.chineseweapons.item.EnchantmentLevels.level(level, stack, key);
    }

    @Override
    public BlockPos blockPos(Vec3 position) {
        return BlockPos.containing(position);
    }

    @Override
    public boolean isOnCooldown(Player player, ItemStack stack) {
        return player.getCooldowns().isOnCooldown(stack);
    }

    @Override
    public float cooldownPercent(Player player, ItemStack stack, float partialTick) {
        return player.getCooldowns().getCooldownPercent(stack, partialTick);
    }

    @Override
    public void addCooldown(Player player, ItemStack stack, int ticks) {
        player.getCooldowns().addCooldown(stack, ticks);
    }

    @Override
    public void damageItem(ItemStack stack, int amount, LivingEntity holder) {
        stack.hurtAndBreak(amount, holder, EquipmentSlot.MAINHAND);
    }

    @Override
    public void damageItemInUsedHand(ItemStack stack, int amount, LivingEntity holder) {
        stack.hurtAndBreak(amount, holder, holder.getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }

    @Override
    public void damageItemRandom(ItemStack stack, int amount, LivingEntity holder) {
        stack.hurtAndBreak(amount, holder, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean hurt(LivingEntity target, Player attacker, float amount) {
        return target.hurtOrSimulate(attacker != null
                ? target.damageSources().playerAttack(attacker)
                : target.damageSources().generic(), amount);
    }

    @Override
    public boolean hurtMob(LivingEntity target, LivingEntity attacker, float amount) {
        return target.hurtOrSimulate(target.damageSources().mobAttack(attacker), amount);
    }

    @Override
    public boolean hurtThorns(LivingEntity target, LivingEntity defender, float amount) {
        return target.hurtOrSimulate(target.damageSources().thorns(defender), amount);
    }

    @Override
    public void playSound(Level level, double x, double y, double z, Sound sound, float volume, float pitch) {
        SoundEvent event;
        SoundSource source;
        switch (sound) {
            case RAID_HORN -> {
                event = SoundEvents.RAID_HORN.value();
                source = SoundSource.PLAYERS;
            }
            case GENERIC_SPLASH -> {
                event = SoundEvents.GENERIC_SPLASH;
                source = SoundSource.BLOCKS;
            }
            case TRIDENT_THROW -> {
                event = SoundEvents.TRIDENT_THROW.value();
                source = SoundSource.PLAYERS;
            }
            case TRIDENT_RIPTIDE_1 -> {
                event = SoundEvents.TRIDENT_RIPTIDE_1.value();
                source = SoundSource.PLAYERS;
            }
            case TRIDENT_RIPTIDE_2 -> {
                event = SoundEvents.TRIDENT_RIPTIDE_2.value();
                source = SoundSource.PLAYERS;
            }
            default -> {
                event = SoundEvents.TRIDENT_RIPTIDE_3.value();
                source = SoundSource.PLAYERS;
            }
        }
        level.playSound(null, x, y, z, event, source, volume, pitch);
    }

    @Override
    public void sendOverlayMessage(Player player, Component message) {
        player.sendOverlayMessage(message);
    }

    @Override
    public MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }

    @Override
    public boolean isOnGround(Entity entity) {
        return entity.onGround();
    }

    @Override
    public void grantAdvancement(ServerPlayer player, String path) {
        if (player.level().getServer() == null) {
            return;
        }
        AdvancementHolder advancement = player.level().getServer().getAdvancements()
                .get(Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, path));
        if (advancement == null) {
            return;
        }
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) {
            return;
        }
        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    @Override
    public int selectedSlot(Player player) {
        return player.getInventory().getSelectedSlot();
    }

    @Override
    public void clearAggro(Mob mob) {
        mob.setTarget(null);
        mob.setLastHurtByMob(null);
        mob.setLastHurtByPlayer((Player) null, 0);
    }

    @Override
    public Entity collisionMount(Player player) {
        return player.getVehicle() instanceof AbstractHorse horse ? horse : player;
    }

    @Override
    public boolean isTethered(LivingEntity entity) {
        return net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents.isTethered(entity);
    }

    @Override
    public boolean tryStartMountedDrag(Player owner, LivingEntity target) {
        return net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents
                .tryStartMountedDrag(owner, target);
    }

    @Override
    public Entity createEntity(EntityType<?> type, Level level) {
        return type.create(level, EntitySpawnReason.MOB_SUMMONED);
    }

    @Override
    public void moveEntity(Entity entity, double x, double y, double z, float yaw, float pitch) {
        entity.snapTo(x, y, z, yaw, pitch);
    }

    @Override
    public float daggerAxeHookChance(ItemStack stack) {
        return WeaponScriptSettings.getChance(stack.getItem(), WeaponScriptSettings.Skill.HOOK_DISMOUNT, 0.30F);
    }

    @Override
    public boolean armorGain(ItemStack stack) {
        return WeaponScriptSettings.getLogicChance(stack.getItem(), WeaponScriptSettings.Logic.ARMOR_GAIN, true);
    }
}
