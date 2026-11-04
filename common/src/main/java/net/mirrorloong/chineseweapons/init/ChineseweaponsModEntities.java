package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.spi.RegistryBuilder;
import net.mirrorloong.chineseweapons.spi.RegistryEntry;

public class ChineseweaponsModEntities {
    private static final RegistryBuilder<EntityType<?>> ENTITIES =
            ChineseWeapons.registration().builder("minecraft:entity_type");

    public static final RegistryEntry<EntityType<?>> SONG_JAVELIN_PROJECTILE =
            ENTITIES.register("song_javelin", () -> ChineseWeapons.platform().content().songJavelinEntity());

    public static final RegistryEntry<EntityType<?>> STANDING_SHIELD =
            ENTITIES.register("standing_shield", () -> ChineseWeapons.platform().content().standingShieldEntity());

    /**
     * The shared table stores {@code EntityType<?>} because the concrete entity
     * classes are target types. Call sites that need the concrete generic pass
     * it explicitly, e.g. {@code ChineseweaponsModEntities.<SongJavelinProjectile>type(...)}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityType<T> type(RegistryEntry<EntityType<?>> entry) {
        return (EntityType<T>) entry.get();
    }

    public static void init() {
    }
}
