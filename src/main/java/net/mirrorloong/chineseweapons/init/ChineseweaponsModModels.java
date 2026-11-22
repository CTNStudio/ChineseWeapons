package net.mirrorloong.chineseweapons.init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.mirrorloong.chineseweapons.client.model.*;

public class ChineseweaponsModModels {
	//old api @SubscribeEvent registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) + event.registerLayerDefinition(...) -> new api EntityModelLayerRegistry.registerModelLayer(...)
	public static void registerLayerDefinitions() {
		EntityModelLayerRegistry.registerModelLayer(Modelmountain_character_armor.LAYER_LOCATION, Modelmountain_character_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelearly_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modelearly_tang_dynasty_ming_guang_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modelmiddle_tang_dynasty_ming_guang_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION, Modellate_tang_dynasty_ming_guang_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelfootmen_armor.LAYER_LOCATION, Modelfootmen_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelblack_chui_armor.LAYER_LOCATION, Modelblack_chui_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modeltang_dynasty_infantry_armor.LAYER_LOCATION, Modeltang_dynasty_infantry_armor::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(Modelfine_scale_armor.LAYER_LOCATION, Modelfine_scale_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelsuanni_helmet.LAYER_LOCATION, Modelsuanni_helmet::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelbohai_helmet.LAYER_LOCATION, Modelbohai_helmet::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModelSongJavelinModel.LAYER_LOCATION, ModelSongJavelinModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModelSongStandingShield.LAYER_LOCATION, ModelSongStandingShield::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelsong_dynasty_infantry_armor.LAYER_LOCATION, Modelsong_dynasty_infantry_armor::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Modelface_guard.LAYER_LOCATION, Modelface_guard::createBodyLayer);
	}
}
