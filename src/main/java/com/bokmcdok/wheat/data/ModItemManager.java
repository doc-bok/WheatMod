package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.item.IModItem;
import com.bokmcdok.wheat.item.ModBlockNamedItem;
import com.bokmcdok.wheat.item.ModItem;
import com.bokmcdok.wheat.item.ModBlockItem;
import com.bokmcdok.wheat.item.ModItemImpl;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;

import java.util.Optional;

public class ModItemManager extends ModDataManager<IModItem> {
    private static final String ITEMS_FOLDER = "items";
    private static final String CONTAINERS_FOLDER = "containers";
    private ModEffectManager mEffectManager = new ModEffectManager();

    private enum ItemType {
        ITEM,
        BLOCK,
        BLOCK_NAMED
    }

    /**
     * Get the entries as a list of ModItems.
     * @return And array of items loaded by this data manager.
     */
    public IModItem[] getItems() {
        return getAllEntries().toArray(new IModItem[0]);
    }

    /**
     * Get an array
     * @return An array of items to register with Forge.
     */
    public Item[] getAsItems() {
        List<IModItem> values = new ArrayList(getAllEntries());
        List<Item> converted = Lists.transform(values, i -> i.asItem());
        return converted.toArray(new Item[0]);
    }

    /**
     * Load all the items from their data files.
     */
    public void loadItems() {
        //  Load effects
        mEffectManager.loadEffects();

        //  Load container items
        loadEntries(CONTAINERS_FOLDER);

        //  Load other items
        loadEntries(ITEMS_FOLDER);
    }

    /**
     * Convert a JSON file to a Minecraft Item.
     * @param json The JSON data from the file.
     * @return A new instance of the item.
     */
    protected IModItem deserialize(ResourceLocation location, JsonObject json) {
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

        if (JSONUtils.hasField(json, "compost_chance")) {
            float compostChance = JSONUtils.getFloat(json, "compost_chance");
            properties.compostChance(compostChance);
        }

        deserializeTools(json, properties);
        deserializeFood(json, properties);
        deserializeThrowing(json, properties);
        deserializeColor(json, properties);

        //  Create the right item type
        String typeValue = JSONUtils.getString(json, "type");
        ItemType type = ItemType.valueOf(typeValue.toUpperCase());

        IModItem result = null;
        switch (type) {
            case ITEM:
                result = new ModItem(properties);
                break;

            case BLOCK: {
                String blockName = JSONUtils.getString(json, "block");
                result = new ModBlockItem(getBlock(blockName), properties);
                break;
            }

            case BLOCK_NAMED: {
                String blockName = JSONUtils.getString(json, "block");
                result = new ModBlockNamedItem(getBlock(blockName), properties);
                break;
            }

            default:
                LOGGER.info("Item type {} not supported", typeValue);
                return null;
        }

        result.asItem().setRegistryName(location);
        return result;
    }

    /**
     * Deserialize tool properties on items.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeTools(JsonObject json, ModItemImpl.ModItemProperties properties) {
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
    }

    /**
     * Convert a JSON object to a Food instance.
     * @param json The JSON object from the item file.
     * @return A new instance of Food.
     */
    private void deserializeFood(JsonObject json, ModItemImpl.ModItemProperties properties) {
        if (JSONUtils.hasField(json, "food")) {
            JsonObject food = JSONUtils.getJsonObject(json, "food");
            Food.Builder builder = new Food.Builder();

            if (JSONUtils.hasField(food, "hunger")) {
                int hunger = JSONUtils.getInt(food, "hunger");
                builder.hunger(hunger);
            }

            if (JSONUtils.hasField(food, "saturation")) {
                float saturation = JSONUtils.getFloat(food, "saturation");
                builder.saturation(saturation);
            }

            if (JSONUtils.hasField(food, "meat")) {
                boolean meat = JSONUtils.getBoolean(food, "meat");
                if (meat) {
                    builder.meat();
                }
            }

            if (JSONUtils.hasField(food, "always_edible")) {
                boolean alwaysEdible = JSONUtils.getBoolean(food, "always_edible");
                if (alwaysEdible) {
                    builder.setAlwaysEdible();
                }
            }

            if (JSONUtils.hasField(food, "fast_to_eat")) {
                boolean fastToEat = JSONUtils.getBoolean(food, "fast_to_eat");
                if (fastToEat) {
                    builder.fastToEat();
                }
            }

            if (JSONUtils.hasField(food, "effects")) {
                JsonArray effects = JSONUtils.getJsonArray(food, "effects");
                for (JsonElement effect : effects) {
                    JsonObject effectAsJsonObject = effect.getAsJsonObject();

                    String effectName = JSONUtils.getString(effectAsJsonObject, "effect_type");
                    ResourceLocation location = new ResourceLocation(effectName);

                    float probability = 1.0f;
                    if (JSONUtils.hasField(effectAsJsonObject, "probability")) {
                        probability = JSONUtils.getFloat(effectAsJsonObject, "probability");
                    }

                    EffectInstance effectInstance = mEffectManager.getEntry(location);
                    if (effectInstance != null) {
                        builder.effect(effectInstance, probability);
                    }
                }
            }

            properties.food(builder.build());
        }
    }

    /**
     * Deserialize throwing properties on items.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeThrowing(JsonObject json, ModItemImpl.ModItemProperties properties) {
        if (JSONUtils.hasField(json, "throwing")) {
            JsonObject throwing = JSONUtils.getJsonObject(json, "throwing");
            float velocity = JSONUtils.getFloat(throwing, "velocity");
            float offset = JSONUtils.getFloat(throwing, "offset");
            float inaccuracy = JSONUtils.getFloat(throwing, "inaccuracy");

            properties.throwing(offset, velocity, inaccuracy);

            if (JSONUtils.hasField(throwing, "sound")) {
                String sound = JSONUtils.getString(throwing, "sound");
                float volume = 0.5f;
                float pitch = 0.4f;

                if (JSONUtils.hasField(throwing, "volume")) {
                    volume = JSONUtils.getFloat(throwing, "volume");
                }

                if (JSONUtils.hasField(throwing, "pitch")) {
                    pitch = JSONUtils.getFloat(throwing, "pitch");
                }

                Optional<SoundEvent> event = Registry.SOUND_EVENT.getValue(new ResourceLocation(sound));
                if (event.isPresent()) {
                    properties.throwingSound(event.get(), volume, pitch);
                }
            }
        }
    }

    /**
     * Deserialize the color of an item
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeColor(JsonObject json, ModItemImpl.ModItemProperties properties) {
        int color = deserializeColor(json);
        if (color != -1) {
            properties.color((item, state) -> deserializeColor(json));
        }
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

        return getEntry(location).asItem();
    }
}
