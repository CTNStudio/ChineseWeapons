package net.mirrorloong.chineseweapons.procedures;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DyeableItem {

    String TAG_COLOR = "Color";
    String TAG_DISPLAY = "display";

    Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    static boolean hasCustomColor(ItemStack stack) {
        if (stack.isEmpty()) return false;
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_COLOR);
    }

    static int getColor(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(TAG_COLOR)) {
            return tag.getInt(TAG_COLOR);
        }
        return 0xFFFFFF;
    }

    static void setColor(ItemStack stack, int color) {
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd -> cd.update(tag -> tag.putInt(TAG_COLOR, color)));
    }

    static void removeColor(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(TAG_COLOR)) {
            tag.remove(TAG_COLOR);
            if (tag.isEmpty()) {
                stack.remove(DataComponents.CUSTOM_DATA);
            } else {
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static String getArmorTexture(ItemStack stack, String type,
                                  String baseTexture, String overlayTexture) {
        if (hasCustomColor(stack)) {
            int color = getColor(stack);
            return getOrCreateDyedTexture(baseTexture, overlayTexture, color);
        }
        return getDefaultTexture(baseTexture, overlayTexture);
    }

    //默认
    @OnlyIn(Dist.CLIENT)
    private static String getDefaultTexture(String basePath, String overlayPath) {
        String cacheKey = "default_" + basePath.hashCode() + "_" + overlayPath.hashCode();

        if (TEXTURE_CACHE.containsKey(cacheKey)) {
            return TEXTURE_CACHE.get(cacheKey).toString();
        }

        try {
            Minecraft mc = Minecraft.getInstance();

            NativeImage baseImage = loadTexture(basePath);
            NativeImage overlayImage = loadTexture(overlayPath);

            if (baseImage == null || overlayImage == null) {
                return basePath;
            }

            int width = Math.min(baseImage.getWidth(), overlayImage.getWidth());
            int height = Math.min(baseImage.getHeight(), overlayImage.getHeight());

            NativeImage mergedImage = new NativeImage(width, height, true);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int basePixel = baseImage.getPixelRGBA(x, y);
                    mergedImage.setPixelRGBA(x, y, basePixel);
                }
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int overlayPixel = overlayImage.getPixelRGBA(x, y);
                    int overlayAlpha = (overlayPixel >> 24) & 255;

                    if (overlayAlpha > 0) {
                        mergedImage.setPixelRGBA(x, y, overlayPixel);
                    }
                }
            }

            String textureKey = "merged_armor/" + Math.abs(cacheKey.hashCode());
            ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath("chineseweapons", textureKey);

            TextureManager textureManager = mc.getTextureManager();
            DynamicTexture dynamicTexture = new DynamicTexture(mergedImage);
            textureManager.register(textureLocation, dynamicTexture);
            dynamicTexture.upload();

            TEXTURE_CACHE.put(cacheKey, textureLocation);

            baseImage.close();
            overlayImage.close();

            return textureLocation.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return basePath;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static String getOrCreateDyedTexture(String basePath, String overlayPath, int color) {
        String cacheKey = "dyed_" + basePath.hashCode() + "_" + overlayPath.hashCode() + "_" + String.format("%06X", color);

        if (TEXTURE_CACHE.containsKey(cacheKey)) {
            return TEXTURE_CACHE.get(cacheKey).toString();
        }

        try {
            Minecraft mc = Minecraft.getInstance();

            NativeImage baseImage = loadTexture(basePath);
            NativeImage overlayImage = loadTexture(overlayPath);

            if (baseImage == null || overlayImage == null) {
                return basePath;
            }

            int width = Math.min(baseImage.getWidth(), overlayImage.getWidth());
            int height = Math.min(baseImage.getHeight(), overlayImage.getHeight());

            NativeImage mergedImage = new NativeImage(width, height, true);

            float colorR = ((color >> 16) & 255) / 255.0f;
            float colorG = ((color >> 8) & 255) / 255.0f;
            float colorB = (color & 255) / 255.0f;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int basePixel = baseImage.getPixelRGBA(x, y);
                    mergedImage.setPixelRGBA(x, y, basePixel);
                }
            }

            float[] luminances = new float[width * height];
            int pixelCount = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int overlayPixel = overlayImage.getPixelRGBA(x, y);
                    int alpha = (overlayPixel >> 24) & 255;
                    if (alpha > 0) {
                        // 交换 R 和 B
                        int r = overlayPixel & 255;
                        int g = (overlayPixel >> 8) & 255;
                        int b = (overlayPixel >> 16) & 255;

                        float luminance = 0.299f * r + 0.587f * g + 0.114f * b;
                        luminances[pixelCount] = luminance;
                        pixelCount++;
                    }
                }
            }

            float medianLuminance = 128f;
            float minLuminance = 0f;
            float maxLuminance = 255f;

            if (pixelCount > 0) {
                float[] validLuminances = new float[pixelCount];
                System.arraycopy(luminances, 0, validLuminances, 0, pixelCount);
                java.util.Arrays.sort(validLuminances);

                medianLuminance = validLuminances[pixelCount / 2];
                minLuminance = validLuminances[0];
                maxLuminance = validLuminances[pixelCount - 1];
            }

            float luminanceRange = Math.max(maxLuminance - minLuminance, 1f);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int overlayPixel = overlayImage.getPixelRGBA(x, y);
                    int overlayAlpha = (overlayPixel >> 24) & 255;

                    if (overlayAlpha > 0) {
                        int overlayR = overlayPixel & 255;
                        int overlayG = (overlayPixel >> 8) & 255;
                        int overlayB = (overlayPixel >> 16) & 255;

                        float pixelLuminance = 0.299f * overlayR + 0.587f * overlayG + 0.114f * overlayB;

                        float luminanceDeviation = (pixelLuminance - medianLuminance) / (luminanceRange / 2f);
                        luminanceDeviation = Math.max(-1f, Math.min(1f, luminanceDeviation));

                        float intensity = 0.5f;
                        float brightnessFactor = 1.0f + luminanceDeviation * intensity;

                        int dyedR = (int)(colorR * 255 * brightnessFactor);
                        int dyedG = (int)(colorG * 255 * brightnessFactor);
                        int dyedB = (int)(colorB * 255 * brightnessFactor);

                        dyedR = Math.min(255, Math.max(0, dyedR));
                        dyedG = Math.min(255, Math.max(0, dyedG));
                        dyedB = Math.min(255, Math.max(0, dyedB));

                        int dyedPixel = (overlayAlpha << 24) | (dyedB << 16) | (dyedG << 8) | dyedR;
                        mergedImage.setPixelRGBA(x, y, dyedPixel);
                    }
                }
            }

            String textureKey = "dyed_armor/" + Math.abs(cacheKey.hashCode());
            ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath("chineseweapons", textureKey);

            TextureManager textureManager = mc.getTextureManager();
            DynamicTexture dynamicTexture = new DynamicTexture(mergedImage);
            textureManager.register(textureLocation, dynamicTexture);
            dynamicTexture.upload();

            TEXTURE_CACHE.put(cacheKey, textureLocation);

            baseImage.close();
            overlayImage.close();

            return textureLocation.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return basePath;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static NativeImage loadTexture(String texturePath) throws IOException {
        ResourceLocation resourceLocation = ResourceLocation.parse(texturePath);
        Minecraft mc = Minecraft.getInstance();

        Optional<Resource> resource = mc.getResourceManager().getResource(resourceLocation);
        if (resource.isPresent()) {
            InputStream inputStream = resource.get().open();
            NativeImage image = NativeImage.read(inputStream);
            inputStream.close();
            return image;
        }

        return null;
    }

    static void addDyeTooltip(ItemStack stack, List<Component> tooltip) {
        if (hasCustomColor(stack)) {
            int color = getColor(stack);
            String hexColor = String.format("#%06X", color);
            tooltip.add(Component.translatable("text.chineseweapons.color", hexColor)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}