package com.bokmcdok.wheat.entity.creature.villager.trade;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.data.ModDataManager;
import com.google.gson.JsonObject;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModVillagerTradeModifierDataManager extends ModDataManager<ModVillagerTradeModifier> {

    /**
     * Get the modifier for the specified profession.
     * @param profession The profession to modify.
     * @return The modifier for the profession.
     */
    public ModVillagerTradeModifier getModifier(VillagerProfession profession) {

        //  Funky stuff because modded professions have a namespace but vanilla professions don't.
        String name = profession.toString();
        if (name.contains(":")) {
            return getEntry(new ResourceLocation(name));
        }

        return getEntry(new ResourceLocation(WheatMod.MOD_ID, name));
    }

    /**
     * Deserialize a trade modifier.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return The newly built trade modifier.
     */
    @Override
    protected ModVillagerTradeModifier deserialize(ResourceLocation location, JsonObject json) {
        ModVillagerTradeModifierBuilder builder = new ModVillagerTradeModifierBuilder();
        setStringArray(builder, json, "remove", ModVillagerTradeModifierBuilder::removeItem);

        JsonObject add = JSONUtils.getJsonObject(json, "add");
        setObjectArray(builder, add, "novice", (x, value) -> {
            deserializeTrade(1, x, value);
        });

        setObjectArray(builder, add, "apprentice", (x, value) -> {
            deserializeTrade(2, x, value);
        });

        setObjectArray(builder, add, "journeyman", (x, value) -> {
            deserializeTrade(3, x, value);
        });

        setObjectArray(builder, add, "expert", (x, value) -> {
            deserializeTrade(4, x, value);
        });

        setObjectArray(builder, add, "master", (x, value) -> {
            deserializeTrade(5, x, value);
        });

        return builder.build();
    }

    /**
     * Deserialize a trade.
     * @param level The level of the trade.
     * @param builder The modifier builder.
     * @param json The JSON data to deserialize.
     */
    protected void deserializeTrade(int level, ModVillagerTradeModifierBuilder builder, JsonObject json) {
        ModVillagerTradeBuilder trade = new ModVillagerTradeBuilder();
        setString(trade, json, "type", ModVillagerTradeBuilder::tradeType);
        setResourceLocation(trade, json, "item", ModVillagerTradeBuilder::item);
        setInt(trade, json, "cost", ModVillagerTradeBuilder::cost);
        setInt(trade, json, "quantity", ModVillagerTradeBuilder::quantity);
        setInt(trade, json, "max_trades", ModVillagerTradeBuilder::maxTrades);
        setInt(trade, json, "XP", ModVillagerTradeBuilder::xp);
        builder.addTrade(level, trade);
    }
}
