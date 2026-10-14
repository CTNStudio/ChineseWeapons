package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChineseweaponsModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ChineseweaponsMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SongJavelinProjectile>> SONG_JAVELIN_PROJECTILE =
            ENTITIES.register("song_javelin",
                    () -> EntityType.Builder.<SongJavelinProjectile>of(
                                    SongJavelinProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("song_javelin"));

    public static final DeferredHolder<EntityType<?>, EntityType<SongStandingShieldEntity>> STANDING_SHIELD =
            ENTITIES.register("standing_shield",
                    () -> EntityType.Builder.of(
                                    SongStandingShieldEntity::new, MobCategory.MISC)
                            .sized(0.8f, 2.8f)
                            .fireImmune()
                            .build(String.valueOf(ResourceLocation.fromNamespaceAndPath(
                                    ChineseweaponsMod.MODID, "standing_shield"))));
}