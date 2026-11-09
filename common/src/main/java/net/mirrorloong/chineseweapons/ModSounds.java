package net.mirrorloong.chineseweapons;

import net.minecraft.sounds.SoundEvent;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public final class ModSounds {
    private static final RegistryBuilder<SoundEvent> REGISTRY =
            ChineseWeapons.registration().builder("minecraft:sound_event");

    public static final RegistryEntry<SoundEvent> GUDUO =
            REGISTRY.register("guduo", () -> ChineseWeapons.platform().content().sound("guduo"));

    private ModSounds() {
    }

    public static void init() {
    }
}
