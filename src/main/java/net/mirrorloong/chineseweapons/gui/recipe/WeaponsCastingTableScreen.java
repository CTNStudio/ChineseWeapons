package net.mirrorloong.chineseweapons.gui.recipe;
//old api net.minecraft.client.gui.screens.MenuScreens.register(...) -> new api net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(...)
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.ArmorCastingTableBlockEntityCache;

public class WeaponsCastingTableScreen extends AbstractContainerScreen<WeaponsCastingTableTypeGuiMenu> {

    private final ResourceLocation TextureBackGround =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/gui/weapons_casting_table.png");

    public WeaponsCastingTableScreen(WeaponsCastingTableTypeGuiMenu pMenu, Inventory pInventory, Component pComponent) {
        super(pMenu, pInventory, pComponent);
        this.inventoryLabelX = -19;
        this.titleLabelX = -19;
        this.inventoryLabelY -= 2;
    }

        public static void register() {
        ScreenRegistry.register(DeferredGui.RegMenu.get(), WeaponsCastingTableScreen::new);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float v) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, x, y, v);

        BlockPos pos = this.menu.getTablePos();
        int progress = ArmorCastingTableBlockEntityCache.get(pos);
        boolean hasRecipe = ArmorCastingTableBlockEntityCache.hasRecipe(pos);

        if (hasRecipe && progress < 100) {
            int slotX = this.leftPos + 115 - 5;
            int slotY = this.topPos + 31;
            guiGraphics.fill(slotX, slotY, slotX + 25, slotY + 23, 0xAA555555);
        }

        this.renderTooltip(guiGraphics, x, y);
    }

                                                                                                                                                                                            @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int guiX = (this.width - this.imageWidth) / 2 - 27;
        int guiY = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TextureBackGround, guiX, guiY, 0, 0, 256, 256);

        int barX = guiX + 80;
        int barY = guiY + 17;
        int barW = 53;

        BlockPos pos = this.menu.getTablePos();
        int progress = ArmorCastingTableBlockEntityCache.get(pos);
        float ratio = progress / 100.0F;
        float cutoff = barW * ratio;

        int emptyColor = 0xFFCFA88A;
        int fullColor  = 0xFFFFFFFF;

                for (int col = 0; col < 53; col++) {
            int worldX = barX + 0 + col;
            float globalColX = 0 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 2) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 3) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 4) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 5) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 6) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 7) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 8) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 9) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 10) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 11) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 12) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 13) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 14) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 15) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 16) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 17) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 18) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 19) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 20) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 21) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 22) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 23) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 24) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 25) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 26) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 27) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 28) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 29) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 30) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 31) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 32) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 33) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 34) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 35) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 36) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 37) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 38) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 39) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 40) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 41) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 42) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 43) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 44) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 45) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 29, color);
            }
            if (col == 46) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 28, color);
            }
            if (col == 47) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 27, color);
            }
            if (col == 48) {
                guiGraphics.fill(worldX, barY + 17, worldX + 1, barY + 26, color);
            }
            if (col == 49) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 25, color);
            }
            if (col == 50) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 24, color);
            }
            if (col == 51) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            }
            if (col == 52) {
                guiGraphics.fill(worldX, barY + 21, worldX + 1, barY + 22, color);
            }
        }

                for (int col = 0; col < 16; col++) {
            int worldX = barX + 17 + col;
            float globalColX = 17 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 16, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 17, color);
            }
            if (col == 2) {
                guiGraphics.fill(worldX, barY + 13, worldX + 1, barY + 16, color);
            }
            if (col == 3) {
                guiGraphics.fill(worldX, barY + 4, worldX + 1, barY + 5, color);
                guiGraphics.fill(worldX, barY + 12, worldX + 1, barY + 15, color);
            }
            if (col == 4) {
                guiGraphics.fill(worldX, barY + 3, worldX + 1, barY + 6, color);
                guiGraphics.fill(worldX, barY + 11, worldX + 1, barY + 14, color);
            }
            if (col == 5) {
                guiGraphics.fill(worldX, barY + 2, worldX + 1, barY + 7, color);
                guiGraphics.fill(worldX, barY + 10, worldX + 1, barY + 13, color);
            }
            if (col == 6) {
                guiGraphics.fill(worldX, barY + 1, worldX + 1, barY + 8, color);
                guiGraphics.fill(worldX, barY + 9, worldX + 1, barY + 12, color);
            }
            if (col == 7) {
                guiGraphics.fill(worldX, barY + 0, worldX + 1, barY + 11, color);
            }
            if (col == 8) {
                guiGraphics.fill(worldX, barY + 1, worldX + 1, barY + 10, color);
            }
            if (col == 9) {
                guiGraphics.fill(worldX, barY + 2, worldX + 1, barY + 11, color);
            }
            if (col == 10) {
                guiGraphics.fill(worldX, barY + 3, worldX + 1, barY + 12, color);
            }
            if (col == 11) {
                guiGraphics.fill(worldX, barY + 4, worldX + 1, barY + 13, color);
            }
            if (col == 12) {
                guiGraphics.fill(worldX, barY + 5, worldX + 1, barY + 12, color);
            }
            if (col == 13) {
                guiGraphics.fill(worldX, barY + 6, worldX + 1, barY + 11, color);
            }
            if (col == 14) {
                guiGraphics.fill(worldX, barY + 7, worldX + 1, barY + 10, color);
            }
            if (col == 15) {
                guiGraphics.fill(worldX, barY + 8, worldX + 1, barY + 9, color);
            }
        }

                for (int col = 0; col < 2; col++) {
            int worldX = barX + 24 + col;
            float globalColX = 24 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 13, worldX + 1, barY + 15, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 16, color);
            }
        }

                for (int col = 0; col < 6; col++) {
            int worldX = barX + 27 + col;
            float globalColX = 27 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 16, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 17, color);
            }
            if (col == 2) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 17, color);
            }
            if (col == 3) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 17, color);
            }
            if (col == 4) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 17, color);
            }
            if (col == 5) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 16, color);
            }
        }

                for (int col = 0; col < 2; col++) {
            int worldX = barX + 29 + col;
            float globalColX = 29 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 15, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 15, color);
            }
        }

                for (int col = 0; col < 2; col++) {
            int worldX = barX + 33 + col;
            float globalColX = 33 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 11, worldX + 1, barY + 13, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 10, worldX + 1, barY + 12, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 34 + col;
            float globalColX = 34 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 6, worldX + 1, barY + 7, color);
            }
        }

                for (int col = 0; col < 2; col++) {
            int worldX = barX + 1 + col;
            float globalColX = 1 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 4 + col;
            float globalColX = 4 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 5 + col;
            float globalColX = 5 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 20, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 6 + col;
            float globalColX = 6 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 7 + col;
            float globalColX = 7 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 20, color);
            }
        }

                for (int col = 0; col < 1; col++) {
            int worldX = barX + 8 + col;
            float globalColX = 8 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

                for (int col = 0; col < 2; col++) {
            int worldX = barX + 10 + col;
            float globalColX = 10 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
            }
            if (col == 1) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
            }
        }

    }

}
