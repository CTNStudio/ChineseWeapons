package net.mirrorloong.chineseweapons.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class WeaponScriptSettings {
    public enum Skill {
        HOOK_DISMOUNT,
        HIT_EFFECT,
        GLAIVE_DISMOUNT,
        GUDUO_STOPRIDING
    }

    public enum ALLWeapon {
        GUDUO_ARMORBLINDNESS,
        GUDUO_HURTDOWNWARD,
        GUDUO_HURTSKILLTICK,
        SONGSTANDINGSHIELD_BEHINDCENTER,
        SONGSTANDINGSHIELD_ANGLE,
        FACEGUARDHELMET_KH
    }
    public enum Logic {
        SONGSTANDINGSHIELD_ENDURANCEDISPLAY,
        FOOTMENARMOR_GAIN
    }

    private static final Map<ResourceLocation, Double> REACH_BONUSES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, EnumMap<Skill, Float>> SKILL_CHANCES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, EnumMap<ALLWeapon, Double>> ARMOR_THRESHOLDS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, EnumMap<Logic, Boolean>> LOGIC_CHANCES = new ConcurrentHashMap<>();

    private WeaponScriptSettings() {
    }

    public static void setReachBonus(ResourceLocation itemId, double bonus) {
        if (!Double.isFinite(bonus) || bonus < 0.0D || bonus > 64.0D) {
            throw new IllegalArgumentException("Reach bonus must be between 0 and 64: " + bonus);
        }
        REACH_BONUSES.put(itemId, bonus);
    }

    public static double getReachBonus(Item item, double fallback) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        return itemId == null ? fallback : REACH_BONUSES.getOrDefault(itemId, fallback);
    }

    public static void setChance(ResourceLocation itemId, Skill skill, double chance) {
        if (!Double.isFinite(chance) || chance <= 0.0D || chance >= 1.0D) {
            throw new IllegalArgumentException("Skill chance must be between 0 and 1: " + chance);
        }
        SKILL_CHANCES.computeIfAbsent(itemId, ignored -> new EnumMap<>(Skill.class)).put(skill, (float) chance);
    }

    public static float getChance(Item item, Skill skill, float fallback) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        EnumMap<Skill, Float> values = itemId == null ? null : SKILL_CHANCES.get(itemId);
        return values == null ? fallback : values.getOrDefault(skill, fallback);
    }

    public static void setArmorThreshold(ResourceLocation itemId, ALLWeapon threshold, double value) {
        if (!Double.isFinite(value) || value < 0.0D) {
            throw new IllegalArgumentException("Armor threshold must be between 0: " + value);
        }
        ARMOR_THRESHOLDS.computeIfAbsent(itemId, ignored -> new EnumMap<>(ALLWeapon.class)).put(threshold, value);
    }

    public static double getArmorThreshold(Item item, ALLWeapon threshold, double fallback) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        EnumMap<ALLWeapon, Double> values = itemId == null ? null : ARMOR_THRESHOLDS.get(itemId);
        return values == null ? fallback : values.getOrDefault(threshold, fallback);
    }

    public static void setLogicChance(ResourceLocation itemId, Logic logic, String value) {
        if (!Objects.equals(value, "true") && !Objects.equals(value, "false")) {
            throw new IllegalArgumentException("Logic chance must be between true/false: " + value);
        }
        LOGIC_CHANCES.computeIfAbsent(itemId, ignored -> new EnumMap<>(Logic.class)).put(logic, Boolean.parseBoolean(value));
    }

    public static boolean getLogicChance(Item item, Logic logic, boolean fallback) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        EnumMap<Logic, Boolean> values = itemId == null ? null : LOGIC_CHANCES.get(itemId);
        return values == null ? fallback : values.getOrDefault(logic, fallback);
    }
}