package net.mirrorloong.chineseweapons.forge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.Registration;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;
import net.mirrorloong.chineseweapons.spi.ResourceId;

public final class ForgeRegistration implements Registration {
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
            case "minecraft:item" -> Registry.ITEM_REGISTRY;
            case "minecraft:block" -> Registry.BLOCK_REGISTRY;
            case "minecraft:block_entity_type" -> Registry.BLOCK_ENTITY_TYPE_REGISTRY;
            case "minecraft:entity_type" -> Registry.ENTITY_TYPE_REGISTRY;
            case "minecraft:mob_effect" -> Registry.MOB_EFFECT_REGISTRY;
            case "minecraft:sound_event" -> Registry.SOUND_EVENT_REGISTRY;
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
            RegistryObject<V> object = deferred.register(name, factory);
            return new Entry<>(object);
        }
    }

    private static final class Entry<T> implements RegistryEntry<T> {
        private final RegistryObject<T> object;

        private Entry(RegistryObject<T> object) {
            this.object = object;
        }

        @Override
        public T get() {
            return object.get();
        }

        @Override
        public ResourceId id() {
            return new ResourceId(object.getId().getNamespace(), object.getId().getPath());
        }
    }
}
