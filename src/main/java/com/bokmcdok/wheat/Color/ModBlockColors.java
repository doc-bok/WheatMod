package com.bokmcdok.wheat.Color;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;

public class ModBlockColors {

    public static final IBlockColor WILD_EINKORN = (state, reader, pos, tintIndex) -> {
        if (reader != null && pos != null) {
            return BiomeColors.getGrassColor(reader, pos);
        }

        return GrassColors.get(0.5D, 1.0D);
    };

    public static final IBlockColor COMMON_WHEAT = (state, reader, pos, tintIndex) -> {
        return COLOR(245, 222, 89);
    };

    public static final IBlockColor EINKORN = (state, reader, pos, tintIndex) -> {
        return COLOR(179, 222, 89);
    };

    public static final IBlockColor WILD_EMMER = (state, reader, pos, tintIndex) -> {
        int color = 0;
        if (reader != null && pos != null) {
            color = BiomeColors.getGrassColor(reader, pos);
        } else {
            color = GrassColors.get(0.5D, 1.0D);
        }

        return (245 << 16) | color;
    };

    public static final IBlockColor EMMER = (state, reader, pos, tintIndex) -> {
        return COLOR(222, 245, 89);
    };

    public static final IBlockColor DURUM = (state, reader, pos, tintIndex) -> {
        return COLOR(179, 245, 89);
    };

    public static final IBlockColor DISEASED_WHEAT = (state, reader, pos, tintIndex) -> {
        return COLOR(156, 66, 0);
    };

    public static final IBlockColor SPELT = (state, reader, pos, tintIndex) -> {
        return COLOR(230, 230, 44);
    };

    /**
     * Helper function to generate a colour.
     * @param r Red
     * @param g Green
     * @param b Blue
     * @return An integer representing the specified colour.
     */
    private static int COLOR(int r, int g, int b) {
        return (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }
}
