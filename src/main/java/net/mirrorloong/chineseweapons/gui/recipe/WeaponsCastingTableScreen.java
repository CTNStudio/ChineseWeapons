package net.mirrorloong.chineseweapons.gui.recipe;

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
            ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "textures/gui/weapons_casting_table.png");

    public WeaponsCastingTableScreen(WeaponsCastingTableTypeGuiMenu pMenu, Inventory pInventory, Component pComponent) {
        super(pMenu, pInventory, pComponent);
        this.inventoryLabelX = -19;
        this.titleLabelX = -19;
        this.inventoryLabelY -= 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float v) {
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
    protected void renderBg(GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
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
        int fullColor = 0xFFFFFFFF;

        for (int col = 0; col < barW; col++) {
            int worldX = barX + col;
            int color = (col < cutoff) ? fullColor : emptyColor;

            if (col < 45) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            } else if (col == 45) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 29, color);
            } else if (col == 46) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 28, color);
            } else if (col == 47) {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 27, color);
            } else if (col == 48) {
                guiGraphics.fill(worldX, barY + 17, worldX + 1, barY + 26, color);
            } else if (col == 49) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 25, color);
            } else if (col == 50) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 24, color);
            } else if (col == 51) {
                guiGraphics.fill(worldX, barY + 20, worldX + 1, barY + 23, color);
            } else if (col == 52) {
                guiGraphics.fill(worldX, barY + 21, worldX + 1, barY + 22, color);
            }
        }

        // 选区 2
        for (int col = 0; col < 16; col++) {
            int worldX = barX + 17 + col;
            int globalColX = 17 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 16, color);
            } else if (col == 1) {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 17, color);
            } else if (col == 2) {
                guiGraphics.fill(worldX, barY + 13, worldX + 1, barY + 16, color);
            } else if (col == 3) {
                guiGraphics.fill(worldX, barY + 4, worldX + 1, barY + 5, color);
                guiGraphics.fill(worldX, barY + 12, worldX + 1, barY + 15, color);
            } else if (col == 4) {
                guiGraphics.fill(worldX, barY + 3, worldX + 1, barY + 6, color);
                guiGraphics.fill(worldX, barY + 11, worldX + 1, barY + 14, color);
            } else if (col == 5) {
                guiGraphics.fill(worldX, barY + 2, worldX + 1, barY + 7, color);
                guiGraphics.fill(worldX, barY + 10, worldX + 1, barY + 13, color);
            } else if (col == 6) {
                guiGraphics.fill(worldX, barY + 1, worldX + 1, barY + 8, color);
                guiGraphics.fill(worldX, barY + 9, worldX + 1, barY + 12, color);
            } else if (col == 7) {
                guiGraphics.fill(worldX, barY + 0, worldX + 1, barY + 11, color);
            } else if (col == 8) {
                guiGraphics.fill(worldX, barY + 1, worldX + 1, barY + 10, color);
            } else if (col == 9) {
                guiGraphics.fill(worldX, barY + 2, worldX + 1, barY + 11, color);
            } else if (col == 10) {
                guiGraphics.fill(worldX, barY + 3, worldX + 1, barY + 12, color);
            } else if (col == 11) {
                guiGraphics.fill(worldX, barY + 4, worldX + 1, barY + 13, color);
            } else if (col == 12) {
                guiGraphics.fill(worldX, barY + 5, worldX + 1, barY + 12, color);
            } else if (col == 13) {
                guiGraphics.fill(worldX, barY + 6, worldX + 1, barY + 11, color);
            } else if (col == 14) {
                guiGraphics.fill(worldX, barY + 7, worldX + 1, barY + 10, color);
            } else if (col == 15) {
                guiGraphics.fill(worldX, barY + 8, worldX + 1, barY + 9, color);
            }
        }

        // 选区 3
        for (int col = 0; col < 2; col++) {
            int worldX = barX + 24 + col;
            int globalColX = 24 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 13, worldX + 1, barY + 15, color);
            } else {
                guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 16, color);
            }
        }

        // 选区 4
        for (int col = 0; col < 6; col++) {
            int worldX = barX + 27 + col;
            int globalColX = 27 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0 || col == 5) {
                guiGraphics.fill(worldX, barY + 15, worldX + 1, barY + 16, color);
            } else {
                guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 17, color);
            }
        }

        // 选区 5
        for (int col = 0; col < 2; col++) {
            int worldX = barX + 29 + col;
            int globalColX = 29 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + 14, worldX + 1, barY + 15, color);
        }

        // 选区 6
        for (int col = 0; col < 2; col++) {
            int worldX = barX + 33 + col;
            int globalColX = 33 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 11, worldX + 1, barY + 13, color);
            } else {
                guiGraphics.fill(worldX, barY + 10, worldX + 1, barY + 12, color);
            }
        }

        // 选区 7
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 34 + col;
            int globalColX = 34 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + 6, worldX + 1, barY + 7, color);
        }

        // 选区 8
        for (int col = 0; col < 2; col++) {
            int worldX = barX + 1 + col;
            int globalColX = 1 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
        }

        // 选区 9-13
        int[][] singleColDecor = {
                {4, 18, 19}, {5, 19, 20}, {6, 18, 19}, {7, 19, 20}, {8, 18, 19}
        };
        for (int[] dec : singleColDecor) {
            int col = dec[0];
            int worldX = barX + col;
            int color = (col < cutoff) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + dec[1], worldX + 1, barY + dec[2], color);
        }

        // 选区 14
        for (int col = 0; col < 2; col++) {
            int worldX = barX + 10 + col;
            int globalColX = 10 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + 16, worldX + 1, barY + 18, color);
        }
    }
}