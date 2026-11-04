package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 26.1 replaced the immediate-mode {@code GuiGraphics} API with render state
 * extraction, so this target ships the plain container screen layout without
 * the bespoke casting-table background and progress arc.
 */
public class WeaponsCastingTableScreen extends AbstractContainerScreen<WeaponsCastingTableTypeGuiMenu> {

    public WeaponsCastingTableScreen(WeaponsCastingTableTypeGuiMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }
}
