package com.bokmcdok.wheat.entity.creature.villager.trade;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ModVillagerTradeModifierBuilder {
    private final Map<Integer, List<ModVillagerTradeBuilder>> mTrades;
    private final List<ResourceLocation> mRemove;

    /**
     * Construction
     */
    public ModVillagerTradeModifierBuilder() {
        mTrades = Maps.newHashMap();
        for(int i = 1; i <=5; ++i) {
            mTrades.put(i, Lists.newArrayList());
        }

        mRemove = Lists.newArrayList();
    }

    /**
     * Add a new trade to the profession.
     * @param level The level of the trade.
     * @param trade The actual trade.
     */
    public void addTrade(int level, ModVillagerTradeBuilder trade) {
        mTrades.get(level).add(trade);
    }

    /**
     * Remove an item from trades.
     * @param registryKey The registry key of the item.
     */
    public void removeItem(String registryKey) {
        mRemove.add(new ResourceLocation(registryKey));
    }

    /**
     * Build the trade modifier.
     * @return A new trade modifier instance.
     */
    public ModVillagerTradeModifier build() {
        ModIngredientSupplier supplier = new ModIngredientSupplier(mRemove.toArray(new ResourceLocation[0]));
        return new ModVillagerTradeModifier(mTrades, new LazyValue<>(supplier));
    }
}
