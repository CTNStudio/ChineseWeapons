package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class SectionBanner extends CreativeModeTab {

    public static final int COLUMNS = 9;
    public static final int VISIBLE_ROWS = 5;

    private final Consumer<SectionOutput> populator;
    private List<Layout> layouts = List.of();

    private SectionBanner(Builder builder, Consumer<SectionOutput> populator) {
        super(builder);
        this.populator = populator;
    }

    public static Builder create(Builder builder, Consumer<SectionOutput> populator) {
        return builder.withTabFactory(b -> new SectionBanner(b, populator));
    }

    @Override
    public void buildContents(ItemDisplayParameters parameters) {
        SectionOutput out = new SectionOutput();
        populator.accept(out);
        this.layouts = List.copyOf(out.layouts);
        try {
            Field f = CreativeModeTab.class.getDeclaredField("displayItems");
            f.setAccessible(true);
            f.set(this, List.copyOf(out.items));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Layout> layouts() {
        return layouts;
    }

    public static final class SectionOutput {
        private final List<ItemStack> items = new ArrayList<>();
        private final List<Layout> layouts = new ArrayList<>();

        public void accept(ItemStack stack) {
            if (!stack.isEmpty()) items.add(stack);
        }

        public void accept(Item item) {
            accept(new ItemStack(item));
        }

        public void section(Component text,
                            @Nullable Integer bg,
                            @Nullable Integer border,
                            @Nullable Integer textColor,
                            @Nullable ResourceLocation wpicon,
                            float iconScale,
                            String align) {
            int remainder = items.size() % COLUMNS;
            if (remainder != 0) {
                for (int i = remainder; i < COLUMNS; i++) items.add(ItemStack.EMPTY);
            }
            int row = items.size() / COLUMNS;
            layouts.add(new Layout(row, text, bg, border,
                    textColor == null ? 0xFFFFFFFF : textColor, wpicon, iconScale, align));
            for (int i = 0; i < COLUMNS; i++) items.add(ItemStack.EMPTY);
        }
    }

    public record Layout(int row, Component text,
                         @Nullable Integer bg, @Nullable Integer border,
                         int textColor,
                         @Nullable ResourceLocation iconId,
                         float iconScale,
                         String align) {}

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class Renderer {
        @SubscribeEvent
        public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
            Screen screen = event.getScreen();
            if (!(screen instanceof CreativeModeInventoryScreen creative)) return;

            CreativeModeTab tab = getSelectedTab();
            if (!(tab instanceof SectionBanner sectioned)) return;
            List<Layout> layouts = sectioned.layouts();
            if (layouts.isEmpty()) return;

            float scrollOffs = getScrollOffs(creative);
            int totalRows = (creative.getMenu().items.size() + COLUMNS - 1) / COLUMNS;
            int hiddenRows = Math.max(totalRows - VISIBLE_ROWS, 0);
            int visibleStart = Math.max((int) (scrollOffs * hiddenRows + 0.5f), 0);

            GuiGraphics g = event.getGuiGraphics();
            Font font = creative.getMinecraft().font;
            int guiLeft = creative.getGuiLeft();
            int guiTop = creative.getGuiTop();

            for (Layout l : layouts) {
                int screenRow = l.row() - visibleStart;
                if (screenRow < 0 || screenRow >= VISIBLE_ROWS) continue;
                int x = guiLeft + 9;
                int y = guiTop + 18 + screenRow * 18;
                cwtab_render(g, font, x, y, l);
            }
        }

        private static void cwtab_render(GuiGraphics g, Font font, int x, int y, Layout l) {
            int width = COLUMNS * 18;
            int height = 18;
            x -= 1;
            y -= 1;
            if (l.bg() != null) g.fill(x, y, x + width, y + height, l.bg());
            if (l.border() != null) {
                g.fill(x, y, x + width, y + 1, l.border());
                g.fill(x, y + height - 1, x + width, y + height, l.border());
                g.fill(x, y, x + 1, y + height, l.border());
                g.fill(x + width - 1, y, x + width, y + height, l.border());
            }

            float scale = Math.max(0.01f, Math.min(2.0f, l.iconScale()));
            int iconSize = Math.round(16 * scale);
            Component text = l.text();
            int textWidth = text != null ? font.width(text) : 0;
            int contentWidth = (l.iconId() != null ? iconSize + 2 : 0) + textWidth;
            int startX;
            switch (l.align()) {
                case "middle" -> startX = x + Math.max(2, (width - contentWidth) / 2);
                case "right"  -> startX = x + Math.max(2, width - contentWidth - 2);
                default       -> startX = x + 2;
            }
            int cur = startX;
            if (l.iconId() != null) {
                Item icon = BuiltInRegistries.ITEM.get(l.iconId());
                var pose = g.pose();
                pose.pushPose();
                pose.translate(cur + 8, y + 1 + 8, 0);
                pose.scale(scale, scale, 1);
                g.renderItem(new ItemStack(icon), -8, -8);
                pose.popPose();
                cur += iconSize + 2;
            }
            if (text != null) {
                g.drawString(font, text, cur, y + (height - 8) / 2, l.textColor(), false);
            }
        }

        private static CreativeModeTab getSelectedTab() {
            try {
                Field f = CreativeModeInventoryScreen.class.getDeclaredField("selectedTab");
                f.setAccessible(true);
                return (CreativeModeTab) f.get(null);
            } catch (Exception e) {
                return null;
            }
        }

        private static float getScrollOffs(CreativeModeInventoryScreen screen) {
            try {
                Field f = CreativeModeInventoryScreen.class.getDeclaredField("scrollOffs");
                f.setAccessible(true);
                return f.getFloat(screen);
            } catch (Exception e) {
                return 0f;
            }
        }
    }
}