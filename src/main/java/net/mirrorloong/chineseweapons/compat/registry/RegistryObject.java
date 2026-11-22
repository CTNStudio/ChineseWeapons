package net.mirrorloong.chineseweapons.compat.registry;

import net.minecraft.resources.ResourceLocation;

public final class RegistryObject<T> {

    private final ResourceLocation id;
    private T value;

    RegistryObject(ResourceLocation id) {
        this.id = id;
    }

    void bind(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public ResourceLocation getId() {
        return id;
    }

    public boolean isPresent() {
        return value != null;
    }
}
