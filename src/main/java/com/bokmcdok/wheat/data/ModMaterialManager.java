package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.material.ModMaterialBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;

public class ModMaterialManager extends ModDataManager<Material> {
    private static final String MATERIALS_FOLDER = "materials";

    public void loadMaterials() {
        loadDataEntries(MATERIALS_FOLDER);
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

        setIfTrue(builder, json, "liquid", (x) -> x.liquid());
        setIfFalse(builder, json, "solid", (x) -> x.notSolid());
        setIfFalse(builder, json, "blocks_movement", (x) -> x.doesNotBlockMovement());
        setIfTrue(builder, json, "replaceable", (x) -> x.replaceable());

        //  Private in Material class...
        //setIfFalse(builder, json, "opaque", (x) -> x.notOpaque());

        setIfTrue(builder, json, "requires_tool", (x) -> x.requiresTool());
        setIfTrue(builder, json, "flammable", (x) -> x.flammable());
        setIfTrue(builder, json, "push_destroys", (x) -> x.pushDestroys());
        setIfTrue(builder, json, "push_blocks", (x) -> x.pushBlocks());

        return builder.build();
    }
}
