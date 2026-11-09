package net.mirrorloong.chineseweapons.gui.recipe;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

/**
 * Fabric replacement for the Forge target's {@code DeferredGui}: the block
 * position travels through Fabric's extended screen handler opening data
 * instead of {@code IForgeMenuType.create}.
 */
public final class DeferredGui {
    private static final RegistryBuilder<MenuType<?>> REGISTRY =
            ChineseWeapons.registration().builder(Registries.MENU);

    public static final RegistryEntry<MenuType<WeaponsCastingTableTypeGuiMenu>> REG_MENU =
            REGISTRY.register("weapons_casting_table",
                    () -> new ExtendedScreenHandlerType<WeaponsCastingTableTypeGuiMenu>(
                            (id, inventory, data) -> new WeaponsCastingTableTypeGuiMenu(id, inventory, data.readBlockPos())));

    private DeferredGui() {
    }

    public static void init() {
    }
}
