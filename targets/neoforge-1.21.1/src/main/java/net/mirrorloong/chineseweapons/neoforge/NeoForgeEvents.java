package net.mirrorloong.chineseweapons.neoforge;

import java.util.Random;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.item.ReachSwordItem;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

public final class NeoForgeEvents {
    private static final Random RANDOM = new Random();

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
    public static void onAttributeModifiers(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof ReachSwordItem reach) {
            event.addModifier(Attributes.ENTITY_INTERACTION_RANGE,
                    new AttributeModifier(ReachSwordItem.ENTITY_REACH_MODIFIER_ID, reach.getReachBonus(),
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
        }
    }
}
