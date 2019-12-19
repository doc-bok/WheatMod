package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.material.ModMaterialBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModMaterialManager extends ModDataManager<Material> {
    private static final String MATERIALS_FOLDER = "materials";

    public void loadMaterials() {
        loadEntries(MATERIALS_FOLDER);
        addCustomEntry("minecraft:air", Material.AIR);
        addCustomEntry("minecraft:structure_void", Material.STRUCTURE_VOID);
        addCustomEntry("minecraft:portal", Material.PORTAL);
        addCustomEntry("minecraft:carpet", Material.CARPET);
        addCustomEntry("minecraft:plants", Material.PLANTS);
        addCustomEntry("minecraft:ocean_plant", Material.OCEAN_PLANT);
        addCustomEntry("minecraft:tall_plants", Material.TALL_PLANTS);
        addCustomEntry("minecraft:sea_grass", Material.SEA_GRASS);
        addCustomEntry("minecraft:water", Material.WATER);
        addCustomEntry("minecraft:bubble_column", Material.BUBBLE_COLUMN);
        addCustomEntry("minecraft:lava", Material.LAVA);
        addCustomEntry("minecraft:snow", Material.SNOW);
        addCustomEntry("minecraft:fire", Material.FIRE);
        addCustomEntry("minecraft:miscellaneous", Material.MISCELLANEOUS);
        addCustomEntry("minecraft:web", Material.WEB);
        addCustomEntry("minecraft:redstone_light", Material.REDSTONE_LIGHT);
        addCustomEntry("minecraft:clay", Material.CLAY);
        addCustomEntry("minecraft:earth", Material.EARTH);
        addCustomEntry("minecraft:organic", Material.ORGANIC);
        addCustomEntry("minecraft:packed_ice", Material.PACKED_ICE);
        addCustomEntry("minecraft:sand", Material.SAND);
        addCustomEntry("minecraft:sponge", Material.SPONGE);
        addCustomEntry("minecraft:shulker", Material.SHULKER);
        addCustomEntry("minecraft:wood", Material.WOOD);
        addCustomEntry("minecraft:bamboo_sapling", Material.BAMBOO_SAPLING);
        addCustomEntry("minecraft:bamboo", Material.BAMBOO);
        addCustomEntry("minecraft:wool", Material.WOOL);
        addCustomEntry("minecraft:tnt", Material.TNT);
        addCustomEntry("minecraft:leaves", Material.LEAVES);
        addCustomEntry("minecraft:glass", Material.GLASS);
        addCustomEntry("minecraft:ice", Material.ICE);
        addCustomEntry("minecraft:cactus", Material.CACTUS);
        addCustomEntry("minecraft:rock", Material.ROCK);
        addCustomEntry("minecraft:iron", Material.IRON);
        addCustomEntry("minecraft:snow_block", Material.SNOW_BLOCK);
        addCustomEntry("minecraft:anvil", Material.ANVIL);
        addCustomEntry("minecraft:barrier", Material.BARRIER);
        addCustomEntry("minecraft:piston", Material.PISTON);
        addCustomEntry("minecraft:coral", Material.CORAL);
        addCustomEntry("minecraft:gourd", Material.GOURD);
        addCustomEntry("minecraft:dragon_egg", Material.DRAGON_EGG);
        addCustomEntry("minecraft:cake", Material.CAKE);
    }

    protected Material deserialize(ResourceLocation location, JsonObject json) {
        MaterialColor mapColor = deserializedMapColor(json);
        if (mapColor == null) {
            return null;
        }

        ModMaterialBuilder builder = new ModMaterialBuilder(mapColor);

        if (JSONUtils.hasField(json, "liquid")) {
            Boolean liquid = JSONUtils.getBoolean(json, "liquid");
            if (liquid) {
                builder.liquid();
            }
        }

        if (JSONUtils.hasField(json, "solid")) {
            Boolean solid = JSONUtils.getBoolean(json, "solid");
            if (!solid) {
                builder.notSolid();
            }
        }

        if (JSONUtils.hasField(json, "blocks_movement")) {
            Boolean blocks_movement = JSONUtils.getBoolean(json, "blocks_movement");
            if (!blocks_movement) {
                builder.doesNotBlockMovement();
            }
        }

        if (JSONUtils.hasField(json, "replaceable")) {
            Boolean replaceable = JSONUtils.getBoolean(json, "replaceable");
            if (replaceable) {
                builder.replaceable();
            }
        }

        //  Private in Material class...
        /*if (JSONUtils.hasField(json, "opaque")) {
            Boolean opaque = JSONUtils.getBoolean(json, "opaque");
            if (!opaque) {
                builder.notOpaque();
            }
        }*/

        if (JSONUtils.hasField(json, "requires_tool")) {
            Boolean requires_tool = JSONUtils.getBoolean(json, "requires_tool");
            if (requires_tool) {
                builder.requiresTool();
            }
        }

        if (JSONUtils.hasField(json, "flammable")) {
            Boolean flammable = JSONUtils.getBoolean(json, "flammable");
            if (flammable) {
                builder.flammable();
            }
        }

        if (JSONUtils.hasField(json, "push_destroys")) {
            Boolean push_destroys = JSONUtils.getBoolean(json, "push_destroys");
            if (push_destroys) {
                builder.pushDestroys();
            }
        }

        if (JSONUtils.hasField(json, "push_blocks")) {
            Boolean push_blocks = JSONUtils.getBoolean(json, "push_blocks");
            if (push_blocks) {
                builder.pushBlocks();
            }
        }

        return builder.build();
    }
}
