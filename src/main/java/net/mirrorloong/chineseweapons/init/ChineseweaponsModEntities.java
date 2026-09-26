package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;

public class ChineseweaponsModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ChineseweaponsMod.MODID);

    public static final RegistryObject<EntityType<SongJavelinProjectile>> SONG_JAVELIN_PROJECTILE =
            ENTITIES.register("song_javelin",
                    () -> EntityType.Builder.<SongJavelinProjectile>of(
                                    SongJavelinProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("song_javelin"));

    public static final RegistryObject<EntityType<SongStandingShieldEntity>> STANDING_SHIELD =
            ENTITIES.register("standing_shield",
                    () -> EntityType.Builder.of(
                                    SongStandingShieldEntity::new, MobCategory.MISC)
                    .sized(0.8f, 2.8f)
                    .fireImmune()
                    .build("standing_shield"));
}