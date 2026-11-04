package net.mirrorloong.chineseweapons.item;

import java.util.List;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.mirrorloong.chineseweapons.fabric.client.ArmorModelProvider;
import net.mirrorloong.chineseweapons.fabric.client.ArmorTextureProvider;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.spi.ArmorItemLike;

/**
 * Generic armour item backed by an {@link ArmorDefinition}. Replaces the
 * per-material item classes; the item keeps its definition so gameplay code can
 * read the material data instead of testing concrete classes.
 */
public class FabricArmorItem extends ArmorItem
        implements ArmorItemLike, ArmorModelProvider, ArmorTextureProvider {
    private static final String FACE_GUARD_MODEL = "face_guard";
    private static final String FOOTMEN_MODEL = "footmen_armor";

    private final ArmorDefinition definition;
    private final ArmorSlot slot;

    public FabricArmorItem(ArmorDefinition definition, ArmorSlot slot) {
        super(material(definition), slotType(slot), properties(definition, slot));
        this.definition = definition;
        this.slot = slot;
    }

    @Override
    public ArmorDefinition definition() {
        return definition;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (!definition.overlayTexture().isEmpty()) {
            DyeableItem.addDyeTooltip(stack, tooltip);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        super.inventoryTick(stack, level, entity, slotId, selected);
        if (slot != ArmorSlot.HELMET || !(entity instanceof Player player) || !isWorn(player, stack)) {
            return;
        }
        if (FACE_GUARD_MODEL.equals(definition.modelId())) {
            FaceGuardHelmetKHProcedures.execute(level, entity, stack);
        } else if (FOOTMEN_MODEL.equals(definition.modelId())) {
            FootmenarmorcompletetestingProcedure.execute(entity);
        }
    }

    private static boolean isWorn(Player player, ItemStack stack) {
        for (ItemStack armor : player.getArmorSlots()) {
            if (armor == stack) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HumanoidModel createArmorModel(LivingEntity living, HumanoidModel defaultModel) {
        return FabricArmorModels.create(definition.modelId(), slot, living, defaultModel);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot equipmentSlot,
                                            ArmorMaterial.Layer layer, boolean innerModel) {
        if (definition.overlayTexture().isEmpty()) {
            return ResourceLocation.parse(definition.baseTexture());
        }
        return ResourceLocation.parse(DyeableItem.getArmorTexture(stack, innerModel ? "layer_2" : "layer_1",
                definition.baseTexture(), definition.overlayTexture()));
    }

    // Forge/NeoForge items expose these hooks; vanilla 1.21.1 does not, so on
    // Fabric the flags are carried by the definition but not consulted by the game.
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return definition.piglinNeutral();
    }

    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return definition.snowWalk() && slot == ArmorSlot.BOOTS;
    }

    private static Item.Properties properties(ArmorDefinition definition, ArmorSlot slot) {
        Item.Properties properties = definition.fireResistant()
                ? new Item.Properties().fireResistant()
                : new Item.Properties();
        return properties.durability(slotType(slot).getDurability(definition.durabilityMultiplier()));
    }

    private static ArmorItem.Type slotType(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> ArmorItem.Type.HELMET;
            case CHESTPLATE -> ArmorItem.Type.CHESTPLATE;
            case LEGGINGS -> ArmorItem.Type.LEGGINGS;
            case BOOTS -> ArmorItem.Type.BOOTS;
        };
    }

    private static Holder<ArmorMaterial> material(ArmorDefinition definition) {
        return CustomArmorMaterials.create(definition.materialName(), definition.durabilityMultiplier(),
                new int[]{definition.feetDefense(), definition.legsDefense(), definition.chestDefense(),
                        definition.helmetDefense()},
                definition.enchantmentValue(), equipSound(definition.equipSound()),
                () -> Ingredient.of(new ItemStack(definition.repairItem())),
                definition.toughness(), definition.knockbackResistance());
    }

    private static Holder<SoundEvent> equipSound(ArmorEquipSound sound) {
        return switch (sound) {
            case IRON -> SoundEvents.ARMOR_EQUIP_IRON;
            case GOLD -> SoundEvents.ARMOR_EQUIP_GOLD;
            case DIAMOND -> SoundEvents.ARMOR_EQUIP_DIAMOND;
            case NETHERITE -> SoundEvents.ARMOR_EQUIP_NETHERITE;
        };
    }
}
