package com.bokmcdok.wheat.entity.creature.villager.wandering_trader;

import com.bokmcdok.wheat.data.ModDataManager;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public class ModWanderingTraderModifierDataManager extends ModDataManager<ModWanderingTraderModifier> {

    @Override
    protected ModWanderingTraderModifier deserialize(ResourceLocation location, JsonObject json) {
        ModWanderingTraderModifierBuilder builder = new ModWanderingTraderModifierBuilder();
        setStringArray(builder, json, "remove", ModWanderingTraderModifierBuilder::removeItem);
        setObjectArray(builder, json, "generic", (x, value) -> {
            ModVillagerTradeBuilder trade = deserializeTrade(value);
            x.addGenericTrade(trade);
        });

        setObjectArray(builder, json, "rare", (x, value) -> {
            ModVillagerTradeBuilder trade = deserializeTrade(value);
            x.addRareTrade(trade);
        });

        return builder.build();
    }

    /**
     * Deserialize a trade.
     * @param json The JSON data to deserialize.
     */
    protected ModVillagerTradeBuilder deserializeTrade(JsonObject json) {
        ModVillagerTradeBuilder trade = new ModVillagerTradeBuilder();
        setString(trade, json, "type", ModVillagerTradeBuilder::tradeType);
        setResourceLocation(trade, json, "item", ModVillagerTradeBuilder::item);
        setInt(trade, json, "cost", ModVillagerTradeBuilder::cost);
        setInt(trade, json, "quantity", ModVillagerTradeBuilder::quantity);
        setInt(trade, json, "max_trades", ModVillagerTradeBuilder::maxTrades);
        setInt(trade, json, "XP", ModVillagerTradeBuilder::xp);
        return trade;
    }
}
