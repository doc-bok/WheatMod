package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.WheatMod;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ModDataManager<T> {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final ModJsonLoader JSON_LOADER = new ModJsonLoader();
    private static ModResourceManager RESOURCE_MANAGER = new ModResourceManager(ResourcePackType.SERVER_DATA, WheatMod.MOD_ID);

    private Map<ResourceLocation, T> mEntries = new HashMap<>();

    /**
     * Get a single entry based on a Resource Location
     * @param location The location of the entry.
     * @return A single entry.
     */
    public T getEntry(ResourceLocation location) {
        return mEntries.get(location);
    }

    /**
     * Get a collection of all entries.
     * @return A collection of entries.
     */
    public Collection<T> getAllEntries() {
        return mEntries.values();
    }

    /**
     * Load items from a specified folder.
     * @param folder The folder to load from.
     */
    public void loadEntries(String folder) {
        Map<ResourceLocation, JsonObject> containerResources = JSON_LOADER.loadJsonResources(RESOURCE_MANAGER, folder);
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

    /**
     * Add a custom non-data-driven entry to the list.
     * @param location Location to add the entry.
     * @param entry The entry to add.
     */
    protected void addCustomEntry(String location, T entry) {
        mEntries.put(new ResourceLocation(location), entry);
    }

    /**
     * Deserialize a color into an integer
     * @param json The JSON object from the file
     */
    protected int deserializeColor(JsonObject json) {
        if (JSONUtils.hasField(json, "color")) {
            JsonObject color = JSONUtils.getJsonObject(json, "color");
            if (JSONUtils.hasField(color, "r")) {
                int r = JSONUtils.getInt(color, "r");
                int g = JSONUtils.getInt(color, "g");
                int b = JSONUtils.getInt(color, "b");

                return (r & 255) << 16 | (g & 255) << 8 | b & 255;
            } else {
                String type = JSONUtils.getString(color, "type");
                if ("spruce".equals(type)) {
                    return FoliageColors.getSpruce();
                } else if ("birch".equals(type)) {
                    return FoliageColors.getBirch();
                } else if ("oak".equals(type)) {
                    return FoliageColors.getDefault();
                } else if ("foliage".equals(type)) {
                    float temperature = JSONUtils.getFloat(color, "temperature");
                    float humidity = JSONUtils.getFloat(color, "humidity");

                    return FoliageColors.get(temperature, humidity);
                } else if ("grass".equals(type)) {
                    float temperature = JSONUtils.getFloat(color, "temperature");
                    float humidity = JSONUtils.getFloat(color, "humidity");

                    return GrassColors.get(temperature, humidity);
                }
            }
        }

        return -1;
    }

    /**
     * Converts a JSON object to a map color.
     * @param json The object to parse
     * @return A map color if one is present in the JSON data.
     */
    protected MaterialColor deserializedMapColor(JsonObject json) {
        if (JSONUtils.hasField(json, "map_color")) {
            String name = JSONUtils.getString(json, "map_color").toUpperCase();

            try {
                Field field = MaterialColor.class.getDeclaredField(name);
                return (MaterialColor) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                LOGGER.error("Material Color {} not supported", name, exception);
            }
        }

        return null;
    }

    /**
     * Get the mod block for a BlockItem or BlockNamedItem.
     * @param blockName The registry name of a block.
     * @return The instance of the block.
     */
    protected Block getBlock(String blockName) {
        return Registry.BLOCK.getOrDefault(new ResourceLocation(blockName));
    }

    /**
     * Deserialize the JSON data to an object.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return A new object based on the JSON data.
     */
    protected abstract T deserialize(ResourceLocation location, JsonObject json);
}
