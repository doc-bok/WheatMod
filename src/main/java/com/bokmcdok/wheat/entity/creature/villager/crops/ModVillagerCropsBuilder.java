package com.bokmcdok.wheat.entity.creature.villager.crops;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModVillagerCropsBuilder {
    private final Map<ResourceLocation, ResourceLocation> mCrops;

    /**
     * Construction
     */
    public ModVillagerCropsBuilder() {
        mCrops = Maps.newHashMap();
    }

    /**
     * Add a new crop.
     * @param seed The seed that makes the crop.
     * @param crop The crop that grows after planting the seed.
     */
    public void addCrop(ResourceLocation seed, ResourceLocation crop) {
        mCrops.put(seed, crop);
    }

    /**
     * Build the villager crops.
     * @return A new villager crops instance.
     */
    public ModVillagerCrops build() {
        return new ModVillagerCrops(mCrops);
    }
}
