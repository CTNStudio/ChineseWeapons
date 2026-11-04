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
import net.mirrorloong.chineseweapons.client.model.Modelface_guard;
import net.mirrorloong.chineseweapons.client.model.ModelSongJavelinModel;
import net.mirrorloong.chineseweapons.client.model.ModelSongStandingShield;
import net.mirrorloong.chineseweapons.client.model.Modelbohai_helmet;
import net.mirrorloong.chineseweapons.client.model.Modelblack_chui_armor;
import net.mirrorloong.chineseweapons.client.model.Modelearly_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelfine_scale_armor;
import net.mirrorloong.chineseweapons.client.model.Modelfootmen_armor;
import net.mirrorloong.chineseweapons.client.model.Modellate_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmiddle_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmountain_character_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsong_dynasty_infantry_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsuanni_helmet;
import net.mirrorloong.chineseweapons.client.model.Modeltang_dynasty_infantry_armor;
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
        EntityModelLayerRegistry.registerModelLayer(Modelmountain_character_armor.LAYER_LOCATION,
                Modelmountain_character_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelearly_tang_dynasty_ming_guang_armor.LAYER_LOCATION,
                Modelearly_tang_dynasty_ming_guang_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION,
                Modelmiddle_tang_dynasty_ming_guang_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION,
                Modellate_tang_dynasty_ming_guang_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelfootmen_armor.LAYER_LOCATION,
                Modelfootmen_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelblack_chui_armor.LAYER_LOCATION,
                Modelblack_chui_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modeltang_dynasty_infantry_armor.LAYER_LOCATION,
                Modeltang_dynasty_infantry_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelfine_scale_armor.LAYER_LOCATION,
                Modelfine_scale_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelsuanni_helmet.LAYER_LOCATION,
                Modelsuanni_helmet::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelbohai_helmet.LAYER_LOCATION,
                Modelbohai_helmet::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModelSongJavelinModel.LAYER_LOCATION,
                ModelSongJavelinModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModelSongStandingShield.LAYER_LOCATION,
                ModelSongStandingShield::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelsong_dynasty_infantry_armor.LAYER_LOCATION,
                Modelsong_dynasty_infantry_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelface_guard.LAYER_LOCATION,
                Modelface_guard::createBodyLayer);
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
