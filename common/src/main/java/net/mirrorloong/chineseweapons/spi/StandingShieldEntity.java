package net.mirrorloong.chineseweapons.spi;

import net.minecraft.world.item.ItemStack;

/**
 * The shared shield action packet and placement rules need to read and restore
 * a deployed standing shield. Entity classes stay in the targets, so they
 * expose the few operations the shared code needs through this marker.
 */
public interface StandingShieldEntity {

    /** The shield item stored on the entity, with its current damage. */
    ItemStack storedItem();

    /** Remaining endurance of the deployed shield. */
    float shieldHealth();

    /** Re-initialises a freshly created entity: yaw, stored item and health. */
    void storeShield(ItemStack stack, float health, float yaw);

    /** Removes the entity; {@code Entity#discard} satisfies this. */
    void discard();
}
