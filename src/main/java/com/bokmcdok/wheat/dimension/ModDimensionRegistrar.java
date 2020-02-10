package com.bokmcdok.wheat.dimension;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.dimension.wheat.ModWheatDimensionFactory;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDimensionRegistrar {

    @ObjectHolder("docwheat:wheat_dimension")
    public static final ModDimension WHEAT_DIMENSION = null;

    /**
     * Register new dimensions.
     * @param event The registry event.
     */
    @SubscribeEvent
    public static void onDimensionRegistryEvent(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register(new ModWheatDimensionFactory().setRegistryName("docwheat:wheat_dimension"));
    }
}
