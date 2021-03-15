package com.bokmcdok.wheat;

import com.bokmcdok.wheat.block.ModBlockEventHandler;
import com.bokmcdok.wheat.entity.creature.ModLivingEntityEventHandler;
import com.bokmcdok.wheat.entity.creature.villager.wandering_trader.ModWanderingTraderEventHandler;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

public class ModForgeEventHandler {
    private final ModLivingEntityEventHandler mLivingEntityEventHandler;
    private final ModWanderingTraderEventHandler mWanderingTraderEventHandler;
    private final ModBlockEventHandler mBlockEventHandler;

    /**
     * Construction
     */
    public ModForgeEventHandler(ModTagRegistrar tagRegistrar) {
        mLivingEntityEventHandler = new ModLivingEntityEventHandler(tagRegistrar);
        mWanderingTraderEventHandler = new ModWanderingTraderEventHandler();
        mBlockEventHandler = new ModBlockEventHandler(tagRegistrar);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterDimensionsEvent);

        MinecraftForge.EVENT_BUS.addListener(this::onAttackEntityEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJoinWorldEvent);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHurtEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingUpdateEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onVillagerTradesEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onWandererTradesEvent);

        MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
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
        mLivingEntityEventHandler.onEntityJoinWorldEvent(event);
    }

    /**
     * Fired when a living entity does an update.
     * @param event The event data.
     */
    private void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        mLivingEntityEventHandler.onLivingUpdateEvent(event);
    }

    /**
     * Fired when the player attacks an entity.
     * @param event The event data.
     */
    private void onAttackEntityEvent(AttackEntityEvent event) {
        mLivingEntityEventHandler.onAttackEntityEvent(event);
    }

    /**
     * Fired when a living entity is hurt.
     * @param event The event data.
     */
    private void OnLivingHurtEvent(LivingHurtEvent event) {
        mLivingEntityEventHandler.OnLivingHurtEvent(event);
    }

    /**
     * Fired when a villager creates its trades.
     * @param event The event data.
     */
    private void onVillagerTradesEvent(VillagerTradesEvent event) {
        mLivingEntityEventHandler.onVillagerTradesEvent(event);
    }

    /**
     * Fired when a wandering trader creates its trades.
     * @param event The event data.
     */
    private void onWandererTradesEvent(WandererTradesEvent event) {
        mWanderingTraderEventHandler.onWandererTradesEvent(event);
    }

    /**
     * Fired when a player right-clicks on a block.
     * @param event The event data.
     */
    private void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        mBlockEventHandler.onRightClickBlock(event);
    }
}
