package net.mirrorloong.chineseweapons.mixin;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.mirrorloong.chineseweapons.fabric.FabricTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the eight master-level armourer forging-stone trades after the vanilla
 * listing selection. Fabric's {@code TradeOfferHelper} can only extend the
 * random listing pool, so a guaranteed set needs this tail hook.
 */
@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Inject(method = "updateTrades", at = @At("TAIL"))
    private void chineseweapons$addMasterTrades(CallbackInfo ci) {
        Villager self = (Villager) (Object) this;
        VillagerData data = self.getVillagerData();
        if (!FabricTrades.isMasterArmourer(data.getProfession(), data.getLevel())) {
            return;
        }

        MerchantOffers offers = self.getOffers();
        for (MerchantOffer offer : FabricTrades.masterOffers()) {
            boolean present = false;
            for (MerchantOffer existing : offers) {
                if (existing.getResult().getItem() == offer.getResult().getItem()) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                offers.add(offer);
            }
        }
    }
}
