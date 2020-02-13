package com.bokmcdok.wheat;

import com.bokmcdok.wheat.entity.creature.villager.ModVillagerEventHandler;
import com.bokmcdok.wheat.entity.creature.villager.wandering_trader.ModWanderingTraderEventHandler;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

public class ModForgeEventHandler {

    private final ModVillagerEventHandler mVillagerEventHandler;
    private final ModWanderingTraderEventHandler mWanderingTraderEventHandler;

    /**
     * Construction
     */
    public ModForgeEventHandler() {
        ModTagDataManager itemTagManager = new ModTagDataManager();
        itemTagManager.loadDataEntries("tags/items");   

        mVillagerEventHandler = new ModVillagerEventHandler(itemTagManager);
        mWanderingTraderEventHandler = new ModWanderingTraderEventHandler();

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterDimensionsEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJoinWorldEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingUpdateEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onVillagerTradesEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onWandererTradesEvent);
    }

    /**
     * Register dimension types.
     * @param event The event data.
     */
    protected void onRegisterDimensionsEvent(RegisterDimensionsEvent event) {
        ForgeRegistry<ModDimension> dimensionRegistry = RegistryManager.ACTIVE.getRegistry(GameData.MODDIMENSIONS);
        for (ModDimension i : dimensionRegistry.getValues()) {
            if (DimensionType.byName(i.getRegistryName()) == null) {
                DimensionManager.registerDimension(i.getRegistryName(), i, null, true);
            }
        }
    }

    /**
     * Fired whenever an entity joins the world.
     * @param event The event data.
     */
    private void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        mVillagerEventHandler.onEntityJoinWorldEvent(event);
    }

    /**
     * Fired when a living entity does an update.
     * @param event The event data.
     */
    private void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        mVillagerEventHandler.onLivingUpdateEvent(event);
    }

    /**
     * Fired when a villager creates its trades.
     * @param event The event data.
     */
    private void onVillagerTradesEvent(VillagerTradesEvent event) {
        mVillagerEventHandler.onVillagerTradesEvent(event);
    }

    /**
     * Fired when a wandering trader creates its trades.
     * @param event The event data.
     */
    private void onWandererTradesEvent(WandererTradesEvent event) {
        mWanderingTraderEventHandler.onWandererTradesEvent(event);
    }
}
