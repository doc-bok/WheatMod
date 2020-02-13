package com.bokmcdok.wheat.entity.creature.villager.wanderingTrader;

import net.minecraftforge.event.village.WandererTradesEvent;

public class ModWanderingTraderEventHandler {
    private final ModWanderingTraderModifier mWanderingTraderModifier;

    /**
     * Construction
     */
    public ModWanderingTraderEventHandler() {
        ModWanderingTraderModifierDataManager dataManager = new ModWanderingTraderModifierDataManager();
        dataManager.loadDataEntries("villager/wandering_trader");
            mWanderingTraderModifier = dataManager.getEntry("docwheat:wandering_trader");
    }

    /**
     * Replace wandering trader trades with modded items.
     * @param event The event data.
     */
    public void onWandererTradesEvent(WandererTradesEvent event) {
        mWanderingTraderModifier.onWandererTradesEvent(event);
    }
}
