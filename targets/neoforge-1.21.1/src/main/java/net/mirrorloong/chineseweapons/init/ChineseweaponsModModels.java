package net.mirrorloong.chineseweapons.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.mirrorloong.chineseweapons.client.model.ModelSongJavelinModel;
import net.mirrorloong.chineseweapons.client.model.ModelSongStandingShield;
import net.mirrorloong.chineseweapons.client.model.Modelblack_chui_armor;
import net.mirrorloong.chineseweapons.client.model.Modelbohai_helmet;
import net.mirrorloong.chineseweapons.client.model.Modelearly_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelface_guard;
import net.mirrorloong.chineseweapons.client.model.Modelfine_scale_armor;
import net.mirrorloong.chineseweapons.client.model.Modelfootmen_armor;
import net.mirrorloong.chineseweapons.client.model.Modellate_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmiddle_tang_dynasty_ming_guang_armor;
import net.mirrorloong.chineseweapons.client.model.Modelmountain_character_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsong_dynasty_infantry_armor;
import net.mirrorloong.chineseweapons.client.model.Modelsuanni_helmet;
import net.mirrorloong.chineseweapons.client.model.Modeltang_dynasty_infantry_armor;

public class ChineseweaponsModModels {
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
        event.registerLayerDefinition(ModelSongJavelinModel.LAYER_LOCATION, ModelSongJavelinModel::createBodyLayer);
        event.registerLayerDefinition(ModelSongStandingShield.LAYER_LOCATION, ModelSongStandingShield::createBodyLayer);
        event.registerLayerDefinition(Modelsong_dynasty_infantry_armor.LAYER_LOCATION, Modelsong_dynasty_infantry_armor::createBodyLayer);
        event.registerLayerDefinition(Modelface_guard.LAYER_LOCATION, Modelface_guard::createBodyLayer);
	}
}
