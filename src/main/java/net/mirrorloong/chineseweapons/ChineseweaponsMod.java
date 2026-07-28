package net.mirrorloong.chineseweapons;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.init.*;
import net.mirrorloong.chineseweapons.item.*;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(ChineseweaponsMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE,modid = ChineseweaponsMod.MODID,value = Dist.DEDICATED_SERVER)
public class ChineseweaponsMod {
	public static final Logger LOGGER = LogManager.getLogger(ChineseweaponsMod.class);
	public static final String MODID = "chineseweapons";

	@SubscribeEvent
	public static void onRecipesUpdated(RecipesUpdatedEvent event){
		WeaponsCastingTableTypeGuiMenu.recipeManager=event.getRecipeManager();
	}

	public ChineseweaponsMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		DeferredRecipe.DeferredRecipeSer.register(bus);
		DeferredRecipe.DeferredRecipe.register(bus);
		DeferredGui.Deferred.register(bus);
		ChineseweaponsModBlocks.REGISTRY.register(bus);
		ChineseweaponsModItems.REGISTRY.register(bus);
		ChineseweaponsModTabs.REGISTRY.register(bus);
		ChineseWeaponsModEffects.REGISTER.register(bus);
        ChineseWeaponsModEnchantments.REGISTRY.register(bus);
	}


	private static final Random random = new Random();
	@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID,bus= Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEvent{
		@SubscribeEvent
		public static void onLivingHurt(LivingHurtEvent event){
			if( event.getEntity().getVehicle() !=null){
				DamageSource source = event.getSource();
				if(source.getEntity() instanceof Player player){
					Item item = player.getMainHandItem().getItem();
					if(item instanceof DiamondglaiveItem || item instanceof GoldenglaiveItem
					|| item instanceof IronglaiveItem || item instanceof NetheriteglaiveItem
					|| item instanceof GreenloongglaiveItem
					|| item instanceof WoodenglaiveItem ||item instanceof StoneglaiveItem ){
						int gai_lv = random.nextInt(99)+1;
						if(gai_lv<=60){
							event.getEntity().stopRiding();
						}
					}
				}
			}
		}
	}
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}
	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
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

}
