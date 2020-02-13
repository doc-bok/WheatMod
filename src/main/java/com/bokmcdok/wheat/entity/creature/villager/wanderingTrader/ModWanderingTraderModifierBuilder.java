package com.bokmcdok.wheat.entity.creature.villager.wanderingTrader;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import com.google.common.collect.Lists;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModWanderingTraderModifierBuilder {
    private final List<ModVillagerTradeBuilder> mGenericTrades;
    private final List<ModVillagerTradeBuilder> mRareTrades;
    private final List<ResourceLocation> mRemove;

    /**
     * Construction.
     */
    public ModWanderingTraderModifierBuilder() {
        mGenericTrades = Lists.newArrayList();
        mRareTrades = Lists.newArrayList();
        mRemove = Lists.newArrayList();
    }

    /**
     * Add a new generic trade.
     * @param trade The trade to add.
     */
    public void addGenericTrade(ModVillagerTradeBuilder trade) {
        mGenericTrades.add(trade);
    }

    /**
     * Add a new rare trade.
     * @param trade The trade to add.
     */
    public void addRareTrade(ModVillagerTradeBuilder trade) {
        mRareTrades.add(trade);
    }

    /**
     * Remove an item from trades.
     * @param registryKey The registry key of the item to remove.
     */
    public void removeItem(String registryKey) {
        mRemove.add(new ResourceLocation(registryKey));
    }

    /**
     * Build the trade modifier.
     * @return A new trade modifier instance.
     */
    public ModWanderingTraderModifier build() {
        ModIngredientSupplier supplier = new ModIngredientSupplier(mRemove.toArray(new ResourceLocation[0]));
        return new ModWanderingTraderModifier(mGenericTrades, mRareTrades, new LazyValue<>(supplier));
    }
}
