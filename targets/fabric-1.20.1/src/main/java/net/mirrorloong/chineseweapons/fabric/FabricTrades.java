package net.mirrorloong.chineseweapons.fabric;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModItems;

/**
 * Armourer master trades for the forging stones. Fabric's
 * {@code TradeOfferHelper} only appends to the vanilla listing pool, from which
 * {@code Villager.updateTrades} randomly picks two entries, so the trades would
 * mostly never appear. {@code VillagerMixin} instead adds all eight offers
 * directly, matching the Forge/NeoForge {@code VillagerTradesEvent} behaviour.
 */
public final class FabricTrades {
    private FabricTrades() {
    }

    /** Creates a fresh set of master offers; callers own the instances. */
    public static List<MerchantOffer> masterOffers() {
        List<MerchantOffer> offers = new ArrayList<>(8);
        offers.add(offer(ChineseweaponsModItems.EarlyTangDynastyMingGuangArmorforgingStone.get(), 5));
        offers.add(offer(ChineseweaponsModItems.FootmenArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.LateTangDynastyMingGuangArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.MiddleTangDynastyMingGuangArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.MountainCharacterArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.BlackChuiArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.TangDynastyInfantryArmorforgingStone.get()));
        offers.add(offer(ChineseweaponsModItems.FineScaleArmorforgingStone.get()));
        return offers;
    }

    public static boolean isMasterArmourer(VillagerProfession profession, int level) {
        return profession == VillagerProfession.ARMORER && level >= 4;
    }

    private static MerchantOffer offer(Item result) {
        return offer(result, 1);
    }

    private static MerchantOffer offer(Item result, int ironCost) {
        return new MerchantOffer(
                new ItemStack(Items.EMERALD, 10),
                new ItemStack(Items.IRON_INGOT, ironCost),
                new ItemStack(result),
                10,
                5,
                0.05F);
    }
}
