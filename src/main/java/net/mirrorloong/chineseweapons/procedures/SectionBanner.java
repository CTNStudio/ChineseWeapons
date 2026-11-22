package net.mirrorloong.chineseweapons.procedures;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.item.Items;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModTabs;

import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class SectionBanner {

    public static final int COLUMNS = 9;
    public static final int VISIBLE_ROWS = 5;

    private static volatile List<Layout> layouts = List.of();

    private SectionBanner() {
    }

    public static CreativeModeTab.Builder create(CreativeModeTab.Builder builder, Consumer<SectionOutput> populator) {
        return builder.displayItems((parameters, output) -> {
            SectionOutput out = new SectionOutput();
            populator.accept(out);
            layouts = List.copyOf(out.layouts);
                                    fillDisplayContents(output, out.items);
        });
    }

    @SuppressWarnings("unchecked")
    private static void fillDisplayContents(CreativeModeTab.Output output, List<ItemStack> items) {
                                                for (Field field : output.getClass().getDeclaredFields()) {
            if (!java.util.Collection.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                field.setAccessible(true);
                java.util.Collection<ItemStack> target = (java.util.Collection<ItemStack>) field.get(output);
                if (target == null) {
                    continue;
                }
                                                                                if (field.getType() == java.util.Collection.class) {
                    if (!setFieldValue(field, output, new ArrayList<>(items))) {
                                                target.clear();
                        target.addAll(items);
                    }
                } else {
                    target.clear();
                    for (ItemStack stack : items) {
                        if (!stack.isEmpty()) {
                            target.add(stack);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot fill creative tab contents", e);
            }
        }
    }

    private static boolean setFieldValue(Field field, Object owner, Object value) {
        try {
            field.setAccessible(true);
            field.set(owner, value);
            return true;
        } catch (Throwable throwable) {
            net.mirrorloong.chineseweapons.ChineseweaponsMod.LOGGER.warn(
                    "[SectionBanner] cannot replace {} with List: {}", field.getName(), throwable.toString());
            return false;
        }
    }

    public static List<Layout> layouts() {
        return layouts;
    }

            public static void init() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof CreativeModeInventoryScreen creative) {
                                if (REGISTERED.add(creative)) {
                    ScreenEvents.afterRender(screen).register((renderedScreen, graphics, mouseX, mouseY, tickDelta) ->
                            renderBanners(creative, graphics));
                }
            }
        });
    }

    private static final java.util.Set<Screen> REGISTERED = java.util.Collections.newSetFromMap(new java.util.WeakHashMap<>());
    private static boolean debugLogged;

    private static void renderBanners(CreativeModeInventoryScreen screen, GuiGraphics graphics) {
                        CreativeModeTab selected = (CreativeModeTab) readField(selectedTabField(), null);
        if (selected == null || selected != ChineseweaponsModTabs.CHINESE_WEAPONS.get()) {
            return;
        }
        List<Layout> current = layouts;
        if (current.isEmpty()) {
            return;
        }

        float scrollOffs = readFloatField(scrollOffsField(), screen);
        List<ItemStack> menuItems = readMenuItems(screen);
        int totalRows = (menuItems.size() + COLUMNS - 1) / COLUMNS;
        int hiddenRows = Math.max(totalRows - VISIBLE_ROWS, 0);
        int visibleStart = Math.max((int) (scrollOffs * hiddenRows + 0.5f), 0);

                                int guiLeft = (screen.width - 195) / 2;
        int guiTop = (screen.height - 136) / 2;
        Font font = Minecraft.getInstance().font;

        if (!debugLogged) {
            debugLogged = true;
            net.mirrorloong.chineseweapons.ChineseweaponsMod.LOGGER.info(
                    "[SectionBanner] render: layouts={} rows={} items={} hiddenRows={} scroll={} gui=({},{}) firstLayoutRow={} displayItemsClass={}",
                    current.size(), totalRows, menuItems.size(), hiddenRows, scrollOffs, guiLeft, guiTop,
                    current.get(0).row(), selected.getDisplayItems().getClass().getSimpleName());
        }

        for (Layout l : current) {
            int screenRow = l.row() - visibleStart;
            if (screenRow < 0 || screenRow >= VISIBLE_ROWS) continue;
            int x = guiLeft + 9;
            int y = guiTop + 18 + screenRow * 18;
            cwtab_render(graphics, font, x, y, l);
        }
    }

    private static Field selectedTabField;
    private static Field scrollOffsField;
    private static Field menuItemsField;

    private static Field selectedTabField() {
        if (selectedTabField == null) {
            selectedTabField = findField(CreativeModeInventoryScreen.class,
                    field -> Modifier.isStatic(field.getModifiers()) && field.getType() == CreativeModeTab.class);
        }
        return selectedTabField;
    }

    private static Field scrollOffsField() {
        if (scrollOffsField == null) {
            scrollOffsField = findField(CreativeModeInventoryScreen.class,
                    field -> !Modifier.isStatic(field.getModifiers()) && field.getType() == float.class);
        }
        return scrollOffsField;
    }

    private static Field findField(Class<?> owner, Predicate<Field> predicate) {
        for (Field field : owner.getDeclaredFields()) {
            if (predicate.test(field)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new IllegalStateException("Cannot locate field in " + owner.getName());
    }

    private static Object readField(Field field, Object owner) {
        try {
            return field.get(owner);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field.getName(), e);
        }
    }

    private static float readFloatField(Field field, Object owner) {
        try {
            return field.getFloat(owner);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<net.minecraft.world.item.ItemStack> readMenuItems(CreativeModeInventoryScreen screen) {
        Object menu = screen.getMenu();
        if (menuItemsField == null) {
            menuItemsField = findField(menu.getClass(), field -> List.class.isAssignableFrom(field.getType()));
        }
        return (List<net.minecraft.world.item.ItemStack>) readField(menuItemsField, menu);
    }

    private static void cwtab_render(GuiGraphics g, Font font, int x, int y, Layout l) {        int width = COLUMNS * 18;
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
            if (icon != Items.AIR) {
                var pose = g.pose();
                pose.pushPose();
                pose.translate(cur + 8, y + 1 + 8, 0);
                pose.scale(scale, scale, 1);
                g.renderItem(new ItemStack(icon), -8, -8);
                pose.popPose();
            } else {
                ResourceLocation tex = new ResourceLocation(l.iconId().getNamespace(), l.iconId().getPath());
                g.blit(tex, cur, y + 1, 0, 0, iconSize, iconSize, 16, 16);
            }
            cur += iconSize + 2;
        }
        if (text != null) {
            g.drawString(font, text, cur, y + (height - 8) / 2, l.textColor(), false);
        }
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
}
