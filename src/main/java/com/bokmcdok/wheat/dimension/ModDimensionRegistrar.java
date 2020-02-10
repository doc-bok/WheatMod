package com.bokmcdok.wheat.dimension;

import com.bokmcdok.wheat.dimension.wheat.ModWheatDimension;
import com.bokmcdok.wheat.dimension.wheat.ModWheatDimensionFactory;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModDimensionRegistrar {
    /**
     * Construction
     */
    public ModDimensionRegistrar() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ModDimension.class, this::onDimensionRegistryEvent);
    }

    /**
     * Register new dimensions.
     * @param event The registry event.
     */
    private void onDimensionRegistryEvent(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register(new ModWheatDimensionFactory().setRegistryName(ModWheatDimension.NAME));
    }
}
