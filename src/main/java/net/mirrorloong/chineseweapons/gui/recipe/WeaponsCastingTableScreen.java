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
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/gui/weapons_casting_table.png");

    public WeaponsCastingTableScreen(WeaponsCastingTableTypeGuiMenu pMenu, Inventory pInventory, Component pComponent) {
        super(pMenu, pInventory, pComponent);
        this.inventoryLabelX = -19;
        this.titleLabelX = -19;
        this.inventoryLabelY -= 2;
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


    //UV详细信息 -- by github sf ovo https://github.com/sf326/mc_gui_uv_extract
    //at pages https://sf326.github.io/mc_gui_uv_extract/
    //贴图尺寸：256 × 256
    //
    //【选区 1】
    //顶点：(80,37) , (80,40) , (125,40) , (125,46) , (126,46) , (126,45) , (127,45) , (127,44) , (128,44) , (128,43) , (129,43) , (129,42) , (130,42) , (130,41) , (131,41) , (131,40) , (132,40) , (132,39) , (133,39) , (133,38) , (132,38) , (132,37) , (131,37) , (131,36) , (130,36) , (130,35) , (129,35) , (129,34) , (128,34) , (128,33) , (127,33) , (127,32) , (126,32) , (126,31) , (125,31) , (125,37)
    //包围盒：u=80, v=31, w=53, h=15
    //对角：左上(80,31)，右下(133,46)
    //归一UV：[0.3125,0.1211,0.5195,0.1797]
    //--------------------------------------------------------
    //【选区 2】
    //顶点：(97,32) , (97,33) , (98,33) , (98,34) , (99,34) , (99,33) , (100,33) , (100,32) , (101,32) , (101,31) , (102,31) , (102,30) , (103,30) , (103,29) , (104,29) , (104,28) , (105,28) , (105,27) , (106,27) , (106,28) , (107,28) , (107,29) , (108,29) , (108,30) , (109,30) , (109,29) , (110,29) , (110,28) , (111,28) , (111,27) , (112,27) , (112,26) , (113,26) , (113,25) , (112,25) , (112,24) , (111,24) , (111,23) , (110,23) , (110,22) , (109,22) , (109,21) , (108,21) , (108,20) , (107,20) , (107,19) , (106,19) , (106,18) , (105,18) , (105,17) , (104,17) , (104,18) , (103,18) , (103,19) , (102,19) , (102,20) , (101,20) , (101,21) , (100,21) , (100,22) , (101,22) , (101,23) , (102,23) , (102,24) , (103,24) , (103,25) , (104,25) , (104,26) , (103,26) , (103,27) , (102,27) , (102,28) , (101,28) , (101,29) , (100,29) , (100,30) , (99,30) , (99,31) , (98,31) , (98,32)
    //包围盒：u=97, v=17, w=16, h=17
    //对角：左上(97,17)，右下(113,34)
    //归一UV：[0.3789,0.0664,0.4414,0.1328]
    //--------------------------------------------------------
    //【选区 3】
    //顶点：(104,30) , (104,32) , (105,32) , (105,33) , (106,33) , (106,31) , (105,31) , (105,30)
    //包围盒：u=104, v=30, w=2, h=3
    //对角：左上(104,30)，右下(106,33)
    //归一UV：[0.4063,0.1172,0.4141,0.1289]
    //--------------------------------------------------------
    //【选区 4】
    //顶点：(107,32) , (107,33) , (108,33) , (108,34) , (112,34) , (112,33) , (113,33) , (113,32) , (112,32) , (112,33) , (108,33) , (108,32)
    //包围盒：u=107, v=32, w=6, h=2
    //对角：左上(107,32)，右下(113,34)
    //归一UV：[0.4180,0.1250,0.4414,0.1328]
    //--------------------------------------------------------
    //【选区 5】
    //顶点：(109,31) , (109,32) , (111,32) , (111,31)
    //包围盒：u=109, v=31, w=2, h=1
    //对角：左上(109,31)，右下(111,32)
    //归一UV：[0.4258,0.1211,0.4336,0.1250]
    //--------------------------------------------------------
    //【选区 6】
    //顶点：(113,28) , (113,30) , (114,30) , (114,29) , (115,29) , (115,27) , (114,27) , (114,28)
    //包围盒：u=113, v=27, w=2, h=3
    //对角：左上(113,27)，右下(115,30)
    //归一UV：[0.4414,0.1055,0.4492,0.1172]
    //--------------------------------------------------------
    //【选区 7】
    //顶点：(114,23) , (114,24) , (115,24) , (115,23)
    //包围盒：u=114, v=23, w=1, h=1
    //对角：左上(114,23)，右下(115,24)
    //归一UV：[0.4453,0.0898,0.4492,0.0938]
    //--------------------------------------------------------
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

        // 选区 1
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

        // 选区 2
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

        // 选区 3
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

        // 选区 4
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

        // 选区 5
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

        // 选区 6
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

        // 选区 7
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 34 + col;
            float globalColX = 34 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 6, worldX + 1, barY + 7, color);
            }
        }

        // 选区 8
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

        // 选区 9
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 4 + col;
            float globalColX = 4 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

        // 选区 10
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 5 + col;
            float globalColX = 5 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 20, color);
            }
        }

        // 选区 11
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 6 + col;
            float globalColX = 6 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

        // 选区 12
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 7 + col;
            float globalColX = 7 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 19, worldX + 1, barY + 20, color);
            }
        }

        // 选区 13
        for (int col = 0; col < 1; col++) {
            int worldX = barX + 8 + col;
            float globalColX = 8 + col;
            int color = (globalColX < cutoff) ? fullColor : emptyColor;

            if (col == 0) {
                guiGraphics.fill(worldX, barY + 18, worldX + 1, barY + 19, color);
            }
        }

        // 选区 14
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