package com.bokmcdok.wheat.entity.creature;

import com.bokmcdok.wheat.entity.creature.player.ModPlayerEntityEventHandler;
import com.bokmcdok.wheat.entity.creature.villager.ModVillagerEventHandler;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;

public class ModLivingEntityEventHandler {
    private final ModPlayerEntityEventHandler mPlayerEntityEventHandler;
    private final ModVillagerEventHandler mVillagerEventHandler;

    /**
     * Construction
     * @param itemTagDataManager Data manager holding the item tags.
     */
    public ModLivingEntityEventHandler(ModTagDataManager itemTagDataManager) {
        mPlayerEntityEventHandler = new ModPlayerEntityEventHandler();
        mVillagerEventHandler = new ModVillagerEventHandler(itemTagDataManager);
    }

    /**
     * Fired whenever an entity joins the world.
     * @param event The event data.
     */
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        mVillagerEventHandler.onEntityJoinWorldEvent(event);
    }

    /**
     * Fired when a villager creates its trades.
     * @param event The event data.
     */
    public void onVillagerTradesEvent(VillagerTradesEvent event) {
        mVillagerEventHandler.onVillagerTradesEvent(event);
    }

    /**
     * Fired when a living entity does an update.
     * @param event The event data.
     */
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        mPlayerEntityEventHandler.onLivingUpdateEvent(event);
        mVillagerEventHandler.onLivingUpdateEvent(event);
    }
}
