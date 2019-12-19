package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.block.IModBlock;
import com.bokmcdok.wheat.block.ModBlockImpl;
import com.bokmcdok.wheat.block.ModCropProperties;
import com.bokmcdok.wheat.block.ModCropsBlock;
import com.bokmcdok.wheat.block.ModHayBlock;
import com.bokmcdok.wheat.block.ModMatBlock;
import com.bokmcdok.wheat.block.ModSmallStoneBlock;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModBlockManager extends ModDataManager<IModBlock> {
    private static final String BLOCKS_FOLDER = "blocks";
    private static final ModMaterialManager MATERIAL_MANAGER = new ModMaterialManager();

    private enum BlockType {
        CROP,
        HAY,
        MAT,
        SMALL_STONE
    }

    public IModBlock[] getBlocks() {
        return getAllEntries().toArray(new IModBlock[0]);
    }

    public Block[] getAsBlocks() {
        List<IModBlock> values = new ArrayList(getAllEntries());
        List<Block> converted = Lists.transform(values, i -> i.asBlock());
        return converted.toArray(new Block[0]);
    }

    public void loadBlocks() {
        MATERIAL_MANAGER.loadMaterials();
        loadEntries(BLOCKS_FOLDER);
    }

    protected IModBlock deserialize(ResourceLocation location, JsonObject json) {
        ModBlockImpl.ModBlockProperties properties = null;

        if (JSONUtils.hasField(json, "material")) {
            String materialName = JSONUtils.getString(json, "material");
            Material material = MATERIAL_MANAGER.getEntry(new ResourceLocation(materialName));
            MaterialColor mapColor = deserializedMapColor(json);

            if (mapColor == null) {
                properties = new ModBlockImpl.ModBlockProperties(material);
            } else {
                properties = new ModBlockImpl.ModBlockProperties(material, mapColor);
            }
        } else if (JSONUtils.hasField(json, "from")) {
            String blockName = JSONUtils.getString(json, "from");
            Block block = getBlock(blockName);
            properties = new ModBlockImpl.ModBlockProperties(block);
        } else {
            LOGGER.error("{} - Blocks require either 'material' or 'from' properties", location.toString());
            return null;
        }

        setIfFalse(properties, json, "block_movement", (x) -> x.doesNotBlockMovement());
        setFloat(properties, json, "slipperiness", (x, value) -> x.slipperiness(value));
        setSound(properties, json, "sound", (x, value) -> x.sound(value));
        setInt(properties, json, "light_value", (x, value) -> x.lightValue(value));
        setTwoFloats(properties, json, "hardness", "resistance", (x, v1, v2) -> x.hardnessAndResistance(v1, v2));
        setIfTrue(properties, json, "ticks_randomly", (x) -> x.tickRandomly());
        setIfTrue(properties, json, "variable_opacity", (x) -> x.variableOpacity());
        setInt(properties, json, "harvest_level", (x, value) -> x.harvestLevel(value));
        setToolType(properties, json, "harvest_tool", (x, value) -> x.harvestTool(value));
        setIfTrue(properties, json, "no_drops", (x) -> x.noDrops());
        setBlock(properties, json, "loot_from", (x, value) -> x.lootFrom(value));

        deserializeColor(json, properties);
        deserializeFire(json, properties);

        ModCropProperties crop = deserializeCropProperties(json);
        if (crop != null) {
            properties.crop(crop);
        }

        String typeValue = JSONUtils.getString(json, "type");
        BlockType type = BlockType.valueOf(typeValue.toUpperCase());

        IModBlock result = null;
        switch (type) {
            case CROP:
                result = new ModCropsBlock(properties);
                break;

            case HAY:
                result = new ModHayBlock(properties);
                break;

            case MAT:
                result = new ModMatBlock(properties);
                break;

            case SMALL_STONE:
                result = new ModSmallStoneBlock(properties);
                break;

            default:
                LOGGER.info("Block type {} not supported", typeValue);
                return null;
        }

        result.asBlock().setRegistryName(location);
        return result;
    }

    /**
     * Deserialize the color of an item
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeColor(JsonObject json, ModBlockImpl.ModBlockProperties properties) {
        int color = deserializeColor(json);
        if (color != -1) {
            properties.color((state, reader, pos, tintIndex) -> deserializeColor(json));
        }
    }

    private void deserializeFire(JsonObject json, ModBlockImpl.ModBlockProperties properties) {
        if (JSONUtils.hasField(json, "fire")) {
            JsonObject fire = JSONUtils.getJsonObject(json, "fire");
            setTwoInts(properties, json, "encouragement", "flammability", (x, v1, v2) -> x.flammable(v1, v2));
        }
    }

    private ModCropProperties deserializeCropProperties(JsonObject json) {
        if (JSONUtils.hasField(json, "crop")) {
            JsonObject crop = JSONUtils.getJsonObject(json, "crop");
            ModCropProperties properties = new ModCropProperties();

            setResourceLocation(properties, crop, "disease", (x, value) -> x.disease(value));
            setResourceLocation(properties, crop, "seed", (x, value) -> x.seed(value));
            setInt(properties, crop, "disease_resistance", (x, value) -> x.diseaseResistance(value));
            setBoolean(properties, crop, "wild", (x, value) -> x.wild(value));

            setArray(properties, crop, "mutations", (x, mutation) -> {
                ResourceLocation mutationBlock = new ResourceLocation(JSONUtils.getString(mutation, "mutation"));

                ResourceLocation required = null;
                if (JSONUtils.hasField(mutation, "required")) {
                    String value = JSONUtils.getString(mutation, "required");
                    required = new ResourceLocation(value);
                }

                Integer weight = 1;
                if (JSONUtils.hasField(mutation, "weight")) {
                    weight = JSONUtils.getInt(mutation, "weight");
                }

                x.addMutation(mutationBlock, required, weight);
            });

            return properties;
        }

        return null;
    }

    private <T> void setArray(T properties, JsonObject json, String key, BiConsumer<T, JsonObject> consumer) {
        if (JSONUtils.hasField(json, key)) {
            JsonArray mutations = JSONUtils.getJsonArray(json, key);
            for (JsonElement i : mutations) {
                JsonObject value = i.getAsJsonObject();
                consumer.accept(properties, value);
            }
        }
    }

    private <T> void setBoolean(T properties, JsonObject json, String key, BiConsumer<T, Boolean> consumer) {
        if (JSONUtils.hasField(json, key)) {
            boolean value = JSONUtils.getBoolean(json, key);
            consumer.accept(properties, value);
        }
    }

    private <T> void setIfFalse(T properties, JsonObject json, String key, Consumer<T> consumer) {
        if (JSONUtils.hasField(json, key)) {
            Boolean value = JSONUtils.getBoolean(json, key);
            if (!value) {
                consumer.accept(properties);
            }
        }
    }

    private <T> void setIfTrue(T properties, JsonObject json, String key, Consumer<T> consumer) {
        if (JSONUtils.hasField(json, key)) {
            Boolean value = JSONUtils.getBoolean(json, key);
            if (value) {
                consumer.accept(properties);
            }
        }
    }

    private <T> void setFloat(T properties, JsonObject json, String key, BiConsumer<T, Float> consumer) {
        if (JSONUtils.hasField(json, key)) {
            float value = JSONUtils.getFloat(json, key);
            consumer.accept(properties, value);
        }
    }

    private <T> void setTwoFloats(T properties, JsonObject json, String key1, String key2, TriConsumer<T, Float, Float> consumer) {
        if (JSONUtils.hasField(json, key1) &&
                JSONUtils.hasField(json, key2)) {
            float value1 = JSONUtils.getFloat(json, key1);
            float value2 = JSONUtils.getFloat(json, key2);
            consumer.accept(properties, value1, value2);
        }
    }

    private <T> void setInt(T properties, JsonObject json, String key, BiConsumer<T, Integer> consumer) {
        if (JSONUtils.hasField(json, key)) {
            int value = JSONUtils.getInt(json, key);
            consumer.accept(properties, value);
        }
    }

    private <T> void setTwoInts(T properties, JsonObject json, String key1, String key2, TriConsumer<T, Integer, Integer> consumer) {
        if (JSONUtils.hasField(json, key1) &&
                JSONUtils.hasField(json, key2)) {
            int value1 = JSONUtils.getInt(json, key1);
            int value2 = JSONUtils.getInt(json, key2);
            consumer.accept(properties, value1, value2);
        }
    }

    private <T> void setString(T properties, JsonObject json, String key, BiConsumer<T, String> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String value = JSONUtils.getString(json, key);
            consumer.accept(properties, value);
        }
    }

    private <T> void setResourceLocation(T properties, JsonObject json, String key, BiConsumer<T, ResourceLocation> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String value = JSONUtils.getString(json, key);
            consumer.accept(properties, new ResourceLocation(value));
        }
    }

    private <T> void setSound(T properties, JsonObject json, String key, BiConsumer<T, SoundType> consumer) {
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

    private <T> void setToolType(T properties, JsonObject json, String key, BiConsumer<T, ToolType> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String toolType = JSONUtils.getString(json, key);
            ToolType tool = ToolType.get(toolType);
            consumer.accept(properties, tool);
        }
    }

    private <T> void setBlock(T properties, JsonObject json, String key, BiConsumer<T, Block> consumer) {
        if (JSONUtils.hasField(json, key)) {
            String blockName = JSONUtils.getString(json, key);
            Block block = getBlock(blockName);
            consumer.accept(properties, block);
        }
    }
}
