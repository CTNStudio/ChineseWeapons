package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;

public class DaggerAxeAndGlaiveItemjizhongProcedure {
    private static final RandomSource RANDOM = RandomSource.create();

    public static void execute(Entity entity, ItemStack weapon) {
        if (entity == null) {
            return;
        }

        if (entity.level().isClientSide()) {
            return;
        }

        float chance = WeaponScriptSettings.getChance(weapon.getItem(), WeaponScriptSettings.Skill.HIT_EFFECT, 0.80F);
        if (RANDOM.nextFloat() < chance) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(
                        ChineseWeaponsModEffects.CusEffectSupplier,
                        120,
                        0,
                        true,
                        true
                ));
            }
        }
    }
}
