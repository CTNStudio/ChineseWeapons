package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * 26.1 serializers are plain records; the JSON/network logic lives in the
 * recipe's codecs.
 */
public final class WCTRecipeSer {
    public static final RecipeSerializer<WCTRecipe> SERIALIZER =
            new RecipeSerializer<>(WCTRecipe.MAP_CODEC, WCTRecipe.STREAM_CODEC);

    private WCTRecipeSer() {
    }
}
