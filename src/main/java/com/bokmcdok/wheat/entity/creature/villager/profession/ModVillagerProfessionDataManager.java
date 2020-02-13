package com.bokmcdok.wheat.entity.creature.villager.profession;

import com.bokmcdok.wheat.data.ModDataManager;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import com.google.gson.JsonObject;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModVillagerProfessionDataManager extends ModDataManager<net.minecraftforge.registries.ForgeRegistryEntry<VillagerProfession>> {

    /**
     * Load custom professions and add references to vanilla professions.
     */
    public void loadProfessions() {
        loadDataEntries("villager/profession");
        addCustomEntry("minecraft:none", VillagerProfession.NONE);
        addCustomEntry("minecraft:armorer", VillagerProfession.ARMORER);
        addCustomEntry("minecraft:butcher", VillagerProfession.BUTCHER);
        addCustomEntry("minecraft:cartographer", VillagerProfession.CARTOGRAPHER);
        addCustomEntry("minecraft:cleric", VillagerProfession.CLERIC);
        addCustomEntry("minecraft:farmer", VillagerProfession.FARMER);
        addCustomEntry("minecraft:fisherman", VillagerProfession.FISHERMAN);
        addCustomEntry("minecraft:fletcher", VillagerProfession.FLETCHER);
        addCustomEntry("minecraft:leatherworker", VillagerProfession.LEATHERWORKER);
        addCustomEntry("minecraft:librarian", VillagerProfession.LIBRARIAN);
        addCustomEntry("minecraft:mason", VillagerProfession.MASON);
        addCustomEntry("minecraft:nitwit", VillagerProfession.NITWIT);
        addCustomEntry("minecraft:shepherd", VillagerProfession.SHEPHERD);
        addCustomEntry("minecraft:toolsmith", VillagerProfession.TOOLSMITH);
        addCustomEntry("minecraft:weaponsmith", VillagerProfession.WEAPONSMITH);
    }

    /**
     * Deserialize a custom profession.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return
     */
    @Override
    protected net.minecraftforge.registries.ForgeRegistryEntry<VillagerProfession> deserialize(ResourceLocation location, JsonObject json) {
        ModVillagerProfession profession = new ModVillagerProfession(location);

        setString(profession, json, "poi", ModVillagerProfession::setPOI);
        setResourceLocation(profession, json, "gifts", ModVillagerProfession::setGifts);

        JsonObject trades = JSONUtils.getJsonObject(json, "trades");
        setObjectArray(profession, trades, "novice", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.NOVICE, x, value);
        });

        setObjectArray(profession, trades, "apprentice", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.APPRENTICE, x, value);
        });

        setObjectArray(profession, trades, "journeyman", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.JOURNEYMAN, x, value);
        });

        setObjectArray(profession, trades, "expert", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.EXPERT, x, value);
        });

        setObjectArray(profession, trades, "master", (x, value) -> {
            deserializeTrade(ModVillagerProfession.TradeLevel.MASTER, x, value);
        });

        return profession;
    }

    /**
     * Deserialize a trade.
     * @param level The level of the trade.
     * @param profession The profession this trade belongs to.
     * @param json The JSON data to deserialize.
     */
    protected void deserializeTrade(ModVillagerProfession.TradeLevel level, ModVillagerProfession profession, JsonObject json) {
        ModVillagerTradeBuilder trade = new ModVillagerTradeBuilder();
        setString(trade, json, "type", ModVillagerTradeBuilder::tradeType);
        setResourceLocation(trade, json, "item", ModVillagerTradeBuilder::item);
        setInt(trade, json, "cost", ModVillagerTradeBuilder::cost);
        setInt(trade, json, "quantity", ModVillagerTradeBuilder::quantity);
        setInt(trade, json, "max_trades", ModVillagerTradeBuilder::maxTrades);
        setInt(trade, json, "XP", ModVillagerTradeBuilder::xp);
        profession.addTrade(level, trade);
    }
}
