package net.mirrorloong.chineseweapons.item;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.spi.ArmorItemLike;

/**
 * Single armour item implementation for the data-driven definitions. 26.1
 * renders armour through equipment assets, so the base/overlay textures of
 * {@link ArmorDefinition} are consumed by the resource pack rather than here.
 */
public class NeoForgeArmorItem extends Item implements DyeableItem, ArmorItemLike {
    private final ArmorDefinition definition;
    private final ArmorSlot slot;

    public NeoForgeArmorItem(ArmorDefinition definition, ArmorSlot slot) {
        super(properties(definition, slot));
        this.definition = definition;
        this.slot = slot;
    }

    @Override
    public ArmorDefinition definition() {
        return definition;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (!definition.overlayTexture().isEmpty()) {
            DyeableItem.addDyeTooltip(stack, tooltip);
        }
    }

    public boolean isDyeable() {
        return definition.dyeable();
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot equipmentSlot) {
        super.inventoryTick(stack, level, entity, equipmentSlot);
        if (!(entity instanceof Player player)) {
            return;
        }
        boolean head = player.getItemBySlot(EquipmentSlot.HEAD) == stack;
        boolean equipped = head
                || player.getItemBySlot(EquipmentSlot.CHEST) == stack
                || player.getItemBySlot(EquipmentSlot.LEGS) == stack
                || player.getItemBySlot(EquipmentSlot.FEET) == stack;
        if (!equipped) {
            return;
        }
        switch (definition.modelId()) {
            case "footmen_armor" -> FootmenarmorcompletetestingProcedure.execute(entity);
            case "face_guard" -> {
                if (head) {
                    FaceGuardHelmetKHProcedures.execute(level, entity, stack);
                }
                FootmenarmorcompletetestingProcedure.execute(entity);
            }
            default -> {
            }
        }
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return definition.piglinNeutral();
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return definition.snowWalk() && slot == ArmorSlot.BOOTS;
    }

    private static Item.Properties properties(ArmorDefinition definition, ArmorSlot slot) {
        Item.Properties properties = definition.fireResistant()
                ? new Item.Properties().fireResistant()
                : new Item.Properties();
        return ModArmorMaterials.apply(properties, definition, type(slot));
    }

    private static ArmorType type(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> ArmorType.HELMET;
            case CHESTPLATE -> ArmorType.CHESTPLATE;
            case LEGGINGS -> ArmorType.LEGGINGS;
            case BOOTS -> ArmorType.BOOTS;
        };
    }
}
