package com.bokmcdok.wheat.dimension.wheat;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class ModWheatDimension extends Dimension {
    public static ResourceLocation NAME = new ResourceLocation(WheatMod.MOD_ID, "wheat_dimension");

    /**
     * Construction
     * @param world The current world.
     * @param type The dimension type to create.
     */
    public ModWheatDimension(World world, DimensionType type) {
        super(world, type, 0.0f);
    }

    /**
     * Create the chunk generator.
     * TODO: Implement this.
     * @return A new chunk generator.
     */
    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        EndGenerationSettings endgenerationsettings = ChunkGeneratorType.FLOATING_ISLANDS.createSettings();
        endgenerationsettings.setDefaultBlock(Blocks.END_STONE.getDefaultState());
        endgenerationsettings.setDefaultFluid(Blocks.AIR.getDefaultState());
        endgenerationsettings.setSpawnPos(this.getSpawnCoordinate());
        return ChunkGeneratorType.FLOATING_ISLANDS.create(this.world, BiomeProviderType.THE_END.create(BiomeProviderType.THE_END.func_226840_a_(this.world.getWorldInfo())), endgenerationsettings);
    }

    /**
     * Find a valid spawn position.
     * @param position The position to search near.
     * @param checkValid TRUE to check if the spawn is valid.
     * @return A valid spawn position.
     */
    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos position, boolean checkValid) {
        Random random = new Random(world.getSeed());
        BlockPos blockpos = new BlockPos(position.getXStart() + random.nextInt(15), 0, position.getZEnd() + random.nextInt(15));
        return world.getGroundAboveSeaLevel(blockpos).getMaterial().blocksMovement() ? blockpos : null;
    }


    /**
     * Find a valid spawn position.
     * @param x The x-position to search near.
     * @param z The z-position to search near.
     * @param checkValid TRUE to check if the spawn is valid.
     * @return A valid spawn position.
     */
    @Nullable
    @Override
    public BlockPos findSpawn(int x, int z, boolean checkValid) {
        return findSpawn(new ChunkPos(x >> 4, z >> 4), checkValid);
    }

    /**
     * Calculate the position of the sun and the moon.
     * @param worldTime The current world time.
     * @param partialTicks The current time delta.
     * @return The celestial angle.
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float)(d0 * 2.0D + d1) / 3.0F;
    }

    /**
     * Is this dimension a surface world (affects mob spawning, sky rendering, ability to sleep, and whether clocks and compasses work.
     * @return Always TRUE.
     */
    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    /**
     * Get the fog colour.
     * @param celestialAngle The current celestial angle.
     * @param partialTicks The time delta.
     * @return The colour of the fog.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
    }

    /**
     * Can a player sleep/respawn in this dimension?
     * @return Always TRUE.
     */
    @Override
    public boolean canRespawnHere() {
        return true;
    }

    /**
     * Should we show fog at this position?
     * @param x The x-position.
     * @param z The z-position.
     * @return Always FALSE.
     */
    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }
}
