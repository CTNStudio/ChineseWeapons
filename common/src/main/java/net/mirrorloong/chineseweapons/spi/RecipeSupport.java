package net.mirrorloong.chineseweapons.spi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Version-specific decoding used by the legacy casting recipe JSON. Only the
 * targets whose Minecraft version still reads recipes as Gson objects
 * implement it; codec-based targets register their serializer directly.
 */
public interface RecipeSupport {

    /** Parses one {@code ingredient} JSON entry ({@code {"item": ...}} or {@code {"tag": ...}}). */
    Ingredient ingredientFromJson(JsonElement json);

    /** Parses the legacy result object ({@code {"item": ..., "count": ...}}). */
    ItemStack resultFromJson(JsonObject json);
}
