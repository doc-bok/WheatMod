package com.bokmcdok.wheat.entity.creature.villager.trade;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraftforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Map;


public class ModVillagerTradeModifier {
    private final Map<Integer, List<ModVillagerTradeBuilder>> mTrades;
    private final LazyValue<Ingredient> mRemove;

    /**
     * Construction
     * @param trades List of new trades to add to the profession.
     * @param remove List of items to remove from trading.
     */
    public ModVillagerTradeModifier(Map<Integer, List<ModVillagerTradeBuilder>> trades, LazyValue<Ingredient> remove) {
        mTrades = trades;
        mRemove = remove;
    }

    /**
     * Update the trades to use the modified values.
     * @param event The event data.
     */
    public void onVillagerTradesEvent(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

        for (int i = 1; i <= 5; ++i) {
            List<VillagerTrades.ITrade> levelTrades = trades.get(i);
            levelTrades.removeIf(x -> {
                MerchantOffer offer = x.getOffer(null, null);
                return mRemove.getValue().test(offer.getSellingStack());
            });

            for (ModVillagerTradeBuilder newTrade : mTrades.get(i)) {
                trades.get(i).add(newTrade.build());
            }
        }
    }
}
