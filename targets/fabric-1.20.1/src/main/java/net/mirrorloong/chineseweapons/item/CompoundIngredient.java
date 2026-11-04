package net.mirrorloong.chineseweapons.item;

import java.util.Arrays;
import java.util.stream.Stream;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Minimal Fabric stand-in for Forge's
 * {@code net.minecraftforge.common.crafting.CompoundIngredient}. Only the repair
 * ingredient use case of the shared code is supported: matching any child
 * ingredient and exposing the combined item list.
 */
public final class CompoundIngredient extends Ingredient {
    private final Ingredient[] children;

    private CompoundIngredient(Ingredient[] children) {
        super(Stream.empty());
        this.children = children;
    }

    public static Ingredient of(Ingredient... children) {
        return new CompoundIngredient(children);
    }

    @Override
    public boolean test(ItemStack stack) {
        for (Ingredient child : children) {
            if (child.test(stack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack[] getItems() {
        return Arrays.stream(children)
                .flatMap(child -> Arrays.stream(child.getItems()))
                .toArray(ItemStack[]::new);
    }

    @Override
    public boolean isEmpty() {
        for (Ingredient child : children) {
            if (!child.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
