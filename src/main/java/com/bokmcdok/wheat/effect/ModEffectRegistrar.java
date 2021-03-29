package com.bokmcdok.wheat.effect;

import com.bokmcdok.wheat.block.ModBlockDataManager;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.Block;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModEffectRegistrar {

    /**
     * Construction
     */
    public ModEffectRegistrar() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Effect.class, this::onEffectRegistryEvent);
    }

    /**
     * Load and register any mod-specific effects.
     * @param event The event data.
     */
    private void onEffectRegistryEvent(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(
                new ModCharmEffect(46207131).setRegistryName("docwheat:charm")
        );
    }
}
