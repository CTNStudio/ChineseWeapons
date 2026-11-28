

package net.mirrorloong.chineseweapons.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

import net.mirrorloong.chineseweapons.procedures.SectionBanner;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class ChineseweaponsModTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChineseweaponsMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHINESE_WEAPONS = REGISTRY.register("chinese_weapons",
            () -> SectionBanner.create(
                    CreativeModeTab.builder()
                            .title(Component.translatable("item_group.chineseweapons.chinese_weapons"))
                            .icon(() -> new ItemStack(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_HELMET.get())),
                    out -> {

                        out.section(Component.translatable("item_group.chineseweapons.armor"),
                                0xFF8C3A2A, 0xFFD4A24A, null,
                                ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "iron_mountain_character_armor_helmet"), 0.9f,
                                "middle");

                        out.accept(ChineseweaponsModItems.TANGDYNASTYINFANTRYARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.TANGDYNASTYINFANTRYARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.TANGDYNASTYINFANTRYARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.TANGDYNASTYINFANTRYARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEEARLYTANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMIDDLETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDLATETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITELATETANGDYNASTYMINGGUANGARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONMOUNTAINCHARACTERARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENMOUNTAINCHARACTERARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDMOUNTAINCHARACTERARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEMOUNTAINCHARACTERARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONBLACKCHUIARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONBLACKCHUIARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONBLACKCHUIARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONBLACKCHUIARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENBLACKCHUIARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDBLACKCHUIARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEBLACKCHUIARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONFOOTMENARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONFOOTMENARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONFOOTMENARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONFOOTMENARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENFOOTMENARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENFOOTMENARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENFOOTMENARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENFOOTMENARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFOOTMENARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFOOTMENARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFOOTMENARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFOOTMENARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFOOTMENARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFOOTMENARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFOOTMENARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFOOTMENARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.SONGDYNASTYINFANTRYARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.SONGDYNASTYINFANTRYARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.SONGDYNASTYINFANTRYARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.SONGDYNASTYINFANTRYARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.IRONFINE_SCALE_ARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.GOLDENFINE_SCALE_ARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.DIAMONDFINE_SCALE_ARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_CHESTPLATE.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_LEGGINGS.get());
                        out.accept(ChineseweaponsModItems.NETHERITEFINE_SCALE_ARMOR_BOOTS.get());
                        out.accept(ChineseweaponsModItems.IRON_SUANNI_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_SUANNI_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_SUANNI_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_SUANNI_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRON_BOHAI_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_BOHAI_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_BOHAI_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_BOHAI_HELMET.get());
                        out.accept(ChineseweaponsModItems.IRON_FACE_GUARD_HELMET.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_FACE_GUARD_HELMET.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_FACE_GUARD_HELMET.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_FACE_GUARD_HELMET.get());

                        out.section(Component.translatable("item_group.chineseweapons.horse_armor"),
                                0xFF31CFCC, 0xFFD4A24A, null,
                                ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "diamond_tang_dynasty_horse_armor"), 0.9f,
                                "middle");

                        out.accept(ChineseweaponsModItems.IRON_TANG_DYNASTY_HORSE.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_TANG_DYNASTY_HORSE.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_TANG_DYNASTY_HORSE.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_TANG_DYNASTY_HORSE.get());

                        out.section(Component.translatable("item_group.chineseweapons.weapon"),
                                0xFF0DEF7B, 0xFFD4A24A, null,
                                ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "green_loong_glaive"), 0.9f,
                                "middle");

                        out.accept(ChineseweaponsModItems.WOODEN_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.STONE_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.IRON_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_DAGGER_AXE.get());
                        out.accept(ChineseweaponsModItems.WOODEN_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.STONE_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.IRON_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_SKY_PIERCING_HALBERD.get());
                        out.accept(ChineseweaponsModItems.WOODEN_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.STONE_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.IRON_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_TANG_DYNASTY_HENG_SABER.get());
                        out.accept(ChineseweaponsModItems.WOODEN_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.STONE_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.IRON_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_GU_DUO.get());
                        out.accept(ChineseweaponsModItems.WOODEN_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.STONE_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.IRON_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_SONG_JAVENLIN.get());
                        out.accept(ChineseweaponsModItems.WOODEN_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.STONE_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.IRON_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.GOLDEN_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.DIAMOND_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.NETHERITE_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.GREENLOONG_GLAIVE.get());
                        out.accept(ChineseweaponsModItems.TangDynastyShield.get());
                        out.accept(ChineseweaponsModItems.SongStandingShield.get());
                        out.accept(ChineseweaponsModItems.BED_CROSSBOW_ITEM.get());
                        out.accept(ChineseweaponsModItems.SPEAR_THREE_SWORD_BOLT.get());
                        out.accept(ChineseweaponsModItems.TREAD_PEG_BOLT.get());

                        out.section(Component.translatable("item_group.chineseweapons.materials_and_miscellaneous_items"),
                                0xFFB17A43, 0xFFD4A24A, null,
                                ResourceLocation.fromNamespaceAndPath(ChineseweaponsMod.MODID, "armor_casting_table"), 0.9f,
                                "middle");

                        out.accept(ChineseweaponsModBlocks.ARMOR_CASTING_TABLE.get().asItem());
                        out.accept(ChineseweaponsModBlocks.ANCIENT_GEM_ORE.get().asItem());
                        out.accept(ChineseweaponsModBlocks.DeepslateANCIENT_GEM_ORE.get().asItem());
                        out.accept(ChineseweaponsModBlocks.ANCIENT_GEM_BLOCK.get().asItem());
                        out.accept(ChineseweaponsModItems.AncientGem.get());
                        out.accept(ChineseweaponsModItems.DiamondArmorPieces.get());
                        out.accept(ChineseweaponsModItems.DiamondArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.DiamondMountainCharacterArmorPieces.get());
                        out.accept(ChineseweaponsModItems.DiamondMountainCharacterArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.GoldenArmorPieces.get());
                        out.accept(ChineseweaponsModItems.GoldenArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.GoldenMountainCharacterArmorPieces.get());
                        out.accept(ChineseweaponsModItems.GoldenMountainCharacterArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.IronArmorPieces.get());
                        out.accept(ChineseweaponsModItems.IronArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.IronMountainCharacterArmorPieces.get());
                        out.accept(ChineseweaponsModItems.IronMountainCharacterArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.StoneArmorPieces.get());
                        out.accept(ChineseweaponsModItems.StoneArmorPiecesPlate.get());
                        out.accept(ChineseweaponsModItems.EarlyTangDynastyMingGuangArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.FootmenArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.LateTangDynastyMingGuangArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.MiddleTangDynastyMingGuangArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.MountainCharacterArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.BlackChuiArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.FineScaleArmorforgingStone.get());
                        out.accept(ChineseweaponsModItems.TangDynastyInfantryArmorforgingStone.get());

                        if (ModList.get().isLoaded("patchouli")) {
                            out.section(Component.translatable("item_group.chineseweapons.book"),
                                    0xFF8C3A2A, 0xFFD4A24A, null,
                                    ResourceLocation.fromNamespaceAndPath("chineseweapons", "textures/item/forging_notes.png"), 0.9f,
                                    "middle");

                            var guideBookItem = BuiltInRegistries.ITEM.get(
                                    ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book"));
                            if (guideBookItem != Items.AIR) {
                                var bookStack = new ItemStack(guideBookItem);

                                // get
                                var bookComponentType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(
                                        ResourceLocation.fromNamespaceAndPath("patchouli", "book"));

                                if (bookComponentType != null) {
                                    @SuppressWarnings("unchecked")
                                    var typed = (DataComponentType<ResourceLocation>) bookComponentType;
                                    bookStack.set(typed, ResourceLocation.fromNamespaceAndPath("chineseweapons", "forging_notes"));
                                }

                                out.accept(bookStack);
                            }
                        }
                    }
            ).build());
}
