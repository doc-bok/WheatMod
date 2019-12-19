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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        if (JSONUtils.hasField(json, "blocks_movement")) {
            Boolean blocksMovement = JSONUtils.getBoolean(json, "blocks_movement");
            if (!blocksMovement) {
                properties.doesNotBlockMovement();
            }
        }

        if (JSONUtils.hasField(json, "slipperiness")) {
            float slipperiness = JSONUtils.getFloat(json, "slipperiness");
            properties.slipperiness(slipperiness);
        }

        SoundType sound = deserializeSound(json);
        if (sound != null) {
            properties.sound(sound);
        }

        if (JSONUtils.hasField(json, "light_value")) {
            int lightValue = JSONUtils.getInt(json, "light_value");
            properties.lightValue(lightValue);
        }

        if (JSONUtils.hasField(json, "hardness") &&
                JSONUtils.hasField(json, "resistance")) {
            float hardness = JSONUtils.getFloat(json, "hardness");
            int resistance = JSONUtils.getInt(json, "resistance");
            properties.hardnessAndResistance(hardness, resistance);
        }

        if (JSONUtils.hasField(json, "ticks_randomly")) {
            Boolean tickRandomly = JSONUtils.getBoolean(json, "ticks_randomly");
            if (tickRandomly) {
                properties.tickRandomly();
            }
        }

        if (JSONUtils.hasField(json, "variable_opacity")) {
            Boolean variableOpacity = JSONUtils.getBoolean(json, "variable_opacity");
            if (variableOpacity) {
                properties.variableOpacity();
            }
        }

        if (JSONUtils.hasField(json, "harvest_level")) {
            int harvestLevel = JSONUtils.getInt(json, "harvest_level");
            properties.harvestLevel(harvestLevel);
        }

        if (JSONUtils.hasField(json, "harvest_tool")) {
            String toolType = JSONUtils.getString(json, "harvest_tool");
            ToolType tool = ToolType.get(toolType);
            properties.harvestTool(tool);
        }

        if (JSONUtils.hasField(json, "no_drops")) {
            Boolean noDrops = JSONUtils.getBoolean(json, "no_drops");
            if (noDrops) {
                properties.noDrops();
            }
        }

        if (JSONUtils.hasField(json, "loot_from")) {
            String blockName = JSONUtils.getString(json, "loot_from");
            Block block = getBlock(blockName);
            properties.lootFrom(block);
        }

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
     * Helper to deserialize sound properties for an item.
     * @param json The JSON object from the file
     * @return The sound type, if any
     */
    private SoundType deserializeSound(JsonObject json) {
        if (JSONUtils.hasField(json, "sound")) {
            String name = JSONUtils.getString(json, "sound").toUpperCase();

            try {
                Field field = SoundType.class.getDeclaredField(name);
                return (SoundType) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                LOGGER.error("Sound Type {} not supported", name, exception);
            }
        }

        return null;
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
            int encouragement = JSONUtils.getInt(fire, "encouragement");
            int flammability = JSONUtils.getInt(fire, "flammability");
            properties.flammable(encouragement, flammability);
        }
    }

    private ModCropProperties deserializeCropProperties(JsonObject json) {
        if (JSONUtils.hasField(json, "crop")) {
            JsonObject crop = JSONUtils.getJsonObject(json, "crop");
            ModCropProperties properties = new ModCropProperties();

            if (JSONUtils.hasField(crop, "disease")) {
                String disease = JSONUtils.getString(crop, "disease");
                properties.disease(new ResourceLocation(disease));
            }

            if (JSONUtils.hasField(crop, "seed")) {
                String seed = JSONUtils.getString(crop, "seed");
                properties.seed(new ResourceLocation(seed));
            }

            if (JSONUtils.hasField(crop, "disease_resistance")) {
                int diseaseResistance = JSONUtils.getInt(crop, "disease_resistance");
                properties.diseaseResistance(diseaseResistance);
            }

            if (JSONUtils.hasField(crop, "wild")) {
                boolean wild = JSONUtils.getBoolean(crop, "wild");
                properties.wild(wild);
            }

            if (JSONUtils.hasField(crop, "mutations")) {
                JsonArray mutations = JSONUtils.getJsonArray(crop, "mutations");
                for (JsonElement i : mutations) {
                    JsonObject mutation = i.getAsJsonObject();
                    ResourceLocation mutationBlock = new ResourceLocation(JSONUtils.getString(mutation, "mutation"));

                    ResourceLocation required = null;
                    if (JSONUtils.hasField(mutation, "required")) {
                        required = new ResourceLocation(JSONUtils.getString(mutation, "required"));
                    }

                    int weight = 1;
                    if (JSONUtils.hasField(mutation, "weight")) {
                        weight = JSONUtils.getInt(mutation, "weight");
                    }

                    properties.addMutation(mutationBlock, required, weight);
                }
            }

            return properties;
        }

        return null;
    }
}
