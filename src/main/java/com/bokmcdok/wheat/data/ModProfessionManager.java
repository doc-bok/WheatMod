package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.villager.ModVillagerProfession;
import com.bokmcdok.wheat.villager.ModVillagerTrade;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModProfessionManager extends ModDataManager<ModVillagerProfession> {
    @Override
    protected ModVillagerProfession deserialize(ResourceLocation location, JsonObject json) {
        ModVillagerProfession profession = new ModVillagerProfession(location);

        setString(profession, json, "poi", (x, value) -> x.setPOI(value));
        setResourceLocation(profession, json, "gifts", (x, value) -> x.setGifts(value));

        JsonObject trades = JSONUtils.getJsonObject(json, "trades");
        setArray(profession, trades, "novice", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.NOVICE, x, value);
        });

        setArray(profession, trades, "apprentice", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.APPRENTICE, x, value);
        });

        setArray(profession, trades, "journeyman", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.JOURNEYMAN, x, value);
        });

        setArray(profession, trades, "expert", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.EXPERT, x, value);
        });

        setArray(profession, trades, "master", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.MASTER, x, value);
        });

        return profession;
    }

    protected void deserializeTrade(ModVillagerProfession.TradeLevel level, ModVillagerProfession profession, JsonObject json) {
        ModVillagerTrade trade = new ModVillagerTrade();
        setString(trade, json, "type", (x, value) -> x.tradeType(value));
        setResourceLocation(trade, json, "item", (x, value) -> x.item(value));
        setInt(trade, json, "cost", (x, value) -> x.cost(value));
        setInt(trade, json, "quantity", (x, value) -> x.quantity(value));
        setInt(trade, json, "max_trades", (x, value) -> x.maxTrades(value));
        setInt(trade, json, "XP", (x, value) -> x.xp(value));
        profession.addTrade(level, trade);
    }
}
