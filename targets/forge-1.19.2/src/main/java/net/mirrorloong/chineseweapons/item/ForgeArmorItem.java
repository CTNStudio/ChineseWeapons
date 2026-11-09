package net.mirrorloong.chineseweapons.item;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;

import net.minecraft.client.model.HumanoidModel;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorEquipSound;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;
import net.mirrorloong.chineseweapons.procedures.armor.FootmenarmorcompletetestingProcedure;
import net.mirrorloong.chineseweapons.spi.ArmorItemLike;

public class ForgeArmorItem extends ArmorItem implements DyeableItem, ArmorItemLike {
    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    private final ArmorDefinition definition;
    private final ArmorSlot slot;

    public ForgeArmorItem(ArmorDefinition definition, ArmorSlot slot) {
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (definition.overlayTexture().isEmpty()) {
            return definition.baseTexture();
        }
        return DyeableItem.getArmorTexture(stack, type, definition.baseTexture(), definition.overlayTexture());
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return definition.piglinNeutral();
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return definition.snowWalk() && slot == ArmorSlot.BOOTS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slotId, boolean selected) {
        super.inventoryTick(stack, world, entity, slotId, selected);
        if (this.slot != ArmorSlot.HELMET || !(entity instanceof Player player)) {
            return;
        }
        if (!Iterables.contains(player.getArmorSlots(), stack)) {
            return;
        }
        if ("footmen_armor".equals(definition.modelId())) {
            FootmenarmorcompletetestingProcedure.execute(entity);
        } else if ("face_guard".equals(definition.modelId())) {
            FaceGuardHelmetKHProcedures.execute(world, entity, stack);
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack,
                                                          EquipmentSlot equipmentSlot,
                                                          HumanoidModel<?> defaultModel) {
                return ArmorModels.create(definition.modelId(), slot, living, defaultModel);
            }
        });
    }

    private static Item.Properties properties(ArmorDefinition definition) {
        Item.Properties properties = new Item.Properties();
        return definition.fireResistant() ? properties.fireResistant() : properties;
    }

    private static EquipmentSlot slotType(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> EquipmentSlot.HEAD;
            case CHESTPLATE -> EquipmentSlot.CHEST;
            case LEGGINGS -> EquipmentSlot.LEGS;
            case BOOTS -> EquipmentSlot.FEET;
        };
    }

    private static ArmorMaterial material(ArmorDefinition definition) {
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(EquipmentSlot type) {
                return BASE_DURABILITY[type.getIndex()] * definition.durabilityMultiplier();
            }

            @Override
            public int getDefenseForSlot(EquipmentSlot type) {
                return switch (type) {
                    case HEAD -> definition.helmetDefense();
                    case CHEST -> definition.chestDefense();
                    case LEGS -> definition.legsDefense();
                    case FEET -> definition.feetDefense();
                    default -> 0;
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
