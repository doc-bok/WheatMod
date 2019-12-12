package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.item.ModItem;
import com.bokmcdok.wheat.item.ModBlockItem;
import com.bokmcdok.wheat.item.ModItemImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.Map.Entry;

import com.google.gson.JsonParseException;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class ModItemManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ITEMS_FOLDER = "items";
    private static final String CONTAINERS_FOLDER = "containers";
    private ModEffectManager mEffectManager = new ModEffectManager();
    private Map<ResourceLocation, Item> mContainerItems = ImmutableMap.of();
    private ModJsonLoader mJsonLoader = new ModJsonLoader();

    private enum ItemType {
        ITEM,
        BLOCK,
        BLOCK_NAMED
    }

    /**
     * Get the list of items to register.
     * @return An array of items to register with Forge.
     */
    public Item[] getItems() {
        return mContainerItems.values().toArray(new Item[0]);
    }

    /**
     * Load all the items from their data files.
     * @param resourceManager The resource manager to use to get the JSON files.
     */
    public void loadItems(IResourceManager resourceManager) {
        //  Load effects
        mEffectManager.loadEffects(resourceManager);

        mContainerItems = Maps.newHashMap();

        //  Load container items
        loadItems(resourceManager, CONTAINERS_FOLDER);

        //  Load other items
        loadItems(resourceManager, ITEMS_FOLDER);

        LOGGER.info("Loaded {} items", mContainerItems.values().size());
    }

    /**
     * Load items from a specified folder.
     * @param resourceManager The resource manager to use to get the JSON files.
     * @param folder The folder to load from.
     */
    private void loadItems(IResourceManager resourceManager, String folder) {
        Map<ResourceLocation, JsonObject> containerResources = mJsonLoader.loadJsonResources(resourceManager, folder);
        for(Entry<ResourceLocation, JsonObject> entry : containerResources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) { continue; }

            try {
                Item item = deserializeItem(entry.getValue());
                if (item == null) {
                    LOGGER.info("Skipping loading item {} as it's serializer returned null", resourceLocation);
                    continue;
                }

                item.setRegistryName(resourceLocation);
                mContainerItems.put(resourceLocation, item);

            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading item {}", resourceLocation, exception);
            }
        }
    }

    /**
     * Convert a JSON file to a Minecraft Item.
     * @param json The JSON data from the file.
     * @return A new instance of the item.
     */
    private Item deserializeItem(JsonObject json) {
        ModItemImpl.ModItemProperties properties = new ModItemImpl.ModItemProperties();

        if (JSONUtils.hasField(json, "max_stack_size")) {
            int maxStackSize = JSONUtils.getInt(json, "max_stack_size");
            properties.maxStackSize(maxStackSize);
        }

        if (JSONUtils.hasField(json,"default_max_damage")) {
            int defaultMaxDamage = JSONUtils.getInt(json, "default_max_damage");
            properties.defaultMaxDamage(defaultMaxDamage);
        }

        if (JSONUtils.hasField(json,"max_damage")) {
            int defaultMaxDamage = JSONUtils.getInt(json, "max_damage");
            properties.maxDamage(defaultMaxDamage);
        }

        if (JSONUtils.hasField(json, "group")) {
            String group = JSONUtils.getString(json, "group");
            properties.group(getItemGroup(group));
        }

        if (JSONUtils.hasField(json, "rarity")) {
            String rarity = JSONUtils.getString(json, "rarity");
            properties.rarity(Rarity.valueOf(rarity.toUpperCase()));
        }

        if (JSONUtils.hasField(json, "repairable")) {
            Boolean repairable = JSONUtils.getBoolean(json, "repairable");
            if (!repairable) { properties.setNoRepair(); }
        }

        if (JSONUtils.hasField(json, "container")) {
            String containerName = JSONUtils.getString(json, "container");
            Item container = getContainerItem(containerName);
            properties.containerItem(container);
        }

        if (JSONUtils.hasField(json, "tool_types")) {
            JsonArray toolTypes = JSONUtils.getJsonArray(json, "tool_types");
            for (JsonElement tool : toolTypes) {
                JsonObject toolAsJsonObject = tool.getAsJsonObject();
                String type = JSONUtils.getString(toolAsJsonObject, "tool_type");
                int level = JSONUtils.getInt(toolAsJsonObject, "level");

                ToolType toolType = ToolType.get(type);
                properties.addToolType(toolType, level);
            }
        }

        if (JSONUtils.hasField(json, "food")) {
            JsonObject food = JSONUtils.getJsonObject(json, "food");
            properties.food(deserializeFood(food));
        }

        if (JSONUtils.hasField(json, "throwing")) {
            JsonObject throwing = JSONUtils.getJsonObject(json, "throwing");
            float velocity = JSONUtils.getFloat(throwing, "velocity");
            float offset = JSONUtils.getFloat(throwing, "offset");
            float inaccuracy = JSONUtils.getFloat(throwing, "inaccuracy");

            properties.setThrowing(offset, velocity, inaccuracy);

            if (JSONUtils.hasField(throwing, "sound")) {
                String sound = JSONUtils.getString(throwing, "sound");
                float volume = 0.5f;
                float pitch = 0.4f;

                if (JSONUtils.hasField(throwing, "volume")) {
                    volume = JSONUtils.getFloat(throwing, "volume");
                }

                if (JSONUtils.hasField(throwing, "volume")) {
                    pitch = JSONUtils.getFloat(throwing, "volume");
                }

                Optional<SoundEvent> event = Registry.SOUND_EVENT.getValue(new ResourceLocation(sound));
                if (event.isPresent()) {
                    properties.setThrowingSound(event.get(), volume, pitch);
                }
            }
        }

        String typeValue = JSONUtils.getString(json, "type");
        ItemType type = ItemType.valueOf(typeValue.toUpperCase());

        switch (type) {
            case ITEM:
                return new ModItem(properties);

            case BLOCK: {
                String blockName = JSONUtils.getString(json, "block");
                return new ModBlockItem(getModBlock(blockName), properties);
            }

            case BLOCK_NAMED: {
                String blockName = JSONUtils.getString(json, "block");
                return new BlockNamedItem(getModBlock(blockName), properties);
            }

            default:
                LOGGER.info("Item type {} not supported", typeValue);
                return null;
        }
    }

    /**
     * Convert a JSON object to a Food instance.
     * @param json The JSON object from the item file.
     * @return A new instance of Food.
     */
    private Food deserializeFood(JsonObject json) {
        Food.Builder builder = new Food.Builder();

        if (JSONUtils.hasField(json, "hunger")) {
            int hunger = JSONUtils.getInt(json, "hunger");
            builder.hunger(hunger);
        }

        if (JSONUtils.hasField(json, "saturation")) {
            float saturation = JSONUtils.getFloat(json, "saturation");
            builder.saturation(saturation);
        }

        if (JSONUtils.hasField(json, "meat")) {
            boolean meat = JSONUtils.getBoolean(json, "meat");
            if (meat) { builder.meat(); }
        }

        if (JSONUtils.hasField(json, "always_edible")) {
            boolean alwaysEdible = JSONUtils.getBoolean(json, "always_edible");
            if (alwaysEdible) { builder.setAlwaysEdible(); }
        }

        if (JSONUtils.hasField(json, "fast_to_eat")) {
            boolean fastToEat = JSONUtils.getBoolean(json, "fast_to_eat");
            if (fastToEat) { builder.fastToEat(); }
        }

        if (JSONUtils.hasField(json, "effects")) {
            JsonArray effects = JSONUtils.getJsonArray(json, "effects");
            for (JsonElement effect : effects) {
                JsonObject effectAsJsonObject = effect.getAsJsonObject();

                String effectName = JSONUtils.getString(effectAsJsonObject, "effect_type");
                ResourceLocation location = new ResourceLocation(effectName);

                float probability = 1.0f;
                if (JSONUtils.hasField(effectAsJsonObject, "probability")) {
                    probability = JSONUtils.getFloat(effectAsJsonObject, "probability");
                }

                EffectInstance effectInstance = mEffectManager.getEffect(location);
                if (effectInstance != null) {
                    builder.effect(effectInstance, probability);
                }
            }
        }

        return builder.build();
    }

    /**
     * Get the Item Group for an item.
     * @param name The string name of an ItemGroup.
     * @return The instance of an ItemGroup.
     */
    private ItemGroup getItemGroup(String name) {
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.getTabLabel().equals(name)) {
                return group;
            }
        }

        throw new IllegalArgumentException("Invalid Item Group:" + name);
    }

    /**
     * Get the container for an item.
     * @param containerItemName The name of the container.
     * @return The instance of the container item.
     */
    private Item getContainerItem(String containerItemName) {
        ResourceLocation location = new ResourceLocation(containerItemName);
        if ("minecraft".equals(location.getNamespace())) {
            return Registry.ITEM.getOrDefault(location);
        }

        return mContainerItems.get(location);
    }

    /**
     * Get the mod block for a BlockItem or BlockNamedItem.
     * @param blockName The registry name of a block.
     * @return The instance of the block.
     */
    private Block getModBlock(String blockName) {
        try {
            Field field = ModBlockUtils.class.getDeclaredField(blockName);
            return (Block)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            LOGGER.error("Block type {} not supported", blockName, exception);
            return Registry.BLOCK.getOrDefault(new ResourceLocation(blockName));
        }
    }
}
