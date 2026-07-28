package net.mirrorloong.chineseweapons.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.mirrorloong.chineseweapons.client.model.Modeltang_dynasty_infantry_armor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TangdynastyinfantryarmorItem extends ArmorItem {
	public TangdynastyinfantryarmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 16;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{3, 6, 7, 3}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 10;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_IRON;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.IRON_INGOT));
			}

			@Override
			public String getName() {
				return "tang_dynasty_infantry_armor";
			}

			@Override
			public float getToughness() {
				return 0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, type, properties);
	}

	private static ModelPart emptyPart() {
		return new ModelPart(Collections.emptyList(), Collections.emptyMap());
	}

	private static HumanoidModel createArmorModel(LivingEntity living, HumanoidModel defaultModel, ArmorItem.Type armorType) {
		Modeltang_dynasty_infantry_armor model = new Modeltang_dynasty_infantry_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modeltang_dynasty_infantry_armor.LAYER_LOCATION));
		Map<String, ModelPart> parts = switch (armorType) {
			case HELMET -> Map.of("head", model.bipedHead, "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart(), "right_leg", emptyPart(), "left_leg", emptyPart());
			case CHESTPLATE -> Map.of("body", model.bipedBody, "left_arm", model.bipedLeftArm, "right_arm", model.bipedRightArm, "head", emptyPart(), "hat", emptyPart(), "right_leg", emptyPart(), "left_leg", emptyPart());
			case LEGGINGS -> Map.of("left_leg", model.bipedLeftLeg, "right_leg", model.bipedRightLeg, "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart());
			case BOOTS -> Map.of("left_leg", model.LeftBoots, "right_leg", model.RightBoots, "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart());
		};
		HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), parts));
		armorModel.crouching = living.isShiftKeyDown();
		armorModel.riding = defaultModel.riding;
		armorModel.young = living.isBaby();
		return armorModel;
	}

	private static void initializeArmorClient(Consumer<IClientItemExtensions> consumer, ArmorItem.Type armorType) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				return createArmorModel(living, defaultModel, armorType);
			}
		});
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return "chineseweapons:textures/entities/tang_dynasty_infantry_armor.png";
	}

	public static class Helmet extends TangdynastyinfantryarmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			initializeArmorClient(consumer, ArmorItem.Type.HELMET);
		}
	}

	public static class Chestplate extends TangdynastyinfantryarmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			initializeArmorClient(consumer, ArmorItem.Type.CHESTPLATE);
		}
	}

	public static class Leggings extends TangdynastyinfantryarmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			initializeArmorClient(consumer, ArmorItem.Type.LEGGINGS);
		}
	}

	public static class Boots extends TangdynastyinfantryarmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			initializeArmorClient(consumer, ArmorItem.Type.BOOTS);
		}
	}
}
