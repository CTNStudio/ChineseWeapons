package net.mirrorloong.chineseweapons.init;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChineseweaponsModTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT, 5), new ItemStack(ChineseweaponsModItems.EarlyTangDynastyMingGuangArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.FootmenArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.LateTangDynastyMingGuangArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.MiddleTangDynastyMingGuangArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.MountainCharacterArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.BlackChuiArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD,10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.TangDynastyInfantryArmorforgingStone.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD,10), new ItemStack(Items.IRON_INGOT), new ItemStack(ChineseweaponsModItems.FineScaleArmorforgingStone.get()), 10, 5, 0.05f));
        }
    }
}

