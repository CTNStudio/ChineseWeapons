package net.mirrorloong.chineseweapons.fabric;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.Identifier;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.client.model.FabricModelFactory;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableScreen;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEntities;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import net.mirrorloong.chineseweapons.renderer.SongJavelinRenderer;
import net.mirrorloong.chineseweapons.renderer.SongStandingShieldRenderer;

import org.lwjgl.glfw.GLFW;

/**
 * Fabric replacements for the Forge client setup classes: menu screen, entity
 * renderers, model layers and the skill key binding.
 *
 * <p>Item properties, item colour providers and per-item armour renderers are
 * gone in 26.1: item and equipment rendering is data driven, so the armour
 * sets now use generated equipment assets instead of Java-side models.</p>
 */
public final class FabricClientSetup implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "chinese_weapons"));

    private static final KeyMapping ACTION_KEY = new KeyMapping(
            "key.skill.text",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY);

    @Override
    public void onInitializeClient() {
        MenuScreens.register(DeferredGui.REG_MENU.get(), WeaponsCastingTableScreen::new);
        registerEntityRenderers();
        registerModelLayers();
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
        for (ModelSpec entity : EntityModelSpecs.all()) {
            ModelLayerRegistry.registerModelLayer(factory.location(entity), () -> factory.layer(entity));
        }
    }

    private static void registerKeyBinding() {
        KeyMappingHelper.registerKeyMapping(ACTION_KEY);
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
