package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
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
                        ChineseWeaponsModEffects.CusEffectSupplier.get(),
                        120,
                        0,
                        true,
                        true
                ));

                if(livingEntity instanceof ServerPlayer hitPlayer) {
                    Advancement adv = hitPlayer.server.getAdvancements()
                            .getAdvancement(ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "get_dagger_axe/dagger_axe_hook_has_cuts_xg"));
                    if(adv != null) {
                        AdvancementProgress ap = hitPlayer.getAdvancements().getOrStartProgress(adv);
                        if(!ap.isDone()) {
                            for(String cri : ap.getRemainingCriteria()) {
                                hitPlayer.getAdvancements().award(adv, cri);
                            }
                        }
                    }
                }
            }
        }
    }
}
