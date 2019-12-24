package com.bokmcdok.wheat.villager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ModVillagerProfession {
    private final ResourceLocation mLocation;
    private Map<TradeLevel, List<ModVillagerTrade>> mTrades = Maps.newHashMap();
    private ResourceLocation mPoi;
    private ResourceLocation mGifts;

    public enum TradeLevel {
        NOVICE,
        APPRENTICE,
        JOURNEYMAN,
        EXPERT,
        MASTER
    }

    public ModVillagerProfession(ResourceLocation location) {
        mLocation = location;
    }

    public void addTrade(TradeLevel level, ModVillagerTrade trade) {
        if (mTrades.get(level) == null) {
            mTrades.put(level, Lists.newArrayList());
        }

        mTrades.get(level).add(trade);
    }

    public void setPOI(String poi) {
        mPoi = new ResourceLocation(poi);
    }

    public void setGifts(ResourceLocation lootTable) {
        mGifts = lootTable;
    }

    public ResourceLocation getLocation() { return mLocation; }

    public List<ModVillagerTrade> getTrades(TradeLevel level) {
        return mTrades.get(level);
    }

    public ResourceLocation getPOI() {
        return mPoi;
    }

    public ResourceLocation getGifts() {
        return mGifts;
    }
}
