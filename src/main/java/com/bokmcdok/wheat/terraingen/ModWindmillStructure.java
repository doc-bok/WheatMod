package com.bokmcdok.wheat.terraingen;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ModWindmillStructure extends ScatteredStructure<ModWindmillConfig> {

    private static final List<Biome.SpawnListEntry> SPAWN_LIST =
            Lists.newArrayList(new Biome.SpawnListEntry(EntityType.VILLAGER, 1, 1, 1));

    public ModWindmillStructure(Function<Dynamic<?>, ? extends  ModWindmillConfig> config) {
        super(config);
    }

    @Override
    public String getStructureName() {
        return "windmill";
    }

    @Override
    public int getSize() { return 3; }

    @Override
    public List<Biome.SpawnListEntry> getSpawnList() {
        return SPAWN_LIST;
    }

    @Override
    public boolean func_225558_a_(BiomeManager biomeManager, ChunkGenerator<?> chunkGenerator, Random random, int chunkPosX, int chunkPosZ, Biome biome) {
        ChunkPos position = getStartPositionForPosition(chunkGenerator, random, chunkPosX, chunkPosZ, 0, 0);

        int newX = random.nextInt(2) < 1 ? position.x - 3 : position.x + 3;
        int newZ = random.nextInt(2) < 1 ? position.z - 3 : position.z + 3;

        position = new ChunkPos(newX, newZ);
        if (chunkPosX == position.x && chunkPosZ == position.z) {
            int i = chunkPosX >> 4;
            int j = chunkPosZ >> 4;

            random.setSeed((long)(i ^ j << 4) ^ chunkGenerator.getSeed());
            random.nextInt();
            if (random.nextInt(3) != 0) {
                return false;
            }

            if (chunkGenerator.hasStructure(biome, ModFeatureUtils.WINDMILL)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Structure.IStartFactory getStartFactory() {
        return ModWindmillStructure.Start::new;
    }

    @Override
    protected int getSeedModifier() {
        return 10387312;    //  Same as villages
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox bounds, int reference, long seed) {
            super(structure, chunkX, chunkZ, bounds, reference, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome) {
            BlockPos position = new BlockPos(chunkX * 16, 90, chunkZ * 16);
            ModWindmillPiecesHelper.assemble(generator, templateManager, position, components, rand);
            recalculateStructureSize();
        }
    }
}
