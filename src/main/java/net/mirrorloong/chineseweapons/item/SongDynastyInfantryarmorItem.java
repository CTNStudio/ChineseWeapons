package net.mirrorloong.chineseweapons.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.mirrorloong.chineseweapons.client.model.Modelsong_dynasty_infantry_armor;
import net.mirrorloong.chineseweapons.init.ChineseWeaponsArmorMaterials;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SongDynastyInfantryarmorItem extends ArmorItem implements DyeableItem {

    public static final String TEXTURE_BASE = "chineseweapons:textures/entities/song_dynasty_infantry_armor.png";
    public static final String TEXTURE_OVERLAY = "chineseweapons:textures/entities/song_dynasty_infantry_armor_color.png";

    public SongDynastyInfantryarmorItem(Type type, Properties properties) {
        super(ChineseWeaponsArmorMaterials.holder(
                        "song_dynasty_infantry_armor",
                        new int[]{4, 7, 8, 4},   // boots, leggings, chestplate, helmet
                        10,                      // 附魔值
                        SoundEvents.ARMOR_EQUIP_IRON,
                        Items.IRON_INGOT,
                        0f,                      // 韧性
                        0f),                     // 击退抗性
                type,
                properties.durability(new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 16));
    }

    private static ModelPart emptyPart() {
        return new ModelPart(Collections.emptyList(), Collections.emptyMap());
    }

    private static HumanoidModel<?> createArmorModel(LivingEntity living, HumanoidModel<?> defaultModel, Type armorType) {
        Modelsong_dynasty_infantry_armor model = new Modelsong_dynasty_infantry_armor(
                Minecraft.getInstance().getEntityModels().bakeLayer(Modelsong_dynasty_infantry_armor.LAYER_LOCATION));
        Map<String, ModelPart> parts = switch (armorType) {
            case HELMET -> Map.of("head", model.bipedHead, "hat", emptyPart(), "body", emptyPart(),
                    "right_arm", emptyPart(), "left_arm", emptyPart(),
                    "right_leg", emptyPart(), "left_leg", emptyPart());
            case CHESTPLATE -> Map.of("body", model.bipedBody, "left_arm", model.bipedLeftArm,
                    "right_arm", model.bipedRightArm, "head", emptyPart(), "hat", emptyPart(),
                    "right_leg", emptyPart(), "left_leg", emptyPart());
            case LEGGINGS -> Map.of("left_leg", model.bipedLeftLeg, "right_leg", model.bipedRightLeg,
                    "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(),
                    "right_arm", emptyPart(), "left_arm", emptyPart());
            case BOOTS -> Map.of("left_leg", model.LeftBoots, "right_leg", model.RightBoots,
                    "head", emptyPart(), "hat", emptyPart(), "body", emptyPart(),
                    "right_arm", emptyPart(), "left_arm", emptyPart());
            case BODY -> null;
        };
        HumanoidModel<?> armorModel = new HumanoidModel<>(new ModelPart(Collections.emptyList(), parts));
        armorModel.crouching = living.isShiftKeyDown();
        armorModel.riding = defaultModel.riding;
        armorModel.young = living.isBaby();
        return armorModel;
    }

    private static void initializeArmorClient(Consumer<IClientItemExtensions> consumer, Type armorType) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack,
                                                          EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                return createArmorModel(living, defaultModel, armorType);
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        DyeableItem.addDyeTooltip(stack, tooltip);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity,
                                            EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.tryParse(
                DyeableItem.getArmorTexture(stack, "armor", TEXTURE_BASE, TEXTURE_OVERLAY));
    }

    public static class Helmet extends SongDynastyInfantryarmorItem {
        public Helmet() {
            super(Type.HELMET, new Properties());
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            initializeArmorClient(consumer, Type.HELMET);
        }
    }

    public static class Chestplate extends SongDynastyInfantryarmorItem {
        public Chestplate() {
            super(Type.CHESTPLATE, new Properties());
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            initializeArmorClient(consumer, Type.CHESTPLATE);
        }
    }

    public static class Leggings extends SongDynastyInfantryarmorItem {
        public Leggings() {
            super(Type.LEGGINGS, new Properties());
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            initializeArmorClient(consumer, Type.LEGGINGS);
        }
    }

    public static class Boots extends SongDynastyInfantryarmorItem {
        public Boots() {
            super(Type.BOOTS, new Properties());
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            initializeArmorClient(consumer, Type.BOOTS);
        }

        @Override
        public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
            return true;
        }
    }
}