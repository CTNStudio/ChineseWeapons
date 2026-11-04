package net.mirrorloong.chineseweapons.fabric;

import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

/**
 * Armourer journeyman trades for the forging stones. NeoForge registers them
 * through {@code VillagerTradesEvent}; Fabric offers the same customisation
 * through {@code TradeOfferHelper}.
 */
public final class FabricTrades {
    private FabricTrades() {
    }

    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 4, factories -> {
            factories.add((entity, random) -> offer(
                    ChineseweaponsModItems.EarlyTangDynastyMingGuangArmorforgingStone.get(), 5));
            factories.add((entity, random) -> offer(ChineseweaponsModItems.FootmenArmorforgingStone.get()));
            factories.add((entity, random) -> offer(
                    ChineseweaponsModItems.LateTangDynastyMingGuangArmorforgingStone.get()));
            factories.add((entity, random) -> offer(
                    ChineseweaponsModItems.MiddleTangDynastyMingGuangArmorforgingStone.get()));
            factories.add((entity, random) -> offer(
                    ChineseweaponsModItems.MountainCharacterArmorforgingStone.get()));
            factories.add((entity, random) -> offer(ChineseweaponsModItems.BlackChuiArmorforgingStone.get()));
            factories.add((entity, random) -> offer(
                    ChineseweaponsModItems.TangDynastyInfantryArmorforgingStone.get()));
            factories.add((entity, random) -> offer(ChineseweaponsModItems.FineScaleArmorforgingStone.get()));
        });
    }

    private static MerchantOffer offer(Item result) {
        return offer(result, 1);
    }

    private static MerchantOffer offer(Item result, int ironCost) {
        return new MerchantOffer(
                new ItemCost(Items.EMERALD, 10),
                Optional.of(new ItemCost(Items.IRON_INGOT, ironCost)),
                new ItemStack(result),
                10,
                5,
                0.05F);
    }
}
