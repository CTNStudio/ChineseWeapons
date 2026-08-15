package net.mirrorloong.chineseweapons.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

@EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ChineseWeaponsClientEvents {
    private static final ResourceLocation BLOCKING = ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "blocking");

    private ChineseWeaponsClientEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                ChineseweaponsModItems.TangDynastyShield.get(),
                BLOCKING,
                (stack, level, entity, seed) -> entity != null
                        && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1.0F : 0.0F
        ));
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColor dyeableColor = (stack, tintIndex) -> {
            if (tintIndex == 1 && DyeableItem.hasCustomColor(stack)) {
                return DyeableItem.getColor(stack) | 0xFF000000;
            }
            return 0xFFFFFFFF;
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
                            || path.contains("mountain_character_armor_");
                })
                .toArray(Item[]::new);
        event.register(dyeableColor, coloredItems);
    }

}
