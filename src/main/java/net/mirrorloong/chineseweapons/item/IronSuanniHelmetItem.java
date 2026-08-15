
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.client.model.Modelsuanni_helmet;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public abstract class IronSuanniHelmetItem extends ArmorItem {
	public IronSuanniHelmetItem(Type type, Properties properties) {
				super(ChineseWeaponsArmorMaterials.holder("ironsuannihelmet", new int[]{3, 6, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_IRON, Items.IRON_INGOT, 0f, 0f), type, properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 16));
	}

	public static class Helmet extends IronSuanniHelmetItem {
    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.parse("chineseweapons:textures/entities/iron_suanni_helmet.png");
    }

		public Helmet() {
			super(Type.HELMET, new Properties());
		}

		@Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new Modelsuanni_helmet(Minecraft.getInstance().getEntityModels().bakeLayer(Modelsuanni_helmet.LAYER_LOCATION)).bipedHead, "hat",
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
}
