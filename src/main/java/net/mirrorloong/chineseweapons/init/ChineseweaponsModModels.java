
package net.mirrorloong.chineseweapons.init;

import net.mirrorloong.chineseweapons.client.model.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
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
	}
}
