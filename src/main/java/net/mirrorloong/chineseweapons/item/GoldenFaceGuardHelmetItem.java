package net.mirrorloong.chineseweapons.item;

import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.client.model.Modelface_guard;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.procedures.armor.FaceGuardHelmetKHProcedures;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GoldenFaceGuardHelmetItem extends ArmorItem {

    public GoldenFaceGuardHelmetItem(Type type, Properties properties) {
        super(ChineseWeaponsArmorMaterials.holder(
                        "golden_face_guard_helmet",
                        new int[]{2, 4, 6, 2},   // boots, leggings, chestplate, helmet
                        26,                      // 附魔值
                        SoundEvents.ARMOR_EQUIP_GOLD,
                        Items.GOLD_INGOT,
                        1f,                      // 韧性
                        0f),                     // 击退抗性
                type,
                properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 15));
    }

    public static class Helmet extends GoldenFaceGuardHelmetItem {
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
            consumer.accept(new IClientItemExtensions() {
                @Override
                public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack,
                                                              EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                    HumanoidModel<?> armorModel = new HumanoidModel<>(new ModelPart(Collections.emptyList(),
                            Map.of("head",
                                    new Modelface_guard(Minecraft.getInstance().getEntityModels()
                                            .bakeLayer(Modelface_guard.LAYER_LOCATION)).bipedHead,
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                    armorModel.crouching = living.isShiftKeyDown();
                    armorModel.riding = defaultModel.riding;
                    armorModel.young = living.isBaby();
                    return armorModel;
                }
            });
        }
    }
}