package net.mirrorloong.chineseweapons.spi;

import net.mirrorloong.chineseweapons.ChineseWeapons;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;

/**
 * Services a Minecraft loader target provides to the shared source tree.
 */
public interface Platform {
    Registration registration();

    Network network();

    /**
     * Factory for every concrete piece of content the shared {@code init}
     * classes describe. All targets implement it; the shared tree only names
     * registry ids and definitions.
     */
    default ContentFactory content() {
        throw new UnsupportedOperationException("This platform does not provide a content factory");
    }

    /**
     * Version bridge used by {@code rules.*}. Every target supplies one; the
     * shared gameplay rules never touch loader or version-specific APIs on
     * their own.
     */
    GameplayCompat gameplay();

    /**
     * Legacy JSON ingredient/result decoding used by the 1.18.2-1.20.1 casting
     * recipe serializer. Codec-based targets do not need it.
     */
    default RecipeSupport recipes() {
        throw new UnsupportedOperationException("This platform does not provide recipe JSON support");
    }

    boolean isClient();

    MenuType<WeaponsCastingTableTypeGuiMenu> castingTableMenu();

    void openCastingTable(ServerPlayer player, BlockPos pos);

    /**
     * Attaches the registrations collected during bootstrap to the loader.
     * Called by the target entry point after {@code ChineseWeapons.bootstrap}.
     */
    void attach();
}
