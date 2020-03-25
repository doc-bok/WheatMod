package com.bokmcdok.wheat.supplier;

import net.minecraft.util.SoundEvent;

public class ModSoundEventSupplier extends ModRegistrySupplier<SoundEvent> {

    /**
     * Construction
     * @param registryName The registry name of the entity type.
     */
    public ModSoundEventSupplier(String registryName) {
        super(SoundEvent.class, registryName);
    }
}
