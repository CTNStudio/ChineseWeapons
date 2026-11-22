package net.mirrorloong.chineseweapons.compat.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class DeferredRegister<T> {

    private static final List<DeferredRegister<?>> ALL = new ArrayList<>();

    private final Registry<T> registry;
    private final String modid;
    private final List<Entry<? extends T>> entries = new ArrayList<>();

    private DeferredRegister(Registry<T> registry, String modid) {
        this.registry = registry;
        this.modid = modid;
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String modid) {
        DeferredRegister<T> deferredRegister = new DeferredRegister<>(registry, modid);
        ALL.add(deferredRegister);
        return deferredRegister;
    }

    public <R extends T> RegistryObject<R> register(String name, Supplier<? extends R> supplier) {
        Entry<R> entry = new Entry<>(new ResourceLocation(modid, name), supplier);
        entries.add(entry);
        return entry.object;
    }

            public static void registerAll() {
                List<DeferredRegister<?>> snapshot = new ArrayList<>(ALL);
        for (int i = 0; i < snapshot.size(); i++) {
            snapshot.get(i).registerEntries();
        }
    }

    private void registerEntries() {
        for (int i = 0; i < entries.size(); i++) {
            registerEntry(entries.get(i));
        }
    }

    private <R extends T> void registerEntry(Entry<R> entry) {
        R value = entry.supplier.get();
        Registry.register(this.registry, entry.id, value);
        entry.object.bind(value);
    }

    private static final class Entry<R> {
        private final ResourceLocation id;
        private final Supplier<? extends R> supplier;
        private final RegistryObject<R> object;

        private Entry(ResourceLocation id, Supplier<? extends R> supplier) {
            this.id = id;
            this.supplier = supplier;
            this.object = new RegistryObject<>(id);
        }
    }
}
