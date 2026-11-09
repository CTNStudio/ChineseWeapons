package net.mirrorloong.chineseweapons.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.mirrorloong.chineseweapons.client.model.NeoForgeModelFactory;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpec;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;

public class ChineseweaponsModModels {
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		NeoForgeModelFactory factory = NeoForgeModelFactory.INSTANCE;
		for (ArmorModelSpec armour : ArmorModelSpecs.all()) {
			event.registerLayerDefinition(factory.location(armour.model()), () -> factory.layer(armour.model()));
		}
		for (ModelSpec entity : EntityModelSpecs.all()) {
			event.registerLayerDefinition(factory.location(entity), () -> factory.layer(entity));
		}
	}
}
