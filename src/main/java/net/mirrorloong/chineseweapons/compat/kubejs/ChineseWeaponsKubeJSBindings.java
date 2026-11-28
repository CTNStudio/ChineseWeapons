package net.mirrorloong.chineseweapons.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public final class ChineseWeaponsKubeJSBindings {
    private ChineseWeaponsKubeJSBindings() {
    }

        public static void setReachBonus(String itemId, double bonus) {
        WeaponScriptSettings.setReachBonus(parseItemId(itemId), bonus);
    }

        public static void setHookDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HOOK_DISMOUNT, chance);
    }

        public static void setHitEffectChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HIT_EFFECT, chance);
    }

        public static void setGlaiveDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, chance);
    }

        public static void setGuduoArmorBlindnessChance(String itemId, double chance) {
        WeaponScriptSettings.setThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_ARMORBLINDNESS, chance);
    }

        public static void setGuduoHurtDownwardChance(String itemId, double chance) {
        WeaponScriptSettings.setThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_HURTDOWNWARD, chance);
    }

        public static void setGuduoHurtSkillTickChance(String itemId, double chance) {
        WeaponScriptSettings.setThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_HURTSKILLTICK, chance);
    }

    //设置骨朵让对方盾牌冷却多久 用法 ChineseWeapons.setGuduoHurtTargetCoolTickChance('物品ID', 数值)
        public static void setGuduoHurtTargetCoolTickChance(String itemId, double chance) {
        WeaponScriptSettings.setThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_TargetCoolTICK, chance);
    }

    //设置骨朵让对方盾牌冷却触发概率 用法 ChineseWeapons.setGuduoHurtTargetCoolChanceChance('物品ID', 概率)
        public static void setGuduoHurtTargetCoolChanceChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GUDUO_TargetCoolChance, chance);
    }

        public static void setGuduoStopRidingChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GUDUO_STOPRIDING, chance);
    }

        public static void setSongStandingShieldBehindCenterChance(double chance) {
        WeaponScriptSettings.setThreshold(null, WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, chance);
    }

        public static void setSongStandingShield_AngleChance(double chance) {
        WeaponScriptSettings.setThreshold(null, WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_ANGLE, chance);
    }

        public static void setSongStandingShieldEnduranceDisplay(String chance) {
        WeaponScriptSettings.setLogicChance(null, WeaponScriptSettings.Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY, chance);
    }

        public static void setArmorGain(String itemId, String chance) {
        WeaponScriptSettings.setLogicChance(parseItemId(itemId), WeaponScriptSettings.Logic.ARMOR_GAIN, chance);
    }

        public static void setFaceGuardHelmetFarFromChance(String itemId, double chance) {
        WeaponScriptSettings.setThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.FACEGUARDHELMET_KH, chance);
    }

    private static ResourceLocation parseItemId(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid item id: " + itemId);
        }
        return id;
    }
}
