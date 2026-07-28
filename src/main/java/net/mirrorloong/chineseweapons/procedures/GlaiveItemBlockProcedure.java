package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlaiveItemBlockProcedure {
    public static void execute(Level world, LivingEntity entity, ItemStack itemstack, DamageSource source) {
        if (entity == null)
            return;

        if (entity instanceof Player player && player.isCrouching()) {
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                if (itemstack.isDamageableItem()) {
                    if (entity instanceof Player _player)
                        _player.causeFoodExhaustion(1);
                }
            }
        }
    }
}