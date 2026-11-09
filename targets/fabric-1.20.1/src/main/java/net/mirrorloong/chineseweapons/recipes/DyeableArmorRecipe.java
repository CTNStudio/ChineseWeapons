package net.mirrorloong.chineseweapons.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.procedures.armor.DyeableItem;
import net.mirrorloong.chineseweapons.spi.DyeableArmorSupport;

public class DyeableArmorRecipe extends CustomRecipe {

    public static final RecipeSerializer<DyeableArmorRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(DyeableArmorRecipe::new);

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
            float[] colors = ((DyeItem) dyeStack.getItem()).getDyeColor().getTextureDiffuseColors();
            return new int[]{
                    (int) (colors[0] * 255.0F),
                    (int) (colors[1] * 255.0F),
                    (int) (colors[2] * 255.0F)
            };
        }
    };

    public DyeableArmorRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return DyeableArmorCrafting.matches(container::getItem, container.getContainerSize(), DYE);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return DyeableArmorCrafting.assemble(container::getItem, container.getContainerSize(), DYE);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return DyeableArmorCrafting.remainingItems(container::getItem, container.getContainerSize());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return DyeableArmorCrafting.canCraftInDimensions(width, height);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
