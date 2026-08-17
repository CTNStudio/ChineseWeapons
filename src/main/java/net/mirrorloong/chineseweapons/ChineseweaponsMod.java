package net.mirrorloong.chineseweapons;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEffects;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModEnchantments;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsModHorseArmorMaterials;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModTabs;
import net.mirrorloong.chineseweapons.item.DiamondglaiveItem;
import net.mirrorloong.chineseweapons.item.GoldenglaiveItem;
import net.mirrorloong.chineseweapons.item.GreenloongglaiveItem;
import net.mirrorloong.chineseweapons.item.IronglaiveItem;
import net.mirrorloong.chineseweapons.item.NetheriteglaiveItem;
import net.mirrorloong.chineseweapons.item.StoneglaiveItem;
import net.mirrorloong.chineseweapons.item.WoodenglaiveItem;
import net.mirrorloong.chineseweapons.network.ArmorcastingtableguiSlotMessage;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.DyeableArmorRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(ChineseweaponsMod.MODID)
public class ChineseweaponsMod {
	public static final Logger LOGGER = LoggerFactory.getLogger(ChineseweaponsMod.class);
	public static final String MODID = "chineseweapons";

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChineseweaponsMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyeableArmorRecipe>> DYEABLE_ARMOR_RECIPE =
            RECIPE_SERIALIZERS.register("crafting_special_dyeablearmor",
                    () -> DyeableArmorRecipe.SERIALIZER);

    public ChineseweaponsMod(IEventBus modEventBus, ModContainer modContainer) {
        DeferredRecipe.DeferredRecipeSer.register(modEventBus);
        DeferredRecipe.DeferredRecipe.register(modEventBus);
        DeferredGui.Deferred.register(modEventBus);
        ChineseweaponsModBlocks.REGISTRY.register(modEventBus);
        ChineseweaponsModItems.REGISTRY.register(modEventBus);
        ChineseweaponsModTabs.REGISTRY.register(modEventBus);
        ChineseWeaponsModEffects.REGISTER.register(modEventBus);
        ChineseWeaponsModEnchantments.REGISTRY.register(modEventBus);
        ChineseWeaponsModHorseArmorMaterials.REGISTRY.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(ChineseweaponsMod::registerPayloads);
        NeoForge.EVENT_BUS.register(this);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID).versioned("1").optional();
        registrar.playToServer(ArmorcastingtableguiSlotMessage.TYPE,
                ArmorcastingtableguiSlotMessage.STREAM_CODEC,
                ArmorcastingtableguiSlotMessage::handle);
    }

	private static final Random random = new Random();
	@EventBusSubscriber(modid = ChineseweaponsMod.MODID, bus = EventBusSubscriber.Bus.GAME)
	public static class ForgeEvent {
		@SubscribeEvent
		public static void onLivingHurt(LivingDamageEvent.Pre event) {
			if (event.getEntity().getVehicle() != null) {
				DamageSource source = event.getSource();
				if (source.getEntity() instanceof Player player) {
					Item item = player.getMainHandItem().getItem();
					if (item instanceof DiamondglaiveItem || item instanceof GoldenglaiveItem
							|| item instanceof IronglaiveItem || item instanceof NetheriteglaiveItem
							|| item instanceof GreenloongglaiveItem
							|| item instanceof WoodenglaiveItem || item instanceof StoneglaiveItem) {
						float chance = WeaponScriptSettings.getChance(item, WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, 0.60F);
						if (random.nextFloat() < chance) {
							event.getEntity().stopRiding();
						}
					}
				}
			}
		}
	}
	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setValue(work.getValue() - 1);
			if (work.getValue() == 0) {
				actions.add(work);
			}
		});
		actions.forEach(e -> e.getKey().run());
		workQueue.removeAll(actions);
	}
}
