package com.bokmcdok.wheat.entity.creature.villager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;

public class ModMemoryModuleRegistrar {
    public final static String BREED_TARGET = "docwheat:breed_target";
    public final static String INTERACTION_TARGET = "docwheat:interaction_target";

    /**
     * Construction
     */
    public ModMemoryModuleRegistrar() {
        FMLJavaModLoadingContext.get().getModEventBus()
                .addGenericListener(MemoryModuleType.class, this::onRegistryEvent);
    }

    /**
     * Get a specific memory module.
     * @param registryName The registry name of the module.
     * @return The memory module requested.
     */
    public MemoryModuleType<?> getMemoryModule(String registryName) {
        IForgeRegistry<MemoryModuleType<?>> memoryModuleTypeRegistry = ForgeRegistries.MEMORY_MODULE_TYPES;
        return memoryModuleTypeRegistry.getValue(new ResourceLocation(registryName));
    }

    /**
     * Register any new memory modules.
     * @param event The event data.
     */
    private void onRegistryEvent(RegistryEvent.Register<MemoryModuleType<?>> event) {
        event.getRegistry().register(new MemoryModuleType<VillagerEntity>(Optional.empty()).setRegistryName(BREED_TARGET));
        event.getRegistry().register(new MemoryModuleType<LivingEntity>(Optional.empty()).setRegistryName(INTERACTION_TARGET));
    }
}