package net.mirrorloong.chineseweapons.neoforge;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.block.AncientGemBlock;
import net.mirrorloong.chineseweapons.block.AncientgemoreBlock;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;
import net.mirrorloong.chineseweapons.block.ArmorcastingtableBlock;
import net.mirrorloong.chineseweapons.block.DeepslateAncientgemoreBlock;
import net.mirrorloong.chineseweapons.content.ArmorDefinition;
import net.mirrorloong.chineseweapons.content.ArmorSlot;
import net.mirrorloong.chineseweapons.content.WeaponDefinition;
import net.mirrorloong.chineseweapons.content.WeaponMaterial;
import net.mirrorloong.chineseweapons.effects.BleedEffect;
import net.mirrorloong.chineseweapons.effects.CutsEffect;
import net.mirrorloong.chineseweapons.entity.SongJavelinProjectile;
import net.mirrorloong.chineseweapons.entity.SongStandingShieldEntity;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlocks;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModTabs;
import net.mirrorloong.chineseweapons.item.NeoForgeArmorItem;
import net.mirrorloong.chineseweapons.item.NeoForgeDaggerAxeItem;
import net.mirrorloong.chineseweapons.item.NeoForgeGlaiveItem;
import net.mirrorloong.chineseweapons.item.ForgingStoneItem;
import net.mirrorloong.chineseweapons.item.GuDuoItem;
import net.mirrorloong.chineseweapons.item.SkyPiercingHalberdItem;
import net.mirrorloong.chineseweapons.item.SongJavelinItem;
import net.mirrorloong.chineseweapons.item.SongStandingShield;
import net.mirrorloong.chineseweapons.item.TangDynastyHengSaberItem;
import net.mirrorloong.chineseweapons.item.TangDynastyHorseItem;
import net.mirrorloong.chineseweapons.item.TangDynastyShield;
import net.mirrorloong.chineseweapons.spi.ContentFactory;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

public final class NeoForgeContentFactory implements ContentFactory {
    @Override
    public Item simpleItem() {
        return new Item(new Item.Properties());
    }

    @Override
    public Item blockItem(Block block) {
        return new BlockItem(block, new Item.Properties());
    }

    @Override
    public Item armor(ArmorDefinition definition, ArmorSlot slot) {
        return new NeoForgeArmorItem(definition, slot);
    }

    @Override
    public Item glaive(WeaponDefinition definition) {
        return new NeoForgeGlaiveItem(definition);
    }

    @Override
    public Item daggerAxe(WeaponDefinition definition) {
        return new NeoForgeDaggerAxeItem(definition);
    }

    @Override
    public Item hengSaber(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant) {
        return new TangDynastyHengSaberItem(tier(material), displayedAttackDamage, speed, fireResistant);
    }

    @Override
    public Item halberd(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant) {
        return new SkyPiercingHalberdItem(tier(material), displayedAttackDamage, speed, fireResistant);
    }

    @Override
    public Item javelin(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant,
            int durabilityOffset) {
        return new SongJavelinItem(tier(material), displayedAttackDamage, speed, fireResistant, durabilityOffset);
    }

    @Override
    public Item guDuo(WeaponMaterial material, int displayedAttackDamage, float speed, boolean fireResistant,
            int durability) {
        return new GuDuoItem(tier(material), displayedAttackDamage, speed, fireResistant, durability);
    }

    @Override
    public Item tangDynastyShield() {
        return new TangDynastyShield();
    }

    @Override
    public Item songStandingShield() {
        return new SongStandingShield();
    }

    @Override
    public Item horseArmor(int defense, String texturePath) {
        return new TangDynastyHorseItem(defense, ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, texturePath));
    }

    @Override
    public Item forgingStone(String tooltipKey) {
        return new ForgingStoneItem(tooltipKey);
    }

    @Override
    public Block armorCastingTable() {
        return new ArmorcastingtableBlock();
    }

    @Override
    public Block ancientGemOre() {
        return new AncientgemoreBlock();
    }

    @Override
    public Block deepslateAncientGemOre() {
        return new DeepslateAncientgemoreBlock();
    }

    @Override
    public Block ancientGemBlock() {
        return new AncientGemBlock();
    }

    @Override
    public BlockEntityType<?> armorCastingTableBlockEntity() {
        return BlockEntityType.Builder.of(ArmorCastingTableBlockEntity::new,
                ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get()).build(null);
    }

    @Override
    public EntityType<?> songJavelinEntity() {
        return EntityType.Builder.<SongJavelinProjectile>of(
                (type, level) -> new SongJavelinProjectile(type, level), MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("song_javelin");
    }

    @Override
    public EntityType<?> standingShieldEntity() {
        return EntityType.Builder.<SongStandingShieldEntity>of(
                (type, level) -> new SongStandingShieldEntity(type, level), MobCategory.MISC)
                .sized(0.8f, 2.8f)
                .fireImmune()
                .build("song_javelin");
    }

    @Override
    public MobEffect bleedEffect() {
        return new BleedEffect();
    }

    @Override
    public MobEffect cutsEffect() {
        return new CutsEffect();
    }

    @Override
    public SoundEvent sound(String path) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ChineseWeapons.MOD_ID, path));
    }

    @Override
    public void creativeTab() {
        ChineseWeapons.registration().builder("minecraft:creative_mode_tab").register("chinese_weapons",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable(ChineseweaponsModTabs.TITLE_KEY))
                        .icon(() -> new ItemStack(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_HELMET.get()))
                        .displayItems((parameters, output) -> {
                            for (Supplier<Item> item : ChineseweaponsModTabs.ITEMS) {
                                output.accept(item.get());
                            }
                        })
                        .build());
    }

    private static Tier tier(WeaponMaterial material) {
        return switch (material) {
            case WOOD -> Tiers.WOOD;
            case STONE -> Tiers.STONE;
            case GOLD -> Tiers.GOLD;
            case IRON -> Tiers.IRON;
            case DIAMOND -> Tiers.DIAMOND;
            case NETHERITE -> Tiers.NETHERITE;
        };
    }
}
