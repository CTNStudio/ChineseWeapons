
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.client.model.Modellate_tang_dynasty_ming_guang_armor;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GoldenlatetangdynastymingguangarmorItem extends ArmorItem {
	public GoldenlatetangdynastymingguangarmorItem(Type type, Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 15;
			}

			@Override
			public int getDefenseForType(Type type) {
				return new int[]{2, 4, 6, 3}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 26;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_GOLD;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.GOLD_INGOT));
			}

			@Override
			public String getName() {
				return "goldenlatetangdynastymingguangarmor";
			}

			@Override
			public float getToughness() {
				return 1f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, type, properties);
	}

	public static class Helmet extends GoldenlatetangdynastymingguangarmorItem {
		public Helmet() {
			super(Type.HELMET, new Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedHead, "hat",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "chineseweapons:textures/entities/golden_late_tang_dynasty_ming_guang_armor.png";
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}
	}

	public static class Chestplate extends GoldenlatetangdynastymingguangarmorItem {
		public Chestplate() {
			super(Type.CHESTPLATE, new Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("body", new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedBody, "left_arm",
									new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedLeftArm, "right_arm",
									new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedRightArm, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "chineseweapons:textures/entities/golden_late_tang_dynasty_ming_guang_armor.png";
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}
	}

	public static class Leggings extends GoldenlatetangdynastymingguangarmorItem {
		public Leggings() {
			super(Type.LEGGINGS, new Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedLeftLeg, "right_leg",
									new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedRightLeg, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "chineseweapons:textures/entities/golden_late_tang_dynasty_ming_guang_armor.png";
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}
	}

	public static class Boots extends GoldenlatetangdynastymingguangarmorItem {
		public Boots() {
			super(Type.BOOTS, new Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).LeftBoots, "right_leg",
									new Modellate_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modellate_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).RightBoots, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "chineseweapons:textures/entities/golden_late_tang_dynasty_ming_guang_armor.png";
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}
	}
}
