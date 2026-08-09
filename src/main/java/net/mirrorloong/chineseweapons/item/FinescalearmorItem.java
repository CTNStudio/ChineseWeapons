package net.mirrorloong.chineseweapons.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.client.model.Modelfine_scale_armor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FinescalearmorItem extends ArmorItem implements DyeableItem {
	private final MaterialVariant materialVariant;
    private final String TEXTURE_BASE;
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/fine_scale_armor_color.png";

    public FinescalearmorItem(MaterialVariant materialVariant, ArmorItem.Type type) {
		super(materialVariant, type, materialVariant == MaterialVariant.NETHERITE ? new Item.Properties().fireResistant() : new Item.Properties());
        this.TEXTURE_BASE = "chineseweapons:textures/entities/" + materialVariant.textureName;
		this.materialVariant = materialVariant;
	}

	private static ModelPart emptyPart() {
		return new ModelPart(Collections.emptyList(), Collections.emptyMap());
	}

	private static HumanoidModel createArmorModel(LivingEntity living, HumanoidModel defaultModel, ArmorItem.Type armorType) {
		Modelfine_scale_armor model = new Modelfine_scale_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelfine_scale_armor.LAYER_LOCATION));
		Map<String, ModelPart> parts = switch (armorType) {
			case HELMET ->
					Map.of("head", model.bipedHead, "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart(), "right_leg", emptyPart(), "left_leg", emptyPart());
			case CHESTPLATE ->
					Map.of("body", model.bipedBody, "left_arm", model.bipedLeftArm, "right_arm", model.bipedRightArm, "head", emptyPart(), "hat", emptyPart(), "right_leg", emptyPart(), "left_leg", emptyPart());
			case LEGGINGS ->
					Map.of("left_leg", model.bipedLeftLeg, "right_leg", model.bipedRightLeg, "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart());
			case BOOTS ->
					Map.of("left_leg", model.LeftBoots, "right_leg", model.RightBoots, "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart());
		};
		HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), parts));
		armorModel.crouching = living.isShiftKeyDown();
		armorModel.riding = defaultModel.riding;
		armorModel.young = living.isBaby();
		return armorModel;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				return createArmorModel(living, defaultModel, getType());
			}
		});
	}

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return DyeableItem.getArmorTexture(stack, type, TEXTURE_BASE, TEXTURE_OVERLAY);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DyeableItem.addDyeTooltip(stack, tooltip);
    }
    @Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return materialVariant == MaterialVariant.GOLDEN;
	}

	public enum MaterialVariant implements ArmorMaterial {
		IRON(16, new int[]{3, 6, 7, 3}, 10, 0f, 0f, Items.IRON_INGOT, "iron_fine_scale_armor", "iron_fine_scale_armor.png"),
		GOLDEN(15, new int[]{2, 4, 6, 3}, 26, 1f, 0f, Items.GOLD_INGOT, "golden_fine_scale_armor", "golden_fine_scale_armor.png"),
		DIAMOND(34, new int[]{2, 7, 9, 4}, 11, 2f, 0f, Items.DIAMOND, "diamond_fine_scale_armor", "diamond_fine_scale_armor.png"),
		NETHERITE(38, new int[]{2, 7, 9, 4}, 16, 3f, 0.1f, Items.NETHERITE_INGOT, "netherite_fine_scale_armor", "netherite_fine_scale_armor.png");

		private final int durabilityMultiplier;
		private final int[] defenseValues;
		private final int enchantmentValue;
		private final float toughness;
		private final float knockbackResistance;
		private final Item repairItem;
		private final String materialName;
		private final String textureName;

		MaterialVariant(int durabilityMultiplier, int[] defenseValues, int enchantmentValue, float toughness, float knockbackResistance, Item repairItem, String materialName, String textureName) {
			this.durabilityMultiplier = durabilityMultiplier;
			this.defenseValues = defenseValues;
			this.enchantmentValue = enchantmentValue;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairItem = repairItem;
			this.materialName = materialName;
			this.textureName = textureName;
		}

		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * durabilityMultiplier;
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return defenseValues[type.getSlot().getIndex()];
		}

		@Override
		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		@Override
		public SoundEvent getEquipSound() {
			// 按材质返回对应原版穿戴音效
			return switch (this) {
				case IRON -> SoundEvents.ARMOR_EQUIP_IRON;
				case GOLDEN -> SoundEvents.ARMOR_EQUIP_GOLD;
				case DIAMOND -> SoundEvents.ARMOR_EQUIP_DIAMOND;
				case NETHERITE -> SoundEvents.ARMOR_EQUIP_NETHERITE;
			};
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(repairItem);
		}

		@Override
		public String getName() {
			return materialName;
		}

		@Override
		public float getToughness() {
			return toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return knockbackResistance;
		}
	}
}
