package com.bokmcdok.wheat.supplier;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class ModEntityTypeSupplier implements Supplier<EntityType<?>> {
    private final ResourceLocation mRegistryName;

    /**
     * Construction
     * @param registryName The registry name of the entity type.
     */
    public ModEntityTypeSupplier(String registryName) {
        this(new ResourceLocation(registryName));
    }

    /**
     * Construction
     * @param registryName The registry name of the entity type.
     */
    public ModEntityTypeSupplier(ResourceLocation registryName) {
        mRegistryName = registryName;
    }

    /**
     * Get the entity type from the registry.
     * @return The Entity Type instance.
     */
    @Override
    public EntityType<?> get() {
        IForgeRegistry<EntityType<?>> registry = RegistryManager.ACTIVE.getRegistry(GameData.ENTITIES);
        return registry.getValue(mRegistryName);
    }
}
