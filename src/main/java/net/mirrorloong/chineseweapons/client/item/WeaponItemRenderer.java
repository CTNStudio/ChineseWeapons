package net.mirrorloong.chineseweapons.client.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class WeaponItemRenderer implements ModelLoadingPlugin {

    private record Entry(ResourceLocation model, ResourceLocation guiTexture) {
    }

    private static final Map<Item, Entry> ENTRIES = new LinkedHashMap<>();
    private static final List<ResourceLocation> MODELS = new ArrayList<>();
    private static final BuiltinItemRendererRegistry.DynamicItemRenderer RENDERER = WeaponItemRenderer::render;
    private static boolean initialized;

    private WeaponItemRenderer() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        register(ChineseweaponsModItems.DIAMOND_DAGGER_AXE.get(), "chineseweapons:item/diamonddaggeraxe", "chineseweapons:textures/item/mini_diamond_dagger_axe.png");
        register(ChineseweaponsModItems.DIAMOND_GLAIVE.get(), "chineseweapons:item/diamondglaive", "chineseweapons:textures/item/mini_diamond_glaive.png");
        register(ChineseweaponsModItems.DIAMOND_GU_DUO.get(), "chineseweapons:item/diamond_guduo_3d", "chineseweapons:textures/item/mini_diamond_guduo.png");
        register(ChineseweaponsModItems.DIAMOND_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/diamond_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_diamond_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.DIAMOND_SONG_JAVENLIN.get(), "chineseweapons:item/diamond_song_javelin_3d", "chineseweapons:textures/item/mini_diamond_song_javelin.png");
        register(ChineseweaponsModItems.DIAMOND_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/diamond_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/diamond_tang_dynasty_heng_saber.png");
        register(ChineseweaponsModItems.GOLDEN_DAGGER_AXE.get(), "chineseweapons:item/goldendaggeraxe", "chineseweapons:textures/item/mini_golden_dagger_axe.png");
        register(ChineseweaponsModItems.GOLDEN_GLAIVE.get(), "chineseweapons:item/goldenglaive", "chineseweapons:textures/item/mini_golden_glaive.png");
        register(ChineseweaponsModItems.GOLDEN_GU_DUO.get(), "chineseweapons:item/golden_guduo_3d", "chineseweapons:textures/item/mini_golden_guduo.png");
        register(ChineseweaponsModItems.GOLDEN_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/golden_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_golden_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.GOLDEN_SONG_JAVENLIN.get(), "chineseweapons:item/golden_song_javelin_3d", "chineseweapons:textures/item/mini_golden_song_javelin.png");
        register(ChineseweaponsModItems.GOLDEN_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/golden_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/golden_tang_dynasty_heng_saber.png");
        register(ChineseweaponsModItems.GREENLOONG_GLAIVE.get(), "chineseweapons:item/greenloongglaive", "chineseweapons:textures/item/mini_green_loong_glaive.png");
        register(ChineseweaponsModItems.IRON_DAGGER_AXE.get(), "chineseweapons:item/irondaggeraxe", "chineseweapons:textures/item/mini_iron_dagger_axe.png");
        register(ChineseweaponsModItems.IRON_GLAIVE.get(), "chineseweapons:item/ironglaive", "chineseweapons:textures/item/mini_iron_glaive.png");
        register(ChineseweaponsModItems.IRON_GU_DUO.get(), "chineseweapons:item/iron_guduo_3d", "chineseweapons:textures/item/mini_iron_guduo.png");
        register(ChineseweaponsModItems.IRON_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/iron_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_iron_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.IRON_SONG_JAVENLIN.get(), "chineseweapons:item/iron_song_javelin_3d", "chineseweapons:textures/item/mini_iron_song_javelin.png");
        register(ChineseweaponsModItems.IRON_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/iron_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/iron_tang_dynasty_heng_saber.png");
        register(ChineseweaponsModItems.NETHERITE_DAGGER_AXE.get(), "chineseweapons:item/netheritedaggeraxe", "chineseweapons:textures/item/mini_netherite_dagger_axe.png");
        register(ChineseweaponsModItems.NETHERITE_GLAIVE.get(), "chineseweapons:item/netheriteglaive", "chineseweapons:textures/item/mini_netherite_glaive.png");
        register(ChineseweaponsModItems.NETHERITE_GU_DUO.get(), "chineseweapons:item/netherite_guduo_3d", "chineseweapons:textures/item/mini_netherite_guduo.png");
        register(ChineseweaponsModItems.NETHERITE_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/netherite_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_netherite_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.NETHERITE_SONG_JAVENLIN.get(), "chineseweapons:item/netherite_song_javelin_3d", "chineseweapons:textures/item/mini_netherite_song_javelin.png");
        register(ChineseweaponsModItems.NETHERITE_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/netherite_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/netherite_tang_dynasty_heng_saber.png");
        register(ChineseweaponsModItems.STONE_DAGGER_AXE.get(), "chineseweapons:item/stonedaggeraxe", "chineseweapons:textures/item/mini_stone_dagger_axe.png");
        register(ChineseweaponsModItems.STONE_GLAIVE.get(), "chineseweapons:item/stoneglaive", "chineseweapons:textures/item/mini_stone_glaive.png");
        register(ChineseweaponsModItems.STONE_GU_DUO.get(), "chineseweapons:item/stone_guduo_3d", "chineseweapons:textures/item/mini_stone_guduo.png");
        register(ChineseweaponsModItems.STONE_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/stone_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_stone_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.STONE_SONG_JAVENLIN.get(), "chineseweapons:item/stone_song_javelin_3d", "chineseweapons:textures/item/mini_stone_song_javelin.png");
        register(ChineseweaponsModItems.STONE_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/stone_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/stone_tang_dynasty_heng_saber.png");
        register(ChineseweaponsModItems.WOODEN_DAGGER_AXE.get(), "chineseweapons:item/woodendaggeraxe", "chineseweapons:textures/item/mini_wooden_dagger_axe.png");
        register(ChineseweaponsModItems.WOODEN_GLAIVE.get(), "chineseweapons:item/woodenglaive", "chineseweapons:textures/item/mini_wooden_glaive.png");
        register(ChineseweaponsModItems.WOODEN_GU_DUO.get(), "chineseweapons:item/wooden_guduo_3d", "chineseweapons:textures/item/mini_wooden_guduo.png");
        register(ChineseweaponsModItems.WOODEN_SKY_PIERCING_HALBERD.get(), "chineseweapons:item/wooden_sky_piercing_halberd_3d", "chineseweapons:textures/item/mini_wooden_sky_piercing_halberd.png");
        register(ChineseweaponsModItems.WOODEN_SONG_JAVENLIN.get(), "chineseweapons:item/wooden_song_javelin_3d", "chineseweapons:textures/item/mini_wooden_song_javelin.png");
        register(ChineseweaponsModItems.WOODEN_TANG_DYNASTY_HENG_SABER.get(), "chineseweapons:item/wooden_tang_dynasty_heng_saber_3d", "chineseweapons:textures/item/wooden_tang_dynasty_heng_saber.png");
        ModelLoadingPlugin.register(new WeaponItemRenderer());
    }

    private static void register(Item item, String model, String guiTexture) {
        ResourceLocation modelId = new ResourceLocation(model);
        ENTRIES.put(item, new Entry(modelId, new ResourceLocation(guiTexture)));
        MODELS.add(modelId);
        BuiltinItemRendererRegistry.INSTANCE.register(item, RENDERER);
    }

    @Override
    public void onInitializeModelLoader(Context context) {
        context.addModels(MODELS);
    }

    private static void render(ItemStack stack, ItemDisplayContext mode,
                               PoseStack matrices, MultiBufferSource buffers, int light, int overlay) {
        Entry entry = ENTRIES.get(stack.getItem());
        if (entry == null) {
            return;
        }
        if (mode == ItemDisplayContext.GUI) {
            drawGuiIcon(entry.guiTexture(), matrices, buffers, light, overlay);
            return;
        }
        boolean leftHanded = mode == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || mode == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(entry.model());
        if (model == null || model.isCustomRenderer()
                || model == Minecraft.getInstance().getModelManager().getMissingModel()) {
            return;
        }
        matrices.pushPose();
        matrices.translate(0.5D, 0.5D, 0.5D);
        Minecraft.getInstance().getItemRenderer().render(stack, mode, leftHanded, matrices, buffers, light, overlay, model);
        matrices.popPose();
    }

    private static void drawGuiIcon(ResourceLocation texture, PoseStack matrices, MultiBufferSource buffers,
                                    int light, int overlay) {
        RenderType type = RenderType.entityCutoutNoCull(texture);
        boolean immediate = buffers instanceof MultiBufferSource.BufferSource;
        if (immediate) {
            Lighting.setupForFlatItems();
        }
        VertexConsumer consumer = buffers.getBuffer(type);
        Matrix4f matrix = matrices.last().pose();
        consumer.vertex(matrix, 0.0F, 1.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix, 1.0F, 1.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(overlay).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(overlay).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(overlay).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        if (immediate) {
            ((MultiBufferSource.BufferSource) buffers).endBatch(type);
            Lighting.setupFor3DItems();
        }
    }
}
