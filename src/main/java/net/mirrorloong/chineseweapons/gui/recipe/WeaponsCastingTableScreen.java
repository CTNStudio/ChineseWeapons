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
            int slotX = this.leftPos + 115;
            int slotY = this.topPos + 35;
            guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xAA555555);
        }

        this.renderTooltip(guiGraphics, x, y);
    }


    //定位 (80,37) , (80,40) , (125,40) , (125,46) , (133,39) , (133,38) , (125,31) , (125,37)
    //像素包围盒：u=80, v=31, w=53, h=15
    //包围盒对角像素：左上(80,31)，右下(133,46)
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int guiX = (this.width - this.imageWidth) / 2 - 27;
        int guiY = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TextureBackGround, guiX, guiY, 0, 0, 256, 256);

        int barX = guiX + 80;
        int barY = guiY + 31;
        int barW = 53;

        BlockPos pos = this.menu.getTablePos();
        int progress = ArmorCastingTableBlockEntityCache.get(pos);
        float ratio = progress / 100.0F;

        int emptyColor = 0xFFCFA88A; // #CFA88A
        int fullColor  = 0xFFFFFFFF;

        for (int col = 0; col < barW; col++) {
            int worldX = barX + col;

            int y1, y2;
            if (col < 45) {
                y1 = 6;
                y2 = 9;
            } else if (col == 45) {
                y1 = 0;
                y2 = 15;
            } else {
                float t = (col - 45) / 8.0F;
                y1 = Math.round(0 + 8 * t);
                y2 = Math.round(15 - 7 * t);
            }

            int color = (col < barW * ratio) ? fullColor : emptyColor;
            guiGraphics.fill(worldX, barY + y1, worldX + 1, barY + y2, color);
        }
    }
}