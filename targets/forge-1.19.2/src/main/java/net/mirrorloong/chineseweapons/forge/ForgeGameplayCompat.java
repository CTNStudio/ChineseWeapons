package net.mirrorloong.chineseweapons.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

import java.util.UUID;
import java.util.function.Predicate;

/** Forge 1.18.2 implementation of the shared gameplay bridge. */
public final class ForgeGameplayCompat implements GameplayCompat {

    public static final Predicate<ItemStack> ARMOR_GAIN = stack ->
            WeaponScriptSettings.getLogicChance(stack.getItem(), WeaponScriptSettings.Logic.ARMOR_GAIN, true);

    @Override
    public Level level(Entity entity) {
        return entity.level;
    }

    @Override
    public boolean isClientSide(Level level) {
        return level.isClientSide;
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
        ResourceLocation id = Registry.ITEM.getKey(item);
        return id == null ? "" : id.getPath();
    }

    @Override
    public boolean isSword(ItemStack stack) {
        return stack.is(net.minecraftforge.common.Tags.Items.TOOLS_SWORDS)
                || stack.getItem() instanceof SwordItem
                || stack.getItem() instanceof TridentItem;
    }

    @Override
    public double attributeValue(LivingEntity entity, AttributeKind type) {
        return entity.getAttributeValue(attributeOf(type));
    }

    private static Attribute attributeOf(AttributeKind type) {
        return switch (type) {
            case ATTACK_DAMAGE -> Attributes.ATTACK_DAMAGE;
            case MOVEMENT_SPEED -> Attributes.MOVEMENT_SPEED;
            case ARMOR -> Attributes.ARMOR;
        };
    }

    @Override
    public double itemAttackDamage(ItemStack stack) {
        return stack.getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(Attributes.ATTACK_DAMAGE)
                .stream()
                .mapToDouble(AttributeModifier::getAmount)
                .sum();
    }

    @Override
    public void addModifier(LivingEntity entity, AttributeKind type, String key, UUID legacyId,
                            double amount, ModifierOperation operation) {
        AttributeInstance instance = entity.getAttribute(attributeOf(type));
        if (instance == null || instance.getModifier(legacyId) != null) {
            return;
        }
        instance.addTransientModifier(new AttributeModifier(legacyId, key, amount, operationOf(operation)));
    }

    @Override
    public void removeModifier(LivingEntity entity, AttributeKind type, String key, UUID legacyId) {
        AttributeInstance instance = entity.getAttribute(attributeOf(type));
        if (instance != null) {
            instance.removeModifier(legacyId);
        }
    }

    private static AttributeModifier.Operation operationOf(ModifierOperation operation) {
        return switch (operation) {
            case ADD -> AttributeModifier.Operation.ADDITION;
            case MULTIPLY_TOTAL -> AttributeModifier.Operation.MULTIPLY_TOTAL;
        };
    }

    @Override
    public void applyEffect(LivingEntity entity, Effect type, int duration, int amplifier,
                            boolean ambient, boolean visible) {
        entity.addEffect(new MobEffectInstance(effectOf(type), duration, amplifier, ambient, visible));
    }

    private static MobEffect effectOf(Effect type) {
        return switch (type) {
            case SLOWNESS -> MobEffects.MOVEMENT_SLOWDOWN;
            case STRENGTH -> MobEffects.DAMAGE_BOOST;
            case ABSORPTION -> MobEffects.ABSORPTION;
            case SPEED -> MobEffects.MOVEMENT_SPEED;
            case BLINDNESS -> MobEffects.BLINDNESS;
        };
    }

    @Override
    public void applyEffect(LivingEntity entity, RegistryEntry<MobEffect> type, int duration, int amplifier,
                            boolean ambient, boolean visible) {
        entity.addEffect(new MobEffectInstance(type.get(), duration, amplifier, ambient, visible));
    }

    @Override
    public boolean hasEffect(LivingEntity entity, RegistryEntry<MobEffect> type) {
        return entity.hasEffect(type.get());
    }

    @Override
    public int enchantmentLevel(Level level, ItemStack stack, Enchantment enchantment) {
        net.minecraft.world.item.enchantment.Enchantment value = switch (enchantment) {
            case RIPTIDE -> net.minecraft.world.item.enchantment.Enchantments.RIPTIDE;
            case LOYALTY -> net.minecraft.world.item.enchantment.Enchantments.LOYALTY;
            case CHANNELING -> net.minecraft.world.item.enchantment.Enchantments.CHANNELING;
            case PIERCING -> net.minecraft.world.item.enchantment.Enchantments.PIERCING;
            case UNBREAKING -> net.minecraft.world.item.enchantment.Enchantments.UNBREAKING;
        };
        return net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(value, stack);
    }

    @Override
    public BlockPos blockPos(Vec3 position) {
        return new BlockPos(position);
    }

    @Override
    public boolean isOnCooldown(Player player, ItemStack stack) {
        return player.getCooldowns().isOnCooldown(stack.getItem());
    }

    @Override
    public float cooldownPercent(Player player, ItemStack stack, float partialTick) {
        return player.getCooldowns().getCooldownPercent(stack.getItem(), partialTick);
    }

    @Override
    public void addCooldown(Player player, ItemStack stack, int ticks) {
        player.getCooldowns().addCooldown(stack.getItem(), ticks);
    }

    @Override
    public void damageItem(ItemStack stack, int amount, LivingEntity holder) {
        stack.hurtAndBreak(amount, holder, owner -> owner.broadcastBreakEvent(InteractionHand.MAIN_HAND));
    }

    @Override
    public void damageItemInUsedHand(ItemStack stack, int amount, LivingEntity holder) {
        stack.hurtAndBreak(amount, holder, owner -> owner.broadcastBreakEvent(owner.getUsedItemHand()));
    }

    @Override
    public void damageItemRandom(ItemStack stack, int amount, LivingEntity holder) {
        if (stack.hurt(amount, net.minecraft.util.RandomSource.create(), null)) {
            stack.shrink(1);
        }
    }

    @Override
    public boolean hurt(LivingEntity target, Player attacker, float amount) {
        if (attacker != null) {
            return target.hurt(DamageSource.playerAttack(attacker), amount);
        }
        return target.hurt(DamageSource.GENERIC, amount);
    }

    @Override
    public boolean hurtMob(LivingEntity target, LivingEntity attacker, float amount) {
        return target.hurt(DamageSource.mobAttack(attacker), amount);
    }

    @Override
    public boolean hurtThorns(LivingEntity target, LivingEntity defender, float amount) {
        return target.hurt(DamageSource.thorns(defender), amount);
    }

    @Override
    public void playSound(Level level, double x, double y, double z, Sound sound, float volume, float pitch) {
        SoundEvent event;
        SoundSource source;
        switch (sound) {
            case RAID_HORN -> {
                event = SoundEvents.RAID_HORN;
                source = SoundSource.PLAYERS;
            }
            case GENERIC_SPLASH -> {
                event = SoundEvents.GENERIC_SPLASH;
                source = SoundSource.BLOCKS;
            }
            case TRIDENT_THROW -> {
                event = SoundEvents.TRIDENT_THROW;
                source = SoundSource.PLAYERS;
            }
            case TRIDENT_RIPTIDE_1 -> {
                event = SoundEvents.TRIDENT_RIPTIDE_1;
                source = SoundSource.PLAYERS;
            }
            case TRIDENT_RIPTIDE_2 -> {
                event = SoundEvents.TRIDENT_RIPTIDE_2;
                source = SoundSource.PLAYERS;
            }
            default -> {
                event = SoundEvents.TRIDENT_RIPTIDE_3;
                source = SoundSource.PLAYERS;
            }
        }
        level.playSound(null, x, y, z, event, source, volume, pitch);
    }

    @Override
    public void sendOverlayMessage(Player player, Component message) {
        player.displayClientMessage(message, true);
    }

    @Override
    public MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }

    @Override
    public boolean isOnGround(Entity entity) {
        return entity.isOnGround();
    }

    @Override
    public void grantAdvancement(ServerPlayer player, String path) {
        Advancement advancement = player.server.getAdvancements()
                .getAdvancement(new ResourceLocation(ChineseWeapons.MOD_ID, path));
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
        return player.getInventory().selected;
    }

    @Override
    public void clearAggro(Mob mob) {
        mob.setTarget(null);
        mob.setLastHurtByMob(null);
        mob.setLastHurtByPlayer(null);
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
        return type.create(level);
    }

    @Override
    public void moveEntity(Entity entity, double x, double y, double z, float yaw, float pitch) {
        entity.moveTo(x, y, z, yaw, pitch);
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
