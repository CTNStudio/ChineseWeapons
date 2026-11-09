package net.mirrorloong.chineseweapons.gui.recipe;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

/**
 * Fabric replacement for the Forge target's {@code DeferredGui}: the block
 * position travels through Fabric's menu opening data instead of
 * {@code IForgeMenuType.create}. 26.1 renamed the screen-handler API to the
 * menu API ({@link ExtendedMenuType}).
 */
public final class DeferredGui {
    private static final RegistryBuilder<MenuType<?>> REGISTRY =
            ChineseWeapons.registration().builder(Registries.MENU);

    public static final RegistryEntry<MenuType<WeaponsCastingTableTypeGuiMenu>> REG_MENU =
            REGISTRY.register("weapons_casting_table",
                    () -> new ExtendedMenuType<WeaponsCastingTableTypeGuiMenu, BlockPos>(
                            (id, inventory, data) -> new WeaponsCastingTableTypeGuiMenu(id, inventory, data),
                            BlockPos.STREAM_CODEC));

    private DeferredGui() {
    }

    public static void init() {
    }
}
