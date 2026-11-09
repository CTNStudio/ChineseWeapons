
package net.mirrorloong.chineseweapons.item;

import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mirrorloong.chineseweapons.client.model.Modelface_guard;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class NetheriteFaceGuardHelmetItem extends ArmorItem {
	public NetheriteFaceGuardHelmetItem(Type type, Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 38;
			}

			@Override
			public int getDefenseForType(Type type) {
				return new int[]{5, 7, 9, 4}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 16;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.NETHERITE_INGOT));
			}

			@Override
			public String getName() {
				return "netheritefaceguardhelmet";
			}

			@Override
			public float getToughness() {
				return 3f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.1f;
			}
		}, type, properties);
	}

	public static class Helmet extends NetheriteFaceGuardHelmetItem {
        public Helmet() {
            super(Type.HELMET, new Properties());
        }

        @Override
        public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
            super.inventoryTick(itemstack, world, entity, slot, selected);
            if (entity instanceof Player player && Iterables.contains(player.getArmorSlots(), itemstack)) {
                FaceGuardHelmetKHProcedures.execute(world, entity, itemstack);
            }
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept( new IClientItemExtensions() {
                @Override
                public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
                            Map.of("head", new Modelface_guard(Minecraft.getInstance().getEntityModels().bakeLayer(Modelface_guard.LAYER_LOCATION)).bipedHead, "hat",
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
            return "chineseweapons:textures/entities/netherite_face_guard.png";
        }
    }
}
