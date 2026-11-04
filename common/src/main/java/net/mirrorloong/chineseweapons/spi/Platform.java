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

    boolean isClient();

    MenuType<WeaponsCastingTableTypeGuiMenu> castingTableMenu();

    void openCastingTable(ServerPlayer player, BlockPos pos);

    /**
     * Attaches the registrations collected during bootstrap to the loader.
     * Called by the target entry point after {@code ChineseWeapons.bootstrap}.
     */
    void attach();
}
