package net.mirrorloong.chineseweapons.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorEquipSound;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.spi.ArmorItemLike;

/**
 * Generic armour item backed by an {@link ArmorDefinition}. Replaces the
 * deleted per-material item classes; the renderer hook is Fabric's
 * {@link net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer}, registered
 * by {@code FabricArmorRenderers} for every item implementing
 * {@link ArmorModelProvider}.
 */
public class FabricArmorItem extends ArmorItem
        implements DyeableItem, ArmorItemLike {
    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private static final String FACE_GUARD_MODEL = "face_guard";
    private static final String FOOTMEN_MODEL = "footmen_armor";

    private final ArmorDefinition definition;
    private final ArmorSlot slot;

    public FabricArmorItem(ArmorDefinition definition, ArmorSlot slot) {
        super(material(definition), slotType(slot), properties(definition));
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
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (!definition.overlayTexture().isEmpty()) {
            DyeableItem.addDyeTooltip(stack, tooltip);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        super.inventoryTick(stack, level, entity, slotId, selected);
        if (slot != ArmorSlot.HELMET || !(entity instanceof Player player)
                || !Iterables.contains(player.getArmorSlots(), stack)) {
            return;
        }
        if (FACE_GUARD_MODEL.equals(definition.modelId())) {
            FaceGuardHelmetKHProcedures.execute(level, entity, stack);
        } else if (FOOTMEN_MODEL.equals(definition.modelId())) {
            FootmenarmorcompletetestingProcedure.execute(entity);
        }
    }

    // Forge's IForgeItem hooks for these two flags do not exist in vanilla
    // 1.20.1; PiglinAiMixin/PowderSnowBlockMixin consult these methods so the
    // flags still take effect on Fabric.
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return definition.piglinNeutral();
    }

    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return definition.snowWalk() && slot == ArmorSlot.BOOTS;
    }

    private static Item.Properties properties(ArmorDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }

    private static Type slotType(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> Type.HELMET;
            case CHESTPLATE -> Type.CHESTPLATE;
            case LEGGINGS -> Type.LEGGINGS;
            case BOOTS -> Type.BOOTS;
        };
    }

    private static ArmorMaterial material(ArmorDefinition definition) {
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(Type type) {
                return BASE_DURABILITY[type.getSlot().getIndex()] * definition.durabilityMultiplier();
            }

            @Override
            public int getDefenseForType(Type type) {
                return switch (type) {
                    case HELMET -> definition.helmetDefense();
                    case CHESTPLATE -> definition.chestDefense();
                    case LEGGINGS -> definition.legsDefense();
                    case BOOTS -> definition.feetDefense();
                };
            }

            @Override
            public int getEnchantmentValue() {
                return definition.enchantmentValue();
            }

            @Override
            public SoundEvent getEquipSound() {
                return switch (definition.equipSound()) {
                    case IRON -> SoundEvents.ARMOR_EQUIP_IRON;
                    case GOLD -> SoundEvents.ARMOR_EQUIP_GOLD;
                    case DIAMOND -> SoundEvents.ARMOR_EQUIP_DIAMOND;
                    case NETHERITE -> SoundEvents.ARMOR_EQUIP_NETHERITE;
                };
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(definition.repairItem()));
            }

            @Override
            public String getName() {
                return definition.materialName();
            }

            @Override
            public float getToughness() {
                return definition.toughness();
            }

            @Override
            public float getKnockbackResistance() {
                return definition.knockbackResistance();
            }
        };
    }
}
