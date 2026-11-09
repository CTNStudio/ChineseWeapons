package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public final class DeferredGui {
    private static final RegistryBuilder<MenuType<?>> REGISTRY =
            ChineseWeapons.registration().builder(Registries.MENU);

    public static final RegistryEntry<MenuType<WeaponsCastingTableTypeGuiMenu>> REG_MENU =
            REGISTRY.register("weapons_casting_table",
                    () -> IMenuTypeExtension.create((windowId, inventory, data) ->
                            new WeaponsCastingTableTypeGuiMenu(windowId, inventory, data.readBlockPos())));

    private DeferredGui() {
    }

    public static void init() {
    }
}
