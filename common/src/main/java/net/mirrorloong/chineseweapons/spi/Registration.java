package net.mirrorloong.chineseweapons.spi;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Loader-neutral entry point for registering content.
 */
public interface Registration {
    /**
     * Versioned overload kept for code that already holds a registry key.
     */
    <T> RegistryBuilder<T> builder(ResourceKey<Registry<T>> registry);

    /**
     * Version-neutral overload. The id uses vanilla's {@code namespace:path}
     * form, e.g. {@code "minecraft:item"}; each target maps it to its own
     * registry type.
     */
    <T> RegistryBuilder<T> builder(String registryId);
}
