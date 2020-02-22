package com.bokmcdok.wheat.supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class ModSoundEventSupplier implements Supplier<SoundEvent> {
    private final ResourceLocation mRegistryName;

    /**
     * Construction
     * @param registryName The registry name of the entity type.
     */
    public ModSoundEventSupplier(String registryName) {
        this(new ResourceLocation(registryName));
    }

    /**
     * Construction
     * @param registryName The registry name of the entity type.
     */
    public ModSoundEventSupplier(ResourceLocation registryName) {
        mRegistryName = registryName;
    }

    /**
     * Get the entity type from the registry.
     * @return The Entity Type instance.
     */
    @Override
    public SoundEvent get() {
        IForgeRegistry<SoundEvent> registry = RegistryManager.ACTIVE.getRegistry(GameData.SOUNDEVENTS);
        return registry.getValue(mRegistryName);
    }
}
