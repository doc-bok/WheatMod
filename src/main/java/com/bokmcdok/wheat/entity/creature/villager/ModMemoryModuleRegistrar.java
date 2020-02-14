package com.bokmcdok.wheat.entity.creature.villager;

import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

public class ModMemoryModuleRegistrar {

    /**
     * Construction
     */
    public ModMemoryModuleRegistrar() {
        FMLJavaModLoadingContext.get().getModEventBus()
                .addGenericListener(MemoryModuleType.class, this::onRegistryEvent);
    }

    /**
     * Register any new memory modules.
     * @param event The event data.
     */
    private void onRegistryEvent(RegistryEvent.Register<MemoryModuleType<?>> event) {
        event.getRegistry().register(new MemoryModuleType<VillagerEntity>(Optional.empty()).setRegistryName("docwheat:breed_target"));
    }
}