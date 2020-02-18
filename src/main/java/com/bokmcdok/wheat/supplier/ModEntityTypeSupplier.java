package com.bokmcdok.wheat.supplier;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class ModEntityTypeSupplier implements Supplier<EntityType<?>> {
    private final ResourceLocation mRegistryName;

    public ModEntityTypeSupplier(String registryName) {
        this(new ResourceLocation(registryName));
    }

    public ModEntityTypeSupplier(ResourceLocation registryName) {
        mRegistryName = registryName;
    }

    @Override
    public EntityType<?> get() {
        IForgeRegistry<EntityType<?>> registry = RegistryManager.ACTIVE.getRegistry(GameData.ENTITIES);
        return registry.getValue(mRegistryName);
    }
}
