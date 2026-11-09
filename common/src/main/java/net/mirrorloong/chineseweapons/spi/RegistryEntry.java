package net.mirrorloong.chineseweapons.spi;

import java.util.function.Supplier;

/**
 * Handle for one registered object. Mirrors the subset of loader registry
 * handles the shared code uses.
 */
public interface RegistryEntry<T> extends Supplier<T> {
    ResourceId id();
}
