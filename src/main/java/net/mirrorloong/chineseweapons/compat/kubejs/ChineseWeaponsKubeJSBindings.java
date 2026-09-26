package net.mirrorloong.chineseweapons.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

public final class ChineseWeaponsKubeJSBindings {
    private ChineseWeaponsKubeJSBindings() {
    }

    //设置攻击距离加成 用法 ChineseWeapons.setReachBonus('物品ID', 加成数值)
    public static void setReachBonus(String itemId, double bonus) {
        WeaponScriptSettings.setReachBonus(parseItemId(itemId), bonus);
    }

    //设置戈右键钩取下马概率 用法 ChineseWeapons.setHookDismountChance('物品ID', 概率)
    public static void setHookDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HOOK_DISMOUNT, chance);
    }

    //设置命中效果触发概率 用法 ChineseWeapons.setHitEffectChance('物品ID', 概率)
    public static void setHitEffectChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.HIT_EFFECT, chance);
    }

    //设置戟类下马概率 用法 ChineseWeapons.setGlaiveDismountChance('物品ID', 概率)
    public static void setGlaiveDismountChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, chance);
    }

    //设置骨朵护甲致盲概率 用法 ChineseWeapons.setGuduoArmorBlindnessChance('物品ID', 概率)
    public static void setGuduoArmorBlindnessChance(String itemId, double chance) {
        WeaponScriptSettings.setArmorThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_ARMORBLINDNESS, chance);
    }

    //设置骨朵击退/下压格 用法 ChineseWeapons.setGuduoHurtDownwardChance('物品ID', 数值)
    public static void setGuduoHurtDownwardChance(String itemId, double chance) {
        WeaponScriptSettings.setArmorThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_HURTDOWNWARD, chance);
    }

    //设置骨朵受击技能触发概率 用法 ChineseWeapons.setGuduoHurtSkillTickChance('物品ID', 概率)
    public static void setGuduoHurtSkillTickChance(String itemId, double chance) {
        WeaponScriptSettings.setArmorThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.GUDUO_HURTSKILLTICK, chance);
    }

    //设置骨朵停止骑乘概率 用法 ChineseWeapons.setGuduoStopRidingChance('物品ID', 概率)
    public static void setGuduoStopRidingChance(String itemId, double chance) {
        WeaponScriptSettings.setChance(parseItemId(itemId), WeaponScriptSettings.Skill.GUDUO_STOPRIDING, chance);
    }

    //设置宋代立盾背后防爆多少格 用法 ChineseWeapons.setSongStandingShieldBehindCenterChance(数值)
    public static void setSongStandingShieldBehindCenterChance(double chance) {
        WeaponScriptSettings.setArmorThreshold(null, WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER, chance);
    }

    //设置宋代立盾旋转角度 用法 ChineseWeapons.setSongStandingShield_AngleChance(角度)
    public static void setSongStandingShield_AngleChance(double chance) {
        WeaponScriptSettings.setArmorThreshold(null, WeaponScriptSettings.ALLWeapon.SONGSTANDINGSHIELD_ANGLE, chance);
    }

    //设置显示唐代立盾耐久开关 用法 ChineseWeapons.setSongStandingShieldEnduranceDisplay(true/false)
    public static void setSongStandingShieldEnduranceDisplay(String chance) {
        WeaponScriptSettings.setLogicChance(null, WeaponScriptSettings.Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY, chance);
    }

    //设置步人甲增益开关 用法 ChineseWeapons.setFootmenArmorGain('物品ID', true/false)
    public static void setFootmenArmorGain(String itemId, String chance) {
        WeaponScriptSettings.setLogicChance(parseItemId(itemId), WeaponScriptSettings.Logic.FOOTMENARMOR_GAIN, chance);
    }

    //设置面甲驱逐生物最大格数 用法 ChineseWeapons.setFaceGuardHelmetFarFromChance('物品ID', 格数)
    public static void setFaceGuardHelmetFarFromChance(String itemId, double chance) {
        WeaponScriptSettings.setArmorThreshold(parseItemId(itemId), WeaponScriptSettings.ALLWeapon.FACEGUARDHELMET_KH, chance);
    }


    private static ResourceLocation parseItemId(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid item id: " + itemId);
        }
        return id;
    }
}
