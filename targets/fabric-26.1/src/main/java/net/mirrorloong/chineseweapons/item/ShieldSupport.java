package net.mirrorloong.chineseweapons.item;

import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.component.BlocksAttacks;

/**
 * 26.1 removed {@code ShieldItem}; blocking is a {@link BlocksAttacks} data
 * component on an off-hand equippable item. These values mirror the vanilla
 * shield reduction (90% of blocked damage, item damage absorbed normally).
 */
public final class ShieldSupport {
    public static final BlocksAttacks BLOCKS = new BlocksAttacks(
            0.25F,
            1.0F,
            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
            BlocksAttacks.ItemDamageFunction.DEFAULT,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    private ShieldSupport() {
    }
}
