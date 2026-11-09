package net.mirrorloong.chineseweapons.neoforge;

import java.util.Random;

import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.item.ReachSwordItem;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

public final class NeoForgeEvents {
    private static final Random RANDOM = new Random();
    private static final Identifier ENTITY_REACH_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(ChineseWeapons.MOD_ID, "weapon_entity_reach");

    private NeoForgeEvents() {
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity().getVehicle() == null) {
            return;
        }
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }
        Item item = player.getMainHandItem().getItem();
        if (!(item instanceof GlaiveItem)) {
            return;
        }
        float chance = WeaponScriptSettings.getChance(item, WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, 0.60F);
        if (RANDOM.nextFloat() < chance) {
            event.getEntity().stopRiding();
        }
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        // 26.1 clients no longer receive the recipe registry. Request our recipe
        // type so client-side recipe viewers can display it, plus the vanilla
        // types JEI registers from the same sync (JEI adds these itself when it
        // runs on the server; the request set is deduplicated).
        event.sendRecipes(
                RecipeType.CRAFTING,
                RecipeType.STONECUTTING,
                RecipeType.SMELTING,
                RecipeType.SMOKING,
                RecipeType.BLASTING,
                RecipeType.CAMPFIRE_COOKING,
                RecipeType.SMITHING,
                DeferredRecipe.WeaponCastingShapedType.get());
    }

    @SubscribeEvent
    public static void onAttributeModifiers(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof ReachSwordItem reach) {
            event.addModifier(Attributes.ENTITY_INTERACTION_RANGE,
                    new AttributeModifier(ENTITY_REACH_MODIFIER_ID, reach.getReachBonus(),
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
        }
    }
}
