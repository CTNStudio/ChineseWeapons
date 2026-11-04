package net.mirrorloong.chineseweapons.spi;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.content.WeaponMaterial;

/**
 * Creates every concrete piece of content for the shared content tables. The
 * shared source tree only names registry ids and definitions; the actual
 * loader/version-specific classes live in the targets.
 */
public interface ContentFactory {
    Item armor(ArmorDefinition definition, ArmorSlot slot);

    Item glaive(WeaponDefinition definition);

    Item daggerAxe(WeaponDefinition definition);

    Item hengSaber(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant);

    Item halberd(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant);

    Item javelin(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant,
            int durabilityOffset);

    Item guDuo(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant,
            int durability);

    Item tangDynastyShield();

    Item songStandingShield();

    /**
     * @param texturePath namespace-less texture path used by versions that still
     *                    take a horse armour texture directly
     */
    Item horseArmor(int defense, String texturePath);

    Item forgingStone(String tooltipKey);

    Block armorCastingTable();

    Block ancientGemOre();

    Block deepslateAncientGemOre();

    Block ancientGemBlock();

    BlockEntityType<?> armorCastingTableBlockEntity();

    EntityType<?> songJavelinEntity();

    EntityType<?> standingShieldEntity();

    MobEffect bleedEffect();

    MobEffect cutsEffect();

    SoundEvent sound(String path);

    /**
     * Builds and registers the creative tab through the loader API available on
     * this target (registry on 1.20.1+, legacy tab constructor on 1.18/1.19).
     * The tab contents come from {@code ChineseweaponsModTabs}.
     */
    void creativeTab();
}
