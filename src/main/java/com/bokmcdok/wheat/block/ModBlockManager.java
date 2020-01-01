package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.data.ModDataManager;
import com.bokmcdok.wheat.data.ModMaterialManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModBlockManager extends ModDataManager<IModBlock> {
    private static final String BLOCKS_FOLDER = "blocks";
    private static final String TRAPS_FOLDER = "traps";
    private static final ModMaterialManager MATERIAL_MANAGER = new ModMaterialManager();

    private enum BlockType {
        BLOCK,
        CROP,
        HAY,
        MAT,
        SMALL_STONE,
        TRAP
    }

    /**
     * Get the blocks loaded by this data manager.
     * @return An array of blocks.
     */
    public IModBlock[] getBlocks() {
        return getAllEntries().toArray(new IModBlock[0]);
    }

    /**
     * Get the blocks in vanilla format.
     * @return An array of vanilla blocks.
     */
    public Block[] getAsBlocks() {
        List<IModBlock> values = new ArrayList(getAllEntries());
        List<Block> converted = Lists.transform(values, i -> i.asBlock());
        return converted.toArray(new Block[0]);
    }

    /**
     * Load the blocks from the blocks folder.
     */
    public void loadBlocks() {
        MATERIAL_MANAGER.loadMaterials();
        loadDataEntries(BLOCKS_FOLDER);
    }
    /**
     * Load traps from the traps folder.
     */
    public void loadTraps() {
        loadDataEntries(TRAPS_FOLDER);
    }

    /**
     * Deserialize a JSON file into a block.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return
     */
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

        setIfFalse(properties, json, "block_movement", ModBlockImpl.ModBlockProperties::doesNotBlockMovement);
        setFloat(properties, json, "slipperiness", ModBlockImpl.ModBlockProperties::slipperiness);
        setSound(properties, json, "sound", ModBlockImpl.ModBlockProperties::sound);
        setInt(properties, json, "light_value", ModBlockImpl.ModBlockProperties::lightValue);
        setTwoFloats(properties, json, "hardness", "resistance", ModBlockImpl.ModBlockProperties::hardnessAndResistance);
        setIfTrue(properties, json, "ticks_randomly", ModBlockImpl.ModBlockProperties::tickRandomly);
        setIfTrue(properties, json, "variable_opacity", ModBlockImpl.ModBlockProperties::variableOpacity);
        setInt(properties, json, "harvest_level", ModBlockImpl.ModBlockProperties::harvestLevel);
        setToolType(properties, json, "harvest_tool", ModBlockImpl.ModBlockProperties::harvestTool);
        setIfTrue(properties, json, "no_drops", ModBlockImpl.ModBlockProperties::noDrops);
        setBlock(properties, json, "loot_from", ModBlockImpl.ModBlockProperties::lootFrom);
        setInt(properties, json, "inventory_size", ModBlockImpl.ModBlockProperties::setInventory);

        deserializeColor(json, properties);
        deserializeFire(json, properties);
        properties.setShape(deserializeShape(json, "shape"));
        properties.setCollisionShape(deserializeShape(json, "collision_shape"));
        properties.setTargets(deserializeTargets(json, "targets"));

        ModCropProperties crop = deserializeCropProperties(json);
        if (crop != null) {
            properties.crop(crop);
        }

        String typeValue = JSONUtils.getString(json, "type");
        BlockType type = BlockType.valueOf(typeValue.toUpperCase());

        IModBlock result = null;
        switch (type) {
            case BLOCK:
                result = new ModBlock(properties);
                break;

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

            case TRAP:
                result = new ModTrapBlock(properties);
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

    /**
     * Deserialize a block's flammable properties.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeFire(JsonObject json, ModBlockImpl.ModBlockProperties properties) {
        if (JSONUtils.hasField(json, "fire")) {
            JsonObject fire = JSONUtils.getJsonObject(json, "fire");
            setTwoInts(properties, fire, "encouragement", "flammability", (x, v1, v2) -> x.flammable(v1, v2));
        }
    }

    /**
     * Deserializes properties for crops.
     * @param json The json data to deserialize.
     * @return The crop's properties.
     */
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

    /**
     * Deserialize a shape for culling or collisions
     * @param json The json object.
     * @param key The key of the shape to deserialize.
     * @return A new voxel shape for the block.
     */
    private VoxelShape deserializeShape(JsonObject json, String key) {
        VoxelShape result = null;
        if (JSONUtils.hasField(json, key)) {
            JsonArray shapes = JSONUtils.getJsonArray(json, key);
            for(JsonElement i : shapes) {
                JsonObject shape = i.getAsJsonObject();
                JsonArray from = JSONUtils.getJsonArray(shape, "from");
                JsonArray to = JSONUtils.getJsonArray(shape, "to");
                double fromX = from.get(0).getAsDouble();
                double fromY = from.get(1).getAsDouble();
                double fromZ = from.get(2).getAsDouble();
                double toX = to.get(0).getAsDouble();
                double toY = to.get(1).getAsDouble();
                double toZ = to.get(2).getAsDouble();
                if (result == null) {
                    result = Block.makeCuboidShape(fromX, fromY, fromZ, toX, toY, toZ);
                } else {
                    result = VoxelShapes.or(result, Block.makeCuboidShape(fromX, fromY, fromZ, toX, toY, toZ));
                }
            }
        }

        return result;
    }

    /**
     * Deserialize a list of targets.
     * @param json The JSON data from the file.
     * @param key The key to use.
     * @return A list of targets for the block.
     */
    private Set<ResourceLocation> deserializeTargets(JsonObject json, String key) {
        if (JSONUtils.hasField(json, key)) {
            JsonArray entities = JSONUtils.getJsonArray(json, key);
            if (entities.size() > 0) {
                Set<ResourceLocation> result = Sets.newHashSet();
                for (JsonElement i : entities) {
                    result.add(new ResourceLocation(i.getAsString()));
                }

                return result;
            }
        }

        return null;
    }
}