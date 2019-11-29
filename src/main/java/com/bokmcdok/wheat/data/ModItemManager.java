package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.item.ModDurableItem;
import com.bokmcdok.wheat.item.ModItem;
import com.bokmcdok.wheat.item.ModThrowableItem;
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
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ModItemManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ITEMS_FOLDER = "items";
    private static final String CONTAINERS_FOLDER = "containers";
    private ModEffectManager mEffectManager = new ModEffectManager();
    private Map<String, Item> mContainerItems = ImmutableMap.of();
    private ModJsonLoader mJsonLoader = new ModJsonLoader();

    private enum ItemType {
        ITEM,
        BLOCK,
        BLOCK_NAMED,
        DURABLE,
        THROWABLE
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

                mContainerItems.put(item.getRegistryName().getPath(), item);

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
        Item.Properties properties = new Item.Properties();

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

        String registryName = JSONUtils.getString(json, "registry_name");
        String typeValue = JSONUtils.getString(json, "type");
        ItemType type = ItemType.valueOf(typeValue.toUpperCase());

        switch (type) {
            case ITEM:
                return new ModItem(properties).setRegistryName(registryName);

            case BLOCK: {
                String blockName = JSONUtils.getString(json, "block");
                return new BlockItem(getModBlock(blockName), properties).setRegistryName(registryName);
            }

            case BLOCK_NAMED: {
                String blockName = JSONUtils.getString(json, "block");
                return new BlockNamedItem(getModBlock(blockName), properties).setRegistryName(registryName);
            }

            case THROWABLE: {
                String blockName = JSONUtils.getString(json, "block");
                return new ModThrowableItem(getModBlock(blockName), properties).setRegistryName(registryName);
            }

            case DURABLE: {
                return new ModDurableItem(properties).setRegistryName(registryName);
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

                float probability = 1.0f;
                if (JSONUtils.hasField(effectAsJsonObject, "probability")) {
                    probability = JSONUtils.getFloat(effectAsJsonObject, "probability");
                }

                EffectInstance effectInstance = mEffectManager.getEffect(effectName);
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
        if ("bucket".equals(containerItemName)) {
            return Items.BUCKET;
        }

        if ("bowl".equals(containerItemName)) {
            return Items.BOWL;
        }

        if ("bottle".equals(containerItemName)) {
            return Items.GLASS_BOTTLE;
        }

        return mContainerItems.get(containerItemName);
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
            return null;
        }
    }
}
