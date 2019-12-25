package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.entity.ModEntityUtils;
import com.bokmcdok.wheat.item.IModItem;
import com.bokmcdok.wheat.item.ModBlockNamedItem;
import com.bokmcdok.wheat.item.ModItem;
import com.bokmcdok.wheat.item.ModBlockItem;
import com.bokmcdok.wheat.item.ModItemImpl;
import com.bokmcdok.wheat.item.ModSpawnEggItem;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityType;
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
        BLOCK_NAMED,
        SPAWN_EGG
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

        setInt(properties, json, "max_stack_size", (x, value) -> x.maxStackSize(value));
        setInt(properties, json, "default_max_damage", (x, value) -> x.defaultMaxDamage(value));
        setInt(properties, json, "max_damage", (x, value) -> x.maxDamage(value));
        setString(properties, json, "group", (x, value) -> x.group(getItemGroup(value)));
        setString(properties, json, "rarity", (x, value) -> x.rarity(Rarity.valueOf(value)));
        setIfFalse(properties, json, "repairable", (x) -> x.setNoRepair());
        setString(properties, json, "container", (x, value) -> x.containerItem(getContainerItem(value)));
        setFloat(properties, json, "compost_chance", (x, value) -> x.compostChance(value));

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

            case SPAWN_EGG: {
                String entityName = JSONUtils.getString(json, "entity");
                JsonObject primaryColorJson = JSONUtils.getJsonObject(json, "primary_color");
                JsonObject secondaryColorJson = JSONUtils.getJsonObject(json, "secondary_color");
                int primaryColor = deserializeColor(primaryColorJson);
                int secondaryColor = deserializeColor(secondaryColorJson);
                result = new ModSpawnEggItem(new ResourceLocation(entityName), primaryColor, secondaryColor, properties);
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

            setInt(builder, food, "hunger", (x, value) -> x.hunger(value));
            setFloat(builder, food, "saturation", (x, value) -> x.saturation(value));
            setIfTrue(builder, food, "meat", (x) -> x.meat());
            setIfTrue(builder, food, "always_edible", (x) -> x.setAlwaysEdible());
            setIfTrue(builder, food, "fast_to_eat", (x) -> x.fastToEat());
            setArray(builder, food, "effects", (x, effect) -> {
                JsonObject effectAsJsonObject = effect.getAsJsonObject();

                String effectName = JSONUtils.getString(effectAsJsonObject, "effect_type");
                ResourceLocation location = new ResourceLocation(effectName);

                float probability = 1.0f;
                if (JSONUtils.hasField(effectAsJsonObject, "probability")) {
                    probability = JSONUtils.getFloat(effectAsJsonObject, "probability");
                }

                EffectInstance effectInstance = mEffectManager.getEntry(location);
                if (effectInstance != null) {
                    x.effect(effectInstance, probability);
                }
            });

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

    private EntityType<?> getEntity(ResourceLocation entityName) {
        try {
            Field field = ModEntityUtils.class.getDeclaredField(entityName.getPath());
            return (EntityType<?>)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            LOGGER.error("Entity type {} not supported", entityName, exception);
            return null;
        }
    }
}
