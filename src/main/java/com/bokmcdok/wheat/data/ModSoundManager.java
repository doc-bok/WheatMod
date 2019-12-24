package com.bokmcdok.wheat.data;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSoundManager extends ModDataManager<SoundEvent> {
    @Override
    protected SoundEvent deserialize(ResourceLocation location, JsonObject json) {
        return new SoundEvent(location).setRegistryName(location);
    }
}
