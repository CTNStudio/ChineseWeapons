package net.mirrorloong.chineseweapons.item;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.spi.ArmorItemLike;

/**
 * Generic 26.1 armour item driven by {@link ArmorDefinition}. 26.1 has no
 * {@code ArmorItem}: the item carries the material and slot in its properties,
 * and the worn geometry comes from the set's equipment asset instead of a
 * client model hook.
 */
public class FabricArmorItem extends Item implements DyeableItem, ArmorItemLike {
    private final ArmorDefinition definition;
    private final ArmorSlot slot;

    public FabricArmorItem(ArmorDefinition definition, ArmorSlot slot) {
        super(properties(definition).humanoidArmor(ArmorSupport.armor(definition), ArmorSupport.type(slot)));
        this.definition = definition;
        this.slot = slot;
    }

    @Override
    public ArmorDefinition definition() {
        return definition;
    }

    @Override
    public boolean isDyeable() {
        return definition.dyeable();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (!definition.overlayTexture().isEmpty()) {
            DyeableItem.addDyeTooltip(stack, tooltip);
        }
    }

    /**
     * The footmen helmets keep the set-completion check that used to live on
     * each per-material helmet class.
     */
    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot equipmentSlot) {
        super.inventoryTick(stack, level, entity, equipmentSlot);
        if (slot == ArmorSlot.HELMET && "footmen_armor".equals(definition.modelId())
                && entity instanceof Player player && ArmorSupport.isWorn(player, stack)) {
            FootmenarmorcompletetestingProcedure.execute(player);
        }
    }

    private static Item.Properties properties(ArmorDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }
}
