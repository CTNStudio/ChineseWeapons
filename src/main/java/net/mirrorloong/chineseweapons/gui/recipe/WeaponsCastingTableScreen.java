package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
public class WeaponsCastingTableScreen extends AbstractContainerScreen<WeaponsCastingTableTypeGuiMenu> {

    private final ResourceLocation TextureBackGround = ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID,"textures/gui/weapons_casting_table.png");
    public WeaponsCastingTableScreen(WeaponsCastingTableTypeGuiMenu pMenu, Inventory pInventory, Component pComponent){
        super(pMenu,pInventory,pComponent);
        this.inventoryLabelX=-19;
        this.titleLabelX=-19;
        this.inventoryLabelY-=2;
    }
    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float v) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics,x,y,v);
        this.renderTooltip(guiGraphics, x,y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(TextureBackGround, (this.width - this.imageWidth) / 2-27, (this.height - this.imageHeight) / 2,0,0,256, 256);
    }

}