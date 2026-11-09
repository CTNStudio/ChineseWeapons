package net.mirrorloong.chineseweapons.fabric;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.Registration;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;
import net.mirrorloong.chineseweapons.spi.ResourceId;

/**
 * Fabric has no deferred-register event bus: mod initializers run during the
 * registry-filling phase, so entries are registered eagerly and the handle
 * returned by {@link RegistryEntry#get()} is the registered object itself.
 */
public final class FabricRegistration implements Registration {
    @Override
    @SuppressWarnings("unchecked")
    public <T> RegistryBuilder<T> builder(ResourceKey<Registry<T>> registryKey) {
        return new Builder<>((Registry<T>) resolve(registryKey));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> RegistryBuilder<T> builder(String registryId) {
        return new Builder<>((Registry<T>) resolveId(registryId));
    }

    private static Registry<?> resolveId(String registryId) {
        switch (registryId) {
            case "minecraft:item":
                return BuiltInRegistries.ITEM;
            case "minecraft:block":
                return BuiltInRegistries.BLOCK;
            case "minecraft:entity_type":
                return BuiltInRegistries.ENTITY_TYPE;
            case "minecraft:block_entity_type":
                return BuiltInRegistries.BLOCK_ENTITY_TYPE;
            case "minecraft:creative_mode_tab":
                return BuiltInRegistries.CREATIVE_MODE_TAB;
            case "minecraft:mob_effect":
                return BuiltInRegistries.MOB_EFFECT;
            case "minecraft:sound_event":
                return BuiltInRegistries.SOUND_EVENT;
            default:
                break;
        }
        Registry<?> resolved = BuiltInRegistries.REGISTRY.get(new ResourceLocation(registryId));
        if (resolved == null) {
            throw new IllegalStateException("Unknown registry " + registryId);
        }
        return resolved;
    }

    private static Registry<?> resolve(ResourceKey<? extends Registry<?>> key) {
        if (key == Registries.ITEM) {
            return BuiltInRegistries.ITEM;
        }
        if (key == Registries.BLOCK) {
            return BuiltInRegistries.BLOCK;
        }
        if (key == Registries.ENTITY_TYPE) {
            return BuiltInRegistries.ENTITY_TYPE;
        }
        if (key == Registries.BLOCK_ENTITY_TYPE) {
            return BuiltInRegistries.BLOCK_ENTITY_TYPE;
        }
        if (key == Registries.CREATIVE_MODE_TAB) {
            return BuiltInRegistries.CREATIVE_MODE_TAB;
        }
        if (key == Registries.MOB_EFFECT) {
            return BuiltInRegistries.MOB_EFFECT;
        }
        if (key == Registries.ENCHANTMENT) {
            return BuiltInRegistries.ENCHANTMENT;
        }
        if (key == Registries.RECIPE_TYPE) {
            return BuiltInRegistries.RECIPE_TYPE;
        }
        if (key == Registries.RECIPE_SERIALIZER) {
            return BuiltInRegistries.RECIPE_SERIALIZER;
        }
        if (key == Registries.MENU) {
            return BuiltInRegistries.MENU;
        }
        if (key == Registries.SOUND_EVENT) {
            return BuiltInRegistries.SOUND_EVENT;
        }
        Registry<?> resolved = BuiltInRegistries.REGISTRY.get(key.location());
        if (resolved == null) {
            throw new IllegalStateException("Unknown registry " + key.location());
        }
        return resolved;
    }

    private static final class Builder<T> implements RegistryBuilder<T> {
        private final Registry<T> registry;

        private Builder(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public <V extends T> RegistryEntry<V> register(String name, Supplier<V> factory) {
            ResourceId id = new ResourceId(ChineseWeapons.MOD_ID, name);
            V value = factory.get();
            Registry.register(registry, new ResourceLocation(id.namespace(), id.path()), value);
            return new Entry<>(id, value);
        }
    }

    private static final class Entry<T> implements RegistryEntry<T> {
        private final ResourceId id;
        private final T value;

        private Entry(ResourceId id, T value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public ResourceId id() {
            return id;
        }
    }
}
