package com.bokmcdok.wheat.villager;

import com.bokmcdok.wheat.trade.ModEmeraldForItemsTrade;
import com.bokmcdok.wheat.trade.ModItemsForEmeraldsTrade;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ModVillagerTrade {
    private TradeType mType;
    private ResourceLocation mItem;
    private int mCost = 1;
    private int mQuantity = 1;
    private int mMaxTrades = 16;
    private int mXP = 1;

    public enum TradeType {
        BUY,
        SELL
    }

    public void tradeType(String type) {
        mType = TradeType.valueOf(type.toUpperCase());
    }

    public void item(ResourceLocation item) {
        mItem = item;
    }

    public void cost(int cost) {
        mCost = cost;
    }

    public void quantity(int quantity) {
        mQuantity = quantity;
    }

    public void maxTrades(int maxTrades) {
        mMaxTrades = maxTrades;
    }

    public void xp(int xp) {
        mXP = xp;
    }

    public VillagerTrades.ITrade build() {
        Optional<Item> item = Registry.ITEM.getValue(mItem);
        if (item.isPresent()) {
            switch (mType) {
                case BUY:
                    return new ModEmeraldForItemsTrade(item.get(), mQuantity, mMaxTrades, mXP);

                case SELL:
                    return new ModItemsForEmeraldsTrade(item.get(), mCost, mQuantity, mMaxTrades, mXP);
            }
        }

        return null;
    }
}
