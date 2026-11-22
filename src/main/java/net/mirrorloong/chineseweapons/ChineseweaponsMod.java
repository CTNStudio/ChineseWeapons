package net.mirrorloong.chineseweapons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
//old api DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID)
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.init.*;
import net.mirrorloong.chineseweapons.item.GlaiveItem;
import net.mirrorloong.chineseweapons.network.NetworkHandler;
import net.mirrorloong.chineseweapons.procedures.TangDynastyHorseJianProcedure;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItemCleanColor;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHKeepFarProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.procedures.tips;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemGetLostKillProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.TangDynastyHengSabeCutsProcedures;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.DyeableArmorRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChineseweaponsMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger(ChineseweaponsMod.class);
	public static final String MODID = "chineseweapons";

	//old api DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID) -> new api DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID)
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
			DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChineseweaponsMod.MODID);

	public static final RegistryObject<RecipeSerializer<DyeableArmorRecipe>> DYEABLE_ARMOR_RECIPE =
			RECIPE_SERIALIZERS.register("crafting_special_dyeablearmor",
					() -> DyeableArmorRecipe.SERIALIZER);

	public static final DeferredRegister<SoundEvent> SOUNDS =
			DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ChineseweaponsMod.MODID);

	public static final RegistryObject<SoundEvent> GUDUO =
			SOUNDS.register("guduo", () -> SoundEvent.createVariableRangeEvent(
					new ResourceLocation(ChineseweaponsMod.MODID, "guduo")
			));

	private static final Random random = new Random();

	@Override
	public void onInitialize() {
                ChineseweaponsModBlocks.bootstrap();
		ChineseweaponsModItems.bootstrap();
		ChineseWeaponsModEffects.bootstrap();
		ChineseWeaponsModEnchantments.bootstrap();
		ChineseweaponsModEntities.bootstrap();
		ChineseweaponsModBlockEntities.bootstrap();
		ChineseweaponsModTabs.bootstrap();
		DeferredRecipe.bootstrap();
		DeferredGui.bootstrap();

		DeferredRegister.registerAll();
		//old api bus.addListener(ChineseweaponsMod::network) + SimpleChannel.registerMessage -> new api Fabric Networking API v1
		NetworkHandler.initCommon();

				ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			ForgeEvent.onLivingHurt(entity, source);
			return true;
		});

				ServerTickEvents.END_SERVER_TICK.register(this::tick);
				ChineseweaponsModWorldgen.init();

				ChineseWeaponsCombatEvents.init();
		TangDynastyHorseJianProcedure.init();
		tips.init();
		DyeableItemCleanColor.init();
		FaceGuardHelmetKHKeepFarProcedures.init();
		FootmenarmorcompletetestingProcedure.init();
		GlaiveItemGetLostKillProcedure.init();
		TangDynastyHengSabeCutsProcedures.init();
	}

		public static class ForgeEvent {
		public static void onLivingHurt(LivingEntity entity, DamageSource source) {
			if (entity.getVehicle() != null) {
				if (source.getEntity() instanceof Player player) {
					Item item = player.getMainHandItem().getItem();
					if (item instanceof GlaiveItem) {
						float chance = WeaponScriptSettings.getChance(item, WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, 0.60F);
						if (random.nextFloat() < chance) {
							entity.stopRiding();
						}
					}
				}
			}
		}
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();
	private static MinecraftServer currentServer;

		public static void queueServerWork(int tick, Runnable action) {
		if (currentServer != null && currentServer.isSameThread())
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	//old api @SubscribeEvent tick(TickEvent.ServerTickEvent) + event.phase == END -> new api ServerTickEvents.END_SERVER_TICK
	public void tick(MinecraftServer server) {
		currentServer = server;
		List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setValue(work.getValue() - 1);
			if (work.getValue() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getKey().run());
		workQueue.removeAll(actions);
	}
}
