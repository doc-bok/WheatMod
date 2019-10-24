package com.bokmcdok.wheat.Color;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;

public class ModItemColors {
    private static int COLOR(int r, int g, int b) {
        return (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    public static final IItemColor WILD_EINKORN = (item, state) ->
    {
        return GrassColors.get(0.5D, 1.0D);
    };

    public static final IItemColor COMMON_WHEAT = (item, state) -> {
        return COLOR(245, 222, 89);
    };

    public static final IItemColor EINKORN = (item, state) -> {
        return COLOR(179, 222, 89);
    };

    public static final IItemColor COMMON_FLOUR = (item, state) -> {
        return COLOR(255, 232, 178);
    };

    public static final IItemColor WILD_EMMER = (item, state) -> {
        return (245 << 16) | GrassColors.get(0.5D, 1.0D);
    };

    public static final IItemColor EMMER = (item, state) -> {
        return COLOR(222, 245, 89);
    };

    public static final IItemColor DURUM = (item, state) -> {
        return COLOR(179, 245, 89);
    };

    public static final IItemColor DURUM_FLOUR = (item, state) -> {
        return COLOR(222, 245, 178);
    };

    public static final IItemColor SPELT = (item, state) -> {
        return COLOR(230, 230, 44);
    };

    public static final IItemColor SPELT_FLOUR = (item, state) -> {
        return COLOR(245, 244, 178);
    };

    public static final IItemColor TOMATO_SEEDS = (item, state) -> {
        return COLOR(255, 99, 71);
    };
}
