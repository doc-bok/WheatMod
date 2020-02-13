package com.bokmcdok.wheat.entity.creature.villager.crops;

import com.bokmcdok.wheat.data.ModDataManager;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModVillagerCropsDataManager extends ModDataManager<ModVillagerCrops> {
    @Override
    protected ModVillagerCrops deserialize(ResourceLocation location, JsonObject json) {
        ModVillagerCropsBuilder builder = new ModVillagerCropsBuilder();
        setObjectArray(builder, json, "crops", this::deserializeCrop);
        return builder.build();
    }

    private void deserializeCrop(ModVillagerCropsBuilder builder, JsonObject json) {
        String seed = JSONUtils.getString(json, "seed");
        String crop = JSONUtils.getString(json, "crop");
        builder.addCrop(new ResourceLocation(seed), new ResourceLocation(crop));
    }
}
