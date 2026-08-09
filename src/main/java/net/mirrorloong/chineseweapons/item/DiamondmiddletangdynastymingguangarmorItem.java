
package net.mirrorloong.chineseweapons.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.client.model.Modelmiddle_tang_dynasty_ming_guang_armor;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.Minecraft;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.Map;
import java.util.Collections;

public abstract class DiamondmiddletangdynastymingguangarmorItem extends ArmorItem implements DyeableItem {
	public DiamondmiddletangdynastymingguangarmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 34;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{2, 7, 9, 4}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 11;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_DIAMOND;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.DIAMOND));
			}

			@Override
			public String getName() {
				return "diamondmiddletangdynastymingguangarmor";
			}

			@Override
			public float getToughness() {
				return 2f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, type, properties);
	}

    public static final String TEXTURE_BASE = "chineseweapons:textures/entities/diamond_middle_tang_dynasty_ming_guang_armor.png";
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/middle_tang_dynasty_ming_guang_armor_color.png";

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DyeableItem.addDyeTooltip(stack, tooltip);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return DyeableItem.getArmorTexture(stack, type, TEXTURE_BASE, TEXTURE_OVERLAY);
    }

    public static class Helmet extends DiamondmiddletangdynastymingguangarmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}

        @Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedHead, "hat",
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
	}

	public static class Chestplate extends DiamondmiddletangdynastymingguangarmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("body", new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedBody, "left_arm",
									new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedLeftArm, "right_arm",
									new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedRightArm, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}
	}

	public static class Leggings extends DiamondmiddletangdynastymingguangarmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedLeftLeg, "right_leg",
									new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedRightLeg, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

	}

	public static class Boots extends DiamondmiddletangdynastymingguangarmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}


		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedLeft, "right_leg",
									new Modelmiddle_tang_dynasty_ming_guang_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmiddle_tang_dynasty_ming_guang_armor.LAYER_LOCATION)).bipedRight, "head",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}
	}
}
