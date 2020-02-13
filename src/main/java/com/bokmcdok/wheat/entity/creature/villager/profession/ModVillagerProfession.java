package com.bokmcdok.wheat.entity.creature.villager.profession;

import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ModVillagerProfession extends net.minecraftforge.registries.ForgeRegistryEntry<VillagerProfession>{
    private final ResourceLocation mLocation;
    private Map<TradeLevel, List<ModVillagerTradeBuilder>> mTrades = Maps.newHashMap();
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

    public void addTrade(TradeLevel level, ModVillagerTradeBuilder trade) {
        mTrades.computeIfAbsent(level, k -> Lists.newArrayList());

        mTrades.get(level).add(trade);
    }

    public void setPOI(String poi) {
        mPoi = new ResourceLocation(poi);
    }

    public void setGifts(ResourceLocation lootTable) {
        mGifts = lootTable;
    }

    public ResourceLocation getLocation() { return mLocation; }

    public List<ModVillagerTradeBuilder> getTrades(TradeLevel level) {
        return mTrades.get(level);
    }

    public ResourceLocation getPOI() {
        return mPoi;
    }

    public ResourceLocation getGifts() {
        return mGifts;
    }

    public String toString() {
        return mLocation.getPath();
    }
}
