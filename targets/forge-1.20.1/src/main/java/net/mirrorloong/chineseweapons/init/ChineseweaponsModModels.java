package net.mirrorloong.chineseweapons.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.client.model.ForgeModelFactory;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpec;
import net.mirrorloong.chineseweapons.client.spec.ArmorModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.EntityModelSpecs;
import net.mirrorloong.chineseweapons.client.spec.ModelSpec;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChineseweaponsModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ForgeModelFactory factory = ForgeModelFactory.INSTANCE;
		for (ArmorModelSpec armour : ArmorModelSpecs.all()) {
			event.registerLayerDefinition(factory.location(armour.model()), () -> factory.layer(armour.model()));
		}
		for (ModelSpec entity : EntityModelSpecs.all()) {
			event.registerLayerDefinition(factory.location(entity), () -> factory.layer(entity));
		}
	}
}
