package com.bokmcdok.wheat.color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.world.GrassColors;

public class ModItemColors {
    public static final IItemColor WILD_EINKORN = (item, state) ->
    {
        return GrassColors.get(0.5D, 1.0D);
    };

    public static final IItemColor COMMON_WHEAT = (item, state) -> {
        return color(245, 222, 89);
    };

    public static final IItemColor EINKORN = (item, state) -> {
        return color(179, 222, 89);
    };

    public static final IItemColor COMMON_FLOUR = (item, state) -> {
        return color(255, 232, 178);
    };

    public static final IItemColor WILD_EMMER = (item, state) -> {
        return (245 << 16) | GrassColors.get(0.5D, 1.0D);
    };

    public static final IItemColor EMMER = (item, state) -> {
        return color(222, 245, 89);
    };

    public static final IItemColor DURUM = (item, state) -> {
        return color(179, 245, 89);
    };

    public static final IItemColor DURUM_FLOUR = (item, state) -> {
        return color(222, 245, 178);
    };

    public static final IItemColor SPELT = (item, state) -> {
        return color(230, 230, 44);
    };

    public static final IItemColor SPELT_FLOUR = (item, state) -> {
        return color(245, 244, 178);
    };

    public static final IItemColor TOMATO_SEEDS = (item, state) -> {
        return color(255, 99, 71);
    };

    /**
     * Helper function to generate a colour.
     * @param r Red
     * @param g Green
     * @param b Blue
     * @return An integer representing the specified colour.
     */
    private static int color(int r, int g, int b) {
        return (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }
}
