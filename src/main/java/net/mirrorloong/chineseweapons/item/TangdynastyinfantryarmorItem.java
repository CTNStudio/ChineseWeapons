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
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.client.model.Modeltang_dynasty_infantry_armor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public abstract class TangdynastyinfantryarmorItem extends ArmorItem implements DyeableItem {
	public TangdynastyinfantryarmorItem(ArmorItem.Type type, Item.Properties properties) {
				super(ChineseWeaponsArmorMaterials.holder("tang_dynasty_infantry_armor", new int[]{3, 6, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_IRON, Items.IRON_INGOT, 0f, 0f), type, properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 16));
	}

    public static final String TEXTURE_BASE = "chineseweapons:textures/entities/tang_dynasty_infantry_armor.png";
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/tang_dynasty_infantry_armor_color.png";
    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.tryParse(DyeableItem.getArmorTexture(stack, "armor", TEXTURE_BASE, TEXTURE_OVERLAY));
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
			case BODY -> Map.of("head", emptyPart(), "hat", emptyPart(), "body", emptyPart(), "right_arm", emptyPart(), "left_arm", emptyPart(), "right_leg", emptyPart(), "left_leg", emptyPart());
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        DyeableItem.addDyeTooltip(stack, tooltip);
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
