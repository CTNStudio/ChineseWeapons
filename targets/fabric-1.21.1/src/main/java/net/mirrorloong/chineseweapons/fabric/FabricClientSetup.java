package net.mirrorloong.chineseweapons.fabric;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.model.FabricModelFactory;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpec;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.fabric.client.FabricArmorRenderers;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableScreen;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

import org.lwjgl.glfw.GLFW;

/**
 * Fabric replacements for the Forge client setup classes: menu screen, entity
 * renderers, model layers, item properties/colors, armor renderers and the
 * skill key binding.
 */
public final class FabricClientSetup implements ClientModInitializer {
    private static final ResourceLocation BLOCKING = ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "blocking");

    private static final KeyMapping ACTION_KEY = new KeyMapping(
            "key.skill.text",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "item_group.chineseweapons.chinese_weapons");

    @Override
    public void onInitializeClient() {
        MenuScreens.register(DeferredGui.REG_MENU.get(), WeaponsCastingTableScreen::new);
        registerEntityRenderers();
        registerModelLayers();
        registerItemProperties();
        registerItemColors();
        FabricArmorRenderers.register();
        registerKeyBinding();
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ChineseweaponsModEntities.type(ChineseweaponsModEntities.SONG_JAVELIN_PROJECTILE),
                SongJavelinRenderer::new);
        EntityRendererRegistry.register(ChineseweaponsModEntities.type(ChineseweaponsModEntities.STANDING_SHIELD),
                SongStandingShieldRenderer::new);
    }

    private static void registerModelLayers() {
        FabricModelFactory factory = FabricModelFactory.INSTANCE;
        for (ArmorModelSpec armour : ArmorModelSpecs.all()) {
            EntityModelLayerRegistry.registerModelLayer(factory.location(armour.model()),
                    () -> factory.layer(armour.model()));
        }
        for (ModelSpec entity : EntityModelSpecs.all()) {
            EntityModelLayerRegistry.registerModelLayer(factory.location(entity),
                    () -> factory.layer(entity));
        }
    }

    private static void registerItemProperties() {
        ItemProperties.register(
                ChineseweaponsModItems.TangDynastyShield.get(),
                BLOCKING,
                (stack, level, entity, seed) -> entity != null
                        && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
        ItemProperties.register(
                ChineseweaponsModItems.SongStandingShield.get(),
                BLOCKING,
                (stack, level, entity, seed) -> entity != null
                        && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }

    private static void registerItemColors() {
        ItemColor dyeableColor = (stack, tintIndex) -> {
            if (tintIndex == 1 && DyeableItem.hasCustomColor(stack)) {
                return DyeableItem.getColor(stack);
            }
            return 0xFFFFFF;
        };

        Item[] coloredItems = BuiltInRegistries.ITEM.stream()
                .filter(item -> {
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                    if (key == null) {
                        return false;
                    }
                    String path = key.getPath();
                    return path.contains("black_chui_armor_")
                            || path.endsWith("_bohai_helmet")
                            || path.contains("early_tang_dynasty_ming_guang_armor_")
                            || path.contains("fine_scale_armor_")
                            || path.contains("footmen_armor_")
                            || path.contains("late_tang_dynasty_ming_guang_armor_")
                            || path.contains("middle_tang_dynasty_ming_guang_armor_")
                            || path.contains("mountain_character_armor_")
                            || path.contains("song_dynasty_infantry_armor_");
                })
                .toArray(Item[]::new);
        ColorProviderRegistry.ITEM.register(dyeableColor, coloredItems);
    }

    private static void registerKeyBinding() {
        KeyBindingHelper.registerKeyBinding(ACTION_KEY);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.screen != null) {
                return;
            }
            while (ACTION_KEY.consumeClick()) {
                ChineseWeapons.network().sendToServer(new ShieldActionPacket());
            }
        });
    }
}
