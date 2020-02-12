package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.WheatMod;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ModDataManager<T> {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final ModJsonLoader JSON_LOADER = new ModJsonLoader();
    private static ModResourceManager ASSET_RESOURCE_MANAGER = new ModResourceManager(ResourcePackType.CLIENT_RESOURCES, WheatMod.MOD_ID);
    private static ModResourceManager DATA_RESOURCE_MANAGER = new ModResourceManager(ResourcePackType.SERVER_DATA, WheatMod.MOD_ID);

    private Map<ResourceLocation, T> mEntries = new HashMap<>();

    /**
     * Get a single entry based on a Resource Location
     * @param location The location of the entry.
     * @return A single entry.
     */
    public T getEntry(ResourceLocation location) {
        if (mEntries.containsKey(location)) {
            return mEntries.get(location);
        }

        return null;
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
    public void loadAssetEntries(String folder, String extension) {
        List<ResourceLocation> containerResources = listResources(ASSET_RESOURCE_MANAGER, folder, extension);
        for(ResourceLocation resourceLocation : containerResources) {
            if (resourceLocation.getPath().startsWith("_")) { continue; }

            try {
                T entry = deserialize(resourceLocation, null);
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
     * Load entries from the data pack (needed on server-side).
     * @param folder The folder to load the entries from.
     */
    public void loadDataEntries(String folder) {
        loadEntries(DATA_RESOURCE_MANAGER, folder);
    }

    /**
     * Load the entries.
     * @param resourceManager The resource manager to use - assets (client only), or data (server and client).
     * @param folder The folder to load the entries from.
     */
    protected void loadEntries(ModResourceManager resourceManager, String folder) {
        Map<ResourceLocation, JsonObject> containerResources = JSON_LOADER.loadJsonResources(resourceManager, folder);
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
     * List resources with the specified extension.
     * @param resourceManager The resource manager to use - assets (client only), or data (server and client).
     * @param folder The folder to load the entries from.
     * @param extension The extension to look for.
     * @return A list of resource locations.
     */
    protected List<ResourceLocation> listResources(IResourceManager resourceManager, String folder, String extension) {
        List<ResourceLocation> result = Lists.newArrayList();
        int folderNameLength = folder.length() + 1;

        for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations(folder, (x) -> x.endsWith(extension))) {
            String path = resourceLocation.getPath();
            String namespace = resourceLocation.getNamespace();
            String resourceName = path.substring(folderNameLength, path.length() - extension.length());
            ResourceLocation registryName = new ResourceLocation(namespace, resourceName);
            result.add(registryName);
        }

        return  result;
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
                switch (type) {
                    case "spruce":
                        return FoliageColors.getSpruce();

                    case "birch":
                        return FoliageColors.getBirch();

                    case "oak":
                        return FoliageColors.getDefault();

                    case "foliage": {
                        float temperature = JSONUtils.getFloat(color, "temperature");
                        float humidity = JSONUtils.getFloat(color, "humidity");
                        return FoliageColors.get(temperature, humidity);
                    }

                    case "grass": {
                        float temperature = JSONUtils.getFloat(color, "temperature");
                        float humidity = JSONUtils.getFloat(color, "humidity");
                        return GrassColors.get(temperature, humidity);
                    }

                    default:
                        return -1;
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
        IForgeRegistry<Block> blockRegistry = RegistryManager.ACTIVE.getRegistry(GameData.BLOCKS);
        return blockRegistry.getValue(new ResourceLocation(blockName));
    }

    /**
     * Deserialize the JSON data to an object.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return A new object based on the JSON data.
     */
    protected abstract T deserialize(ResourceLocation location, JsonObject json);

    /**
     * Set an array of values.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setArray(U properties, JsonObject json, String key, BiConsumer<U, JsonObject> consumer) {
        if (JSONUtils.hasField(json, key)) {
            JsonArray mutations = JSONUtils.getJsonArray(json, key);
            for (JsonElement i : mutations) {
                JsonObject value = i.getAsJsonObject();
                consumer.accept(properties, value);
            }
        }
    }

    /**
     * Set a boolean value.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setBoolean(U properties, JsonObject json, String key, BiConsumer<U, Boolean> consumer) {
        if (JSONUtils.hasField(json, key)) {
            boolean value = JSONUtils.getBoolean(json, key);
            consumer.accept(properties, value);
        }
    }

    /**
     * Set a boolean value but only if it is false.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setIfFalse(U properties, JsonObject json, String key, Consumer<U> consumer) {
        if (JSONUtils.hasField(json, key)) {
            boolean value = JSONUtils.getBoolean(json, key);
            if (!value) {
                consumer.accept(properties);
            }
        }
    }

    /**
     * Set a boolean value but only if it is true.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setIfTrue(U properties, JsonObject json, String key, Consumer<U> consumer) {
        if (JSONUtils.hasField(json, key)) {
            boolean value = JSONUtils.getBoolean(json, key);
            if (value) {
                consumer.accept(properties);
            }
        }
    }

    /**
     * Set a float value.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setFloat(U properties, JsonObject json, String key, BiConsumer<U, Float> consumer) {
        if (JSONUtils.hasField(json, key)) {
            float value = JSONUtils.getFloat(json, key);
            consumer.accept(properties, value);
        }
    }

    /**
     * Set a two float values.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key1 The first key to look for.
     * @param key2 The second key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setTwoFloats(U properties, JsonObject json, String key1, String key2, TriConsumer<U, Float, Float> consumer) {
        if (JSONUtils.hasField(json, key1) &&
                JSONUtils.hasField(json, key2)) {
            float value1 = JSONUtils.getFloat(json, key1);
            float value2 = JSONUtils.getFloat(json, key2);
            consumer.accept(properties, value1, value2);
        }
    }

    /**
     * Set an integer value.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setInt(U properties, JsonObject json, String key, BiConsumer<U, Integer> consumer) {
        if (JSONUtils.hasField(json, key)) {
            int value = JSONUtils.getInt(json, key);
            consumer.accept(properties, value);
        }
    }

    /**
     * Set a two integer values.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key1 The first key to look for.
     * @param key2 The second key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setTwoInts(U properties, JsonObject json, String key1, String key2, TriConsumer<U, Integer, Integer> consumer) {
        if (JSONUtils.hasField(json, key1) &&
                JSONUtils.hasField(json, key2)) {
            int value1 = JSONUtils.getInt(json, key1);
            int value2 = JSONUtils.getInt(json, key2);
            consumer.accept(properties, value1, value2);
        }
    }

    /**
     * Set a string value.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setString(U properties, JsonObject json, String key, BiConsumer<U, String> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String value = JSONUtils.getString(json, key);
            consumer.accept(properties, value);
        }
    }

    /**
     * Set a resource location.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setResourceLocation(U properties, JsonObject json, String key, BiConsumer<U, ResourceLocation> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String value = JSONUtils.getString(json, key);
            consumer.accept(properties, new ResourceLocation(value));
        }
    }

    /**
     * Set a sound event.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setSoundEvent(U properties, JsonObject json, String key, BiConsumer<U, SoundEvent> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String name = JSONUtils.getString(json, key);

            IForgeRegistry<SoundEvent> soundEventRegistry = RegistryManager.ACTIVE.getRegistry(GameData.SOUNDEVENTS);
            SoundEvent soundEvent = soundEventRegistry.getValue(new ResourceLocation(name));
            consumer.accept(properties, soundEvent);
        }
    }

    /**
     * Set a sound type.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */

    protected <U> void setSoundType(U properties, JsonObject json, String key, BiConsumer<U, SoundType> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String name = JSONUtils.getString(json, key).toUpperCase();

            try {
                Field field = SoundType.class.getDeclaredField(name);
                consumer.accept(properties, (SoundType)field.get(null));
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                LOGGER.error("Sound Type {} not supported", name, exception);
            }
        }
    }

    /**
     * Set a tool type.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setToolType(U properties, JsonObject json, String key, BiConsumer<U, ToolType> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String toolType = JSONUtils.getString(json, key);
            ToolType tool = ToolType.get(toolType);
            consumer.accept(properties, tool);
        }
    }

    /**
     * Set a block.
     * @param properties The properties to set the value on.
     * @param json The JSON data.
     * @param key The key to look for.
     * @param consumer The method to call to set the value.
     * @param <U> The type of builder.
     */
    protected <U> void setBlock(U properties, JsonObject json, String key, BiConsumer<U, Block> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String blockName = JSONUtils.getString(json, key);
            Block block = getBlock(blockName);
            consumer.accept(properties, block);
        }
    }
}
