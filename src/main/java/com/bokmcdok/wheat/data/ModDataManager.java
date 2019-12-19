package com.bokmcdok.wheat.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ModDataManager<T> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final ModJsonLoader mJsonLoader = new ModJsonLoader();
    private Map<ResourceLocation, T> mEntries = new HashMap<>();

    public T getEntry(ResourceLocation location) {
        return mEntries.get(location);
    }

    public Collection<T> getAllEntries() {
        return mEntries.values();
    }

    /**
     * Load items from a specified folder.
     * @param resourceManager The resource manager to use to get the JSON files.
     * @param folder The folder to load from.
     */
    public void loadEntries(IResourceManager resourceManager, String folder) {
        Map<ResourceLocation, JsonObject> containerResources = mJsonLoader.loadJsonResources(resourceManager, folder);
        for(Map.Entry<ResourceLocation, JsonObject> i : containerResources.entrySet()) {
            ResourceLocation resourceLocation = i.getKey();
            if (resourceLocation.getPath().startsWith("_")) { continue; }

            try {
                T entry = deserialize(resourceLocation, i.getValue());
                if (entry == null) {
                    LOGGER.info("Skipping loading entry {} as it's serializer returned null", resourceLocation);
                    continue;
                }

                mEntries.put(resourceLocation, entry);

            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading entry {}", resourceLocation, exception);
            }
        }

        LOGGER.info("Loaded {} entries", mEntries.values().size());
    }

    protected abstract T deserialize(ResourceLocation location, JsonObject json);
}
