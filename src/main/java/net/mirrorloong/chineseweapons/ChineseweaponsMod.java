package net.mirrorloong.chineseweapons;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.init.*;
import net.mirrorloong.chineseweapons.item.*;
import net.mirrorloong.chineseweapons.network.ArmorCastingTableBlockProgressPacket;
import net.mirrorloong.chineseweapons.network.ShieldActionPacket;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.DyeableArmorRecipe;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(ChineseweaponsMod.MODID)
public class ChineseweaponsMod {
    public static final Logger LOGGER = LogManager.getLogger(ChineseweaponsMod.class);
    public static final String MODID = "chineseweapons";

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ChineseweaponsMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyeableArmorRecipe>> DYEABLE_ARMOR_RECIPE =
            RECIPE_SERIALIZERS.register("crafting_special_dyeablearmor",
                    () -> DyeableArmorRecipe.SERIALIZER);

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, ChineseweaponsMod.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GUDUO =
            SOUNDS.register("guduo", () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "guduo")
            ));

    public ChineseweaponsMod(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);

        DeferredRecipe.DeferredRecipeSer.register(modEventBus);
        DeferredRecipe.DeferredRecipe.register(modEventBus);
        DeferredGui.Deferred.register(modEventBus);
        ChineseweaponsModBlocks.REGISTRY.register(modEventBus);
        ChineseweaponsModItems.REGISTRY.register(modEventBus);
        ChineseweaponsModTabs.REGISTRY.register(modEventBus);
        ChineseWeaponsModEffects.REGISTER.register(modEventBus);
        ChineseWeaponsModEnchantments.REGISTRY.register(modEventBus);
        ChineseweaponsModEntities.ENTITIES.register(modEventBus);
        SOUNDS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        ChineseweaponsModBlockEntities.REGISTRY.register(modEventBus);
        ChineseWeaponsModHorseArmorMaterials.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::registerKeys);
    }

    public void registerKeys(RegisterKeyMappingsEvent e) {
        if (!FMLEnvironment.dist.isClient()) return;
        e.register(ChineseweaponsModKeyHandler.ACTION_KEY);
    }

    public void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                GuDuoItem.SquashPacket.TYPE,
                GuDuoItem.SquashPacket.STREAM_CODEC,
                GuDuoItem.SquashPacket::handle
        );
        registrar.playBidirectional(
                ShieldActionPacket.TYPE,
                ShieldActionPacket.STREAM_CODEC,
                ShieldActionPacket::handle
        );
        registrar.playBidirectional(
                ArmorCastingTableBlockProgressPacket.TYPE,
                ArmorCastingTableBlockProgressPacket.STREAM_CODEC,
                ArmorCastingTableBlockProgressPacket::handle
        );
    }

    private static final Random random = new Random();

    @EventBusSubscriber(modid = ChineseweaponsMod.MODID)
    public static class ForgeEvent {
        @SubscribeEvent
        public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
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

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue =
            new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {
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