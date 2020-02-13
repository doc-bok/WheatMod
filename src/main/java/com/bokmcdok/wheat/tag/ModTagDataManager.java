package com.bokmcdok.wheat.tag;

import com.bokmcdok.wheat.data.ModDataManager;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ModTagDataManager extends ModDataManager<ModTag> {

    /**
     * Convert the JSON data to a tag.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return A new tag containing the list of possible values.
     */
    @Override
    protected ModTag deserialize(ResourceLocation location, JsonObject json) {
        Set<ResourceLocation> values = Sets.newHashSet();
        JsonArray mutations = JSONUtils.getJsonArray(json, "values");
        for (JsonElement i : mutations) {
            values.add(new ResourceLocation(i.getAsString()));
        }

        return new ModTag(values);
    }
}
