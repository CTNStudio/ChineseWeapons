package net.mirrorloong.chineseweapons.neoforge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.Registration;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;
import net.mirrorloong.chineseweapons.spi.ResourceId;

public final class NeoForgeRegistration implements Registration {
    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registers = new LinkedHashMap<>();

    @Override
    public <T> RegistryBuilder<T> builder(ResourceKey<Registry<T>> registry) {
        DeferredRegister<T> deferred = DeferredRegister.create(registry, ChineseWeapons.MOD_ID);
        registers.put(registry, deferred);
        return new Builder<>(deferred);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> RegistryBuilder<T> builder(String registryId) {
        return builder((ResourceKey<Registry<T>>) (ResourceKey<?>) registryKey(registryId));
    }

    private static ResourceKey<? extends Registry<?>> registryKey(String registryId) {
        return switch (registryId) {
            case "minecraft:item" -> Registries.ITEM;
            case "minecraft:block" -> Registries.BLOCK;
            case "minecraft:block_entity_type" -> Registries.BLOCK_ENTITY_TYPE;
            case "minecraft:entity_type" -> Registries.ENTITY_TYPE;
            case "minecraft:mob_effect" -> Registries.MOB_EFFECT;
            case "minecraft:sound_event" -> Registries.SOUND_EVENT;
            case "minecraft:creative_mode_tab" -> Registries.CREATIVE_MODE_TAB;
            default -> throw new IllegalArgumentException("Unsupported registry: " + registryId);
        };
    }

    public void attach(IEventBus modBus) {
        registers.values().forEach(register -> register.register(modBus));
    }

    private static final class Builder<T> implements RegistryBuilder<T> {
        private final DeferredRegister<T> deferred;

        private Builder(DeferredRegister<T> deferred) {
            this.deferred = deferred;
        }

        @Override
        public <V extends T> RegistryEntry<V> register(String name, Supplier<V> factory) {
            DeferredHolder<T, V> object = deferred.register(name, factory);
            return new Entry<>(object);
        }
    }

    private static final class Entry<T> implements RegistryEntry<T> {
        private final DeferredHolder<?, ?> object;

        private Entry(DeferredHolder<?, ?> object) {
            this.object = object;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get() {
            return (T) object.get();
        }

        @Override
        public ResourceId id() {
            return new ResourceId(object.getId().getNamespace(), object.getId().getPath());
        }
    }
}
