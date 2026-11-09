package net.mirrorloong.chineseweapons.fabric;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.procedures.TangDynastyHorseJianProcedure;
import net.mirrorloong.chineseweapons.procedures.armor.ChineseWeaponsCombatEvents;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItemCleanColor;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHKeepFarProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.procedures.tips;
import net.mirrorloong.chineseweapons.procedures.weapon.GlaiveItemGetLostKillProcedure;
import net.mirrorloong.chineseweapons.procedures.weapon.TangDynastyHengSabeCutsProcedures;

/**
 * Wires the shared gameplay logic into Fabric API events. Everything the common
 * sources previously expressed as Forge {@code @EventBusSubscriber} handlers is
 * routed from here instead.
 */
public final class FabricEvents {
    private FabricEvents() {
    }

    public static void register() {
        registerWorldGen();
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(FabricEvents::allowDamage);
        ServerTickEvents.END_SERVER_TICK.register(FabricEvents::endServerTick);
        ServerTickEvents.END_WORLD_TICK.register(FabricEvents::endWorldTick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> tips.onEnterGame(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> GlaiveItemGetLostKillProcedure.onPlayerLogout(handler.player));
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) ->
                DyeableItemCleanColor.onRightClickBlock(player, level, hitResult.getBlockPos(),
                        player.getItemInHand(hand)));
        UseItemCallback.EVENT.register((player, level, hand) -> {
            InteractionResultHolder<ItemStack> result =
                    TangDynastyHengSabeCutsProcedures.rightClickTrigger(player, hand);
            return result;
        });
    }

    private static void registerWorldGen() {
        // The shared data pack hooks ore generation through Forge biome modifiers,
        // which Fabric does not read; add the same two placed features here.
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ChineseWeapons.MOD_ID, "ancient_gem_ore")));
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ChineseWeapons.MOD_ID, "deepslate_ancient_gem_ore")));
    }

    private static boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        if (!ChineseWeaponsCombatEvents.allowDamage(entity, source, amount)) {
            return false;
        }
        if (!FootmenarmorcompletetestingProcedure.allowDamage(entity, source)) {
            return false;
        }
        return TangDynastyHorseJianProcedure.allowDamage(entity, source);
    }

    private static void endServerTick(net.minecraft.server.MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ChineseWeaponsCombatEvents.playerTick(player);
            TangDynastyHengSabeCutsProcedures.tickUpdate(player);
            GlaiveItemGetLostKillProcedure.playerTick(player);
        }
    }

    private static void endWorldTick(ServerLevel level) {
        ChineseWeaponsCombatEvents.tickTethered(level);
        FaceGuardHelmetKHKeepFarProcedures.tick();
    }
}
