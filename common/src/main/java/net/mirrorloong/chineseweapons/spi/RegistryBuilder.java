package net.mirrorloong.chineseweapons.spi;

import java.util.function.Supplier;

/**
 * Per-registry builder supplied by a target. Registrations are deferred until
 * the target attaches its loader registry to the mod event bus.
 */
public interface RegistryBuilder<T> {
    <V extends T> RegistryEntry<V> register(String name, Supplier<V> factory);
}
