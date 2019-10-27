package com.bokmcdok.wheat.terraingen;

import com.bokmcdok.wheat.block.ModBlocks;
import com.bokmcdok.wheat.block.WildWheatBlock;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ModTerrain {

    /**
     * Adds the new wild wheat blocks to the terrain generation.
     */
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        for (Biome biome : ForgeRegistries.BIOMES) {

            //  We have Einkorn wheat generated in Forest and Plains biomes.
            if (biome.getCategory() == Biome.Category.FOREST ||
                    biome.getCategory() == Biome.Category.PLAINS) {

                addWildWheatFeature(biome, ModBlocks.wild_einkorn);

            } else if (biome.getCategory() == Biome.Category.RIVER ||
                    biome.getCategory() == Biome.Category.SWAMP) {

                addWildWheatFeature(biome, ModBlocks.wild_emmer);
            }
        }
    }

    /**
     * Add a wild wheat feature to generate in a biome.
     */
    private static void addWildWheatFeature(Biome biome, WildWheatBlock block)
    {
        //  Chance is equal to 1 / x. The calculation makes age 3 the most commonly generated age, decreasing as the age
        //  gets further away. Age 7 (the final age) is least common.
        for (int i = 0; i < 8; ++i) {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(block.withAge(i)), Placement.CHANCE_HEIGHTMAP, new ChanceConfig(Math.abs(i - 3) + 2)));
        }
    }
}