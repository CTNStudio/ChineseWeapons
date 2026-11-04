package net.mirrorloong.chineseweapons.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WCTRecipeSer implements RecipeSerializer<WCTRecipe> {
    @Override
    public MapCodec<WCTRecipe> codec() {
        return WCTRecipe.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WCTRecipe> streamCodec() {
        return WCTRecipe.STREAM_CODEC;
    }
}
