
package net.mirrorloong.chineseweapons.init;

import net.mirrorloong.chineseweapons.client.model.*;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChineseweaponsModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelmountain_character_armor.LAYER_LOCATION, Modelmountain_character_armor::createBodyLayer);
		event.registerLayerDefinition(Modelearly_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modelearly_tang_dynasty_ming_guang_armor::createBodyLayer);
		event.registerLayerDefinition(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modelmiddle_tang_dynasty_ming_guang_armor::createBodyLayer);
        event.registerLayerDefinition(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modellate_tang_dynasty_ming_guang_armor::createBodyLayer);
		event.registerLayerDefinition(Modelfootmen_armor.LAYER_LOCATION, Modelfootmen_armor::createBodyLayer);
		event.registerLayerDefinition(Modelblack_chui_armor.LAYER_LOCATION, Modelblack_chui_armor::createBodyLayer);
		event.registerLayerDefinition(Modeltang_dynasty_infantry_armor.LAYER_LOCATION, Modeltang_dynasty_infantry_armor::createBodyLayer);
		event.registerLayerDefinition(Modelfine_scale_armor.LAYER_LOCATION, Modelfine_scale_armor::createBodyLayer);
        event.registerLayerDefinition(Modelsuanni_helmet.LAYER_LOCATION, Modelsuanni_helmet::createBodyLayer);
        event.registerLayerDefinition(Modelbohai_helmet.LAYER_LOCATION, Modelbohai_helmet::createBodyLayer);
	}
}
