package net.mirrorloong.chineseweapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;

import java.util.UUID;

public abstract class ReachSwordItem extends SwordItem {
    private static final UUID ENTITY_REACH_MODIFIER_ID = UUID.fromString("a6796eb5-9d73-4f19-b846-a2e1efc874f2");

    private final double entityReachBonus;

    protected ReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties, double entityReachBonus) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.entityReachBonus = entityReachBonus;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot != EquipmentSlot.MAINHAND) {
            return super.getDefaultAttributeModifiers(equipmentSlot);
        }

        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(equipmentSlot))
                .put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(ENTITY_REACH_MODIFIER_ID, "Weapon entity reach", WeaponScriptSettings.getReachBonus(this, this.entityReachBonus), AttributeModifier.Operation.ADDITION))
                .build();
    }
}

abstract class FourBlockReachSwordItem extends ReachSwordItem {
    protected FourBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 1.0D);
    }
}

abstract class FiveBlockReachSwordItem extends ReachSwordItem {
    protected FiveBlockReachSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, 2.0D);
    }
}
