package com.bokmcdok.wheat.terraingen;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.entity.ModEntityRegistrar;
import com.bokmcdok.wheat.supplier.ModBlockSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.LazyValue;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ModTerrain {
    private static final LazyValue<Block> WILD_EINKORN = new LazyValue<>(new ModBlockSupplier("docwheat:wild_einkorn"));
    private static final LazyValue<Block> WILD_EMMER = new LazyValue<>(new ModBlockSupplier("docwheat:wild_emmer"));

    /**
     * Adds the new wild wheat blocks to the terrain generation.
     */
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        Feature.STRUCTURES.put("windmill", ModFeatureUtils.WINDMILL);

        for (Biome biome : ForgeRegistries.BIOMES) {
            List<Biome.SpawnListEntry> creatureSpawns = biome.getSpawns(EntityClassification.CREATURE);
            List<Biome.SpawnListEntry> ambientSpawns = biome.getSpawns(EntityClassification.AMBIENT);
            List<Biome.SpawnListEntry> monsterSpawns = biome.getSpawns(EntityClassification.MONSTER);

            switch (biome.getCategory()) {
                case PLAINS:
                    addWildWheatFeature(biome, WILD_EINKORN.getValue());
                    biome.func_226711_a_(ModFeatureUtils.WINDMILL.func_225566_b_(new ModWindmillConfig(1)));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModFeatureUtils.WINDMILL.func_225566_b_(new ModWindmillConfig(1)).func_227228_a_(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.cornsnake, 10, 1, 1));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 3));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.widowbird, 10, 1, 3));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 3));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.heukatze, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenvogel, 40, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case FOREST:
                    addWildWheatFeature(biome, WILD_EINKORN.getValue());
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.cornsnake, 10, 1, 1));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 3));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 3));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidewolf, 5, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.heukatze, 25, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenvogel, 40, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case RIVER:
                case SWAMP:
                    addWildWheatFeature(biome, WILD_EMMER.getValue());
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 3));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.widowbird, 10, 1, 3));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 3));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.heukatze, 25, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenvogel, 40, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case TAIGA:
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 1));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidewolf, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenbeller, 8, 1, 3));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.heukatze, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case SAVANNA:
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.heukatze, 8, 1, 1));
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 1));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case EXTREME_HILLS:
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 1));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    break;

                case JUNGLE:
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 1));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.haferbock, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.weizenmutter, 5, 1, 1));
                    break;

                case MUSHROOM:
                    creatureSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.field_mouse, 10, 1, 1));
                    ambientSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.butterfly, 10, 1, 1));
                    monsterSpawns.add(new Biome.SpawnListEntry(ModEntityRegistrar.getreidehahn, 8, 1, 1));
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Add a wild wheat feature to generate in a biome.
     */
    private static void addWildWheatFeature(Biome biome, Block block)
    {
        CropsBlock cropsBlock = (CropsBlock)block;

        //  Chance is equal to 1 / x. The calculation makes age 3 the most commonly generated age, decreasing as the age
        //  gets further away. Age 7 (the final age) is least common.
        for (int i = 0; i < 8; ++i) {
            BlockClusterFeatureConfig field_226728_P_ = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(cropsBlock.withAge(i)), new SimpleBlockPlacer())).func_227315_a_(32).func_227322_d_();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.func_225566_b_(field_226728_P_).func_227228_a_(Placement.COUNT_HEIGHTMAP_32.func_227446_a_(new FrequencyConfig(Math.abs(i - 3) + 4))));
        }
    }
}
