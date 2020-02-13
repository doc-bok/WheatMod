package com.bokmcdok.wheat.entity.creature.villager.wanderingTrader;

import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraftforge.event.village.WandererTradesEvent;

import java.util.List;

public class ModWanderingTraderModifier {
    private final List<ModVillagerTradeBuilder> mGenericTrades;
    private final List<ModVillagerTradeBuilder> mRareTrades;
    private final LazyValue<Ingredient> mRemove;

    /**
     * Construction
     * @param genericTrades New generic trades to add.
     * @param rareTrades New rare trades to add.
     * @param remove Items to remove from trades.
     */
    public ModWanderingTraderModifier(List<ModVillagerTradeBuilder> genericTrades, List<ModVillagerTradeBuilder> rareTrades, LazyValue<Ingredient> remove) {
        mGenericTrades = genericTrades;
        mRareTrades = rareTrades;
        mRemove = remove;
    }

    /**
     * Replace wandering trader trades with modified trades.
     * @param event The event data.
     */
    public void onWandererTradesEvent(WandererTradesEvent event) {
        List<VillagerTrades.ITrade> genericTrades = event.getGenericTrades();
        genericTrades.removeIf(x -> {
            MerchantOffer offer = x.getOffer(null, null);
            return mRemove.getValue().test(offer.getSellingStack());
        });

        for(ModVillagerTradeBuilder newTrade: mGenericTrades) {
            genericTrades.add(newTrade.build());
        }

        List<VillagerTrades.ITrade> rareTrades = event.getRareTrades();
        rareTrades.removeIf(x -> {
            MerchantOffer offer = x.getOffer(null, null);
            return mRemove.getValue().test(offer.getSellingStack());
        });

        for(ModVillagerTradeBuilder newTrade: mRareTrades) {
            rareTrades.add(newTrade.build());
        }
    }
}
