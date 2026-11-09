package net.mirrorloong.chineseweapons.recipes;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.spi.DyeableArmorSupport;

/**
 * Dyes a {@link DyeableItem} by combining it with dyes. 26.1 crafting recipes
 * take a {@link CraftingInput} and the serializer is a codec record; the
 * colour averaging is shared.
 */
public class DyeableArmorRecipe extends CustomRecipe {

    public static final DyeableArmorRecipe INSTANCE = new DyeableArmorRecipe();

    public static final RecipeSerializer<DyeableArmorRecipe> SERIALIZER = new RecipeSerializer<>(
            MapCodec.unit(INSTANCE),
            StreamCodec.unit(INSTANCE));

    private static final DyeableArmorSupport DYE = new DyeableArmorSupport() {
        @Override
        public boolean isDyeable(ItemStack stack) {
            return stack.getItem() instanceof DyeableItem dyeable && dyeable.isDyeable();
        }

        @Override
        public boolean hasCustomColor(ItemStack stack) {
            return DyeableItem.hasCustomColor(stack);
        }

        @Override
        public int getColor(ItemStack stack) {
            return DyeableItem.getColor(stack);
        }

        @Override
        public void setColor(ItemStack stack, int color) {
            DyeableItem.setColor(stack, color);
        }

        @Override
        public int[] dyeRgb(ItemStack dyeStack) {
            DyeColor dyeColor = dyeStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
            int color = dyeColor.getTextureDiffuseColor();
            return new int[]{(color >> 16) & 255, (color >> 8) & 255, color & 255};
        }
    };

    public DyeableArmorRecipe() {
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        return DyeableArmorCrafting.matches(container::getItem, container.size(), DYE);
    }

    @Override
    public ItemStack assemble(CraftingInput container) {
        return DyeableArmorCrafting.assemble(container::getItem, container.size(), DYE);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        return DyeableArmorCrafting.remainingItems(container::getItem, container.size());
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
