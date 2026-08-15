
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.client.model.Modelbohai_helmet;
import net.mirrorloong.chineseweapons.procedures.DyeableItem;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public abstract class DiamondBohaiHelmetItem extends ArmorItem implements DyeableItem {
	public DiamondBohaiHelmetItem(Type type, Properties properties) {
				super(ChineseWeaponsArmorMaterials.holder("diamondbohaihelmet", new int[]{2, 7, 9, 4}, 11, SoundEvents.ARMOR_EQUIP_DIAMOND, Items.DIAMOND, 2f, 0f), type, properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 34));
	}

    public static final String TEXTURE_BASE = "chineseweapons:textures/entities/diamond_bohai_helmet.png";
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/bohai_helmet_color.png";
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


    public static class Helmet extends DiamondBohaiHelmetItem {
		public Helmet() {
			super(Type.HELMET, new Properties());
		}

        @Override
		public void initializeClient(Consumer<IClientItemExtensions> consumer) {
			consumer.accept(new IClientItemExtensions() {
				@Override
				public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new Modelbohai_helmet(Minecraft.getInstance().getEntityModels().bakeLayer(Modelbohai_helmet.LAYER_LOCATION)).bipedHead, "hat",
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
