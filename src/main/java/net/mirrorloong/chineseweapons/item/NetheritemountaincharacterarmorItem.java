
package net.mirrorloong.chineseweapons.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.client.model.Modelmountain_character_armor;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

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
import net.minecraft.resources.ResourceLocation;

public abstract class NetheritemountaincharacterarmorItem extends ArmorItem implements DyeableItem {
	public NetheritemountaincharacterarmorItem(ArmorItem.Type type, Item.Properties properties) {
				super(ChineseWeaponsArmorMaterials.holder("netheritemountaincharacterarmor", new int[]{2, 7, 9, 4}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, Items.NETHERITE_INGOT, 3f, 0.1f), type, properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 38));
	}

    public static final String TEXTURE_BASE = "chineseweapons:textures/entities/netherite_mountain_character_armor.png";
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/mountain_character_armor_color.png";
    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.tryParse(DyeableItem.getArmorTexture(stack, "armor", TEXTURE_BASE, TEXTURE_OVERLAY));
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        DyeableItem.addDyeTooltip(stack, tooltip);
    }


	public static class Helmet extends NetheritemountaincharacterarmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().fireResistant());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedHead, "hat",
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

	public static class Chestplate extends NetheritemountaincharacterarmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("body", new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedBody, "left_arm",
									new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedLeftArm, "right_arm",
									new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedRightArm, "head",
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

	public static class Leggings extends NetheritemountaincharacterarmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedLeftLeg, "right_leg",
									new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedRightLeg, "head",
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

	public static class Boots extends NetheritemountaincharacterarmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedLeft, "right_leg",
									new Modelmountain_character_armor(Minecraft.getInstance().getEntityModels().bakeLayer(Modelmountain_character_armor.LAYER_LOCATION)).bipedRight, "head",
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
