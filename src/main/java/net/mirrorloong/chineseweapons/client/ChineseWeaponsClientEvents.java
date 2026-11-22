package net.mirrorloong.chineseweapons.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.color.item.ItemColor;
//old api ForgeRegistries.ITEMS -> new api BuiltInRegistries.ITEM
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.client.event.ArmPoseEvents;
import net.mirrorloong.chineseweapons.client.event.HandRenderEvents;
import net.mirrorloong.chineseweapons.event.GlaiveSpinStartedEvent;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModEvents;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModKeyHandler;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModModels;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;

public final class ChineseWeaponsClientEvents {
    private static final ResourceLocation BLOCKING = new ResourceLocation(ChineseweaponsMod.MODID, "blocking");

    private ChineseWeaponsClientEvents() {
    }

    public static void initClient() {
        //old api EntityRenderersEvent.RegisterLayerDefinitions -> new api EntityModelLayerRegistry.registerModelLayer
        ChineseweaponsModModels.registerLayerDefinitions();
        //old api EntityRenderersEvent.RegisterRenderers -> new api EntityRendererRegistry.register
        ChineseweaponsModEvents.registerEntityRenderers();
        //old api RegisterKeyMappingsEvent -> new api KeyBindingHelper.registerKeyBinding
        ChineseweaponsModKeyHandler.registerKeyBinding();
                registerItemProperties();
        //old api RegisterColorHandlersEvent.Item -> new api ColorProviderRegistry.ITEM
        registerItemColors();
                HandRenderEvents.register(GlaiveSpinAnimationHandler::onRenderHand);
        HandRenderEvents.register(GlaiveItemClientEvent::modifyItemRender);
        HandRenderEvents.register(TangDynastyHengSaberClientEvents::modifyItemRender);
                ArmPoseEvents.register(JavelinArmPose::apply);
        //old api @Mod.EventBusSubscriber(FORGE) + @SubscribeEvent startGlaiveAnimation(GlaiveSpinStartedEvent) -> new api GlaiveSpinStartedEvent.register
        GlaiveSpinStartedEvent.register(GlaiveClientEvents::startGlaiveAnimation);
                ClientTickEvents.END_CLIENT_TICK.register(GlaiveSpinAnimationHandler::onClientTick);
                ClientTickEvents.END_CLIENT_TICK.register(ChineseweaponsModKeyHandler::onKey);
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

    //old api @SubscribeEvent registerItemColors(RegisterColorHandlersEvent.Item event) -> new api ColorProviderRegistry.ITEM
    private static void registerItemColors() {
        ItemColor dyeableColor = (stack, tintIndex) -> {
            if (tintIndex == 1 && DyeableItem.hasCustomColor(stack)) {
                return DyeableItem.getColor(stack);
            }
            return 0xFFFFFF;
        };

        Item[] coloredItems = BuiltInRegistries.ITEM.stream()
                //old api ForgeRegistries.ITEMS.getKey(item) -> new api BuiltInRegistries.ITEM.getKey(item)
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
        //old api event.register(dyeableColor, coloredItems) -> new api ColorProviderRegistry.ITEM.register(...)
        ColorProviderRegistry.ITEM.register(dyeableColor, coloredItems);
    }

}
