package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.entity.BedCrossbowEntity;
import net.mirrorloong.chineseweapons.entity.BedCrossbowBoltEntity;

public class ChineseweaponsModEntities {
    //old api DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID) -> new api DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID)
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ChineseweaponsMod.MODID);

    public static final RegistryObject<EntityType<SongJavelinProjectile>> SONG_JAVELIN_PROJECTILE =
            ENTITIES.register("song_javelin",
                    () -> EntityType.Builder.<SongJavelinProjectile>of(
                                    (type, level) -> new SongJavelinProjectile(type, level), MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("song_javelin"));

    public static final RegistryObject<EntityType<SongStandingShieldEntity>> STANDING_SHIELD =
            ENTITIES.register("standing_shield",
                    () -> EntityType.Builder.<SongStandingShieldEntity>of(
                                    (type, level) -> new SongStandingShieldEntity(type, level), MobCategory.MISC)
                            .sized(0.8f, 2.8f)
                            .fireImmune()
                                                        .build(String.valueOf(new ResourceLocation(
                                    ChineseweaponsMod.MODID, "standing_shield"))));

    public static final RegistryObject<EntityType<BedCrossbowEntity>> BED_CROSSBOW =
            ENTITIES.register("bed_crossbow",
                    () -> EntityType.Builder.<BedCrossbowEntity>of(
                                    (type, level) -> new BedCrossbowEntity(type, level), MobCategory.MISC)
                            .sized(2.4F, 1.4F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("bed_crossbow"));

    public static final RegistryObject<EntityType<BedCrossbowBoltEntity>> BED_CROSSBOW_BOLT =
            ENTITIES.register("bed_crossbow_bolt",
                    () -> EntityType.Builder.<BedCrossbowBoltEntity>of(
                                    (type, level) -> new BedCrossbowBoltEntity(type, level), MobCategory.MISC)
                            .sized(2.0F, 0.6F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("bed_crossbow_bolt"));

            public static void bootstrap() {
    }
}
