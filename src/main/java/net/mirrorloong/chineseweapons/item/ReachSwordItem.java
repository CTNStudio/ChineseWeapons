package net.mirrorloong.chineseweapons.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

public abstract class ReachSwordItem extends SwordItem {
    private static final ResourceLocation ENTITY_REACH_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("chineseweapons", "weapon_entity_reach");

    private final double entityReachBonus;

    protected ReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                             Item.Properties properties, double entityReachBonus) {
        super(tier, properties.attributes(
                SwordItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)
        ));
        this.entityReachBonus = entityReachBonus;
    }

    public double getEntityReachBonus() {
        return this.entityReachBonus;
    }

    static {
        NeoForge.EVENT_BUS.addListener(ReachSwordItem::onItemAttributeModifier);
    }

    private static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        if (!(event.getItemStack().getItem() instanceof ReachSwordItem reachSword)) {
            return;
        }

        double bonus = WeaponScriptSettings.getReachBonus(
                reachSword, reachSword.getEntityReachBonus());

        event.addModifier(
                Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(
                        ENTITY_REACH_MODIFIER_ID,
                        bonus,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
    }
}

abstract class FourBlockReachSwordItem extends ReachSwordItem {
    protected FourBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                                      Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }
}

abstract class SixBlockReachSwordItem extends ReachSwordItem {
    protected SixBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                                     Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }

    public abstract boolean canPerformSweepAttack(ItemStack stack);
}

abstract class FiveBlockReachSwordItem extends ReachSwordItem {
    protected FiveBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                                      Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 2.0D);
    }
}