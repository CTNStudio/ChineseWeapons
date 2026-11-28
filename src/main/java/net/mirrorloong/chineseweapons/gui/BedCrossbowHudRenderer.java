package net.mirrorloong.chineseweapons.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;

public final class BedCrossbowHudRenderer {

    private static final ResourceLocation DURABILITY_TEXTURE =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/gui/bed_crossbow_durability.png");
    private static final ResourceLocation CHARGE_TEXTURE =
            new ResourceLocation(ChineseweaponsMod.MODID, "textures/gui/bed_crossbow_charge_up.png");

    private static final int DURABILITY_WIDTH = 45;
    private static final int DURABILITY_HEIGHT = 18;
    private static final int CHARGE_WIDTH = 17;
    private static final int CHARGE_HEIGHT = 5;

    private static boolean registered;

    private BedCrossbowHudRenderer() {
    }

    //old api @Mod.EventBusSubscriber(CLIENT) + RenderGuiEvent.Post -> new api HudRenderCallback
    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        HudRenderCallback.EVENT.register(BedCrossbowHudRenderer::onRenderGui);
    }

    private static void onRenderGui(GuiGraphics graphics, float tickDelta) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        BedCrossbowEntity crossbow = BedCrossbowEntity.findOperating(player);
        if (crossbow == null) {
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int x = screenWidth - DURABILITY_WIDTH - 8;
        int y = 8;

        float durability = Mth.clamp(
                crossbow.getHealth() / BedCrossbowEntity.MAX_HEALTH, 0.0F, 1.0F);
        graphics.blit(DURABILITY_TEXTURE, x, y, 0, 0,
                DURABILITY_WIDTH, DURABILITY_HEIGHT, DURABILITY_WIDTH, DURABILITY_HEIGHT);
        maskDurabilityBar(graphics, x, y, durability);

        String durabilityText = Component.translatable(
                "gui.chineseweapons.bed_crossbow.durability",
                Integer.toString((int) crossbow.getHealth())).getString();
        graphics.drawString(minecraft.font, durabilityText,
                x - 4 - minecraft.font.width(durabilityText), y + 5,
                0xFFFFFFFF, true);

        if (!crossbow.hasLoadedBolt()) {
            return;
        }

        int chargeX = x + DURABILITY_WIDTH - CHARGE_WIDTH;
        int chargeY = y + DURABILITY_HEIGHT + 6;
        float charge = Mth.clamp(crossbow.getCharge(), 0.0F, 1.0F);
        graphics.blit(CHARGE_TEXTURE, chargeX, chargeY, 0, 0,
                CHARGE_WIDTH, CHARGE_HEIGHT, CHARGE_WIDTH, CHARGE_HEIGHT);
        maskChargeBar(graphics, chargeX, chargeY, charge);

        String chargeText = Component.translatable(
                "gui.chineseweapons.bed_crossbow.charge",
                Math.round(charge * 100.0F) + "%").getString();
        int chargeTextX = chargeX - 4 - minecraft.font.width(chargeText);
        ItemStack loaded = crossbow.getLoadedStack();
        if (!loaded.isEmpty()) {
            graphics.renderItem(loaded, chargeTextX - 18, chargeY - 5);
        }
        graphics.drawString(minecraft.font, chargeText,
                chargeTextX, chargeY - 2, 0xFFFFFFFF, true);
    }

    private static void maskDurabilityBar(GuiGraphics graphics, int x, int y, float ratio) {
        int barX = x + 16;
        int barY = y + 7;
        int width = 28;
        float cutoff = width * ratio;
        int emptyColor = 0xFF3A2118;

        for (int col = 0; col < width; col++) {
            if (col + 1 <= cutoff) {
                continue;
            }
            int top = col == 0 ? 4 : (col == 1 ? 3 : 0);
            int from = barX + Math.max(col, (int) Math.ceil(cutoff));
            if (from < barX + col + 1) {
                graphics.fill(from, barY + top, barX + col + 1, barY + 5, emptyColor);
            }
        }
    }

    private static void maskChargeBar(GuiGraphics graphics, int x, int y, float ratio) {
        int barX = x + 1;
        int barY = y + 1;
        int width = 15;
        float cutoff = width * ratio;
        int emptyColor = 0xFF18202A;

        for (int col = 0; col < width; col++) {
            if (col + 1 <= cutoff) {
                continue;
            }
            int from = barX + Math.max(col, (int) Math.ceil(cutoff));
            if (from < barX + col + 1) {
                graphics.fill(from, barY, barX + col + 1, barY + 3, emptyColor);
            }
        }
    }
}
