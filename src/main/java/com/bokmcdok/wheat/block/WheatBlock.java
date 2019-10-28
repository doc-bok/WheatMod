package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Represents a generic wheat block.
 */
public class WheatBlock extends ModCropsBlock {

    protected WheatBlock mMutation = null;
    protected WheatBlock mRequired = null;
    protected boolean mCanMutate = false;
    private final int mDiseaseResistance;

    /**
     * Construct a new wheat block.
     * @param seed The seed used to grow this wheat.
     * @param diseaseResistance The crop's resistance to disease.
     * @param registryName The name of the crop in the registry.
     */
    public WheatBlock(Item seed, int diseaseResistance, String registryName)
    {
        super(seed, registryName);

        mMutation = null;
        mDiseaseResistance = diseaseResistance;
    }

    /**
     * Register potential mutations for the wheat block.
     * @param mutation The potential mutation for this block.
     * @param required The other type of wheat required to mutate this crop.
     */
    public void registerMutation(WheatBlock mutation, WheatBlock required) {
        mMutation = mutation;
        mRequired = required;
        mCanMutate = true;
    }

    /**
     * Main update - checks for disease and mutations
     * @param state The current block state
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     */
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        int oldAge = getAge(state);
        super.tick(state, worldIn, pos, random);

        //  If a crop is generated in the air it will be destroyed during the tick update and replaced with an air
        //  block. In this case we don't want to check for disease/mutation as the crop no longer exists and it will
        //  cause Minecraft to crash when it tries to get the Age property.
        BlockState newState = worldIn.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            if (oldAge != newAge) {
                checkForDisease(worldIn, pos, random, newAge);
                checkForMutation(worldIn, pos, random, oldAge, newAge, 15);
            }
        }
    }

    /**
     * When grown using bonemeal there is zero chance of disease and a higher chance of mutation.
     * @param worldIn The world the block is in
     * @param random The current RNG
     * @param pos The position of the block
     * @param state The current block state
     */
    public void grow(World worldIn, Random random, BlockPos pos, BlockState state) {
        int oldAge = getAge(state);
        super.grow(worldIn, random, pos, state);
        BlockState newState = worldIn.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            checkForMutation(worldIn, pos, random, oldAge, newAge, 10);
        }
    }

    /**
     * Randomly mutates crops into diseased crops. The chances are increased by close-cropping, nearby mushrooms, and
     * other nearby diseased crops.
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     * @param age The age of the crop after the tick update.
     */
    protected void checkForDisease(World worldIn, BlockPos pos, Random random, int age) {
        AtomicInteger diseaseChance = new AtomicInteger(mDiseaseResistance);
        Stream<BlockPos> blocks = BlockPos.getAllInBox(pos.east().north(), pos.west().south());
        blocks.forEach((p) -> {
            BlockState blockState = worldIn.getBlockState(p);
            Block block = blockState.getBlock();
            if (block == this) {
                diseaseChance.addAndGet(-1);
            }

            if (block == Blocks.BROWN_MUSHROOM_BLOCK || block == Blocks.RED_MUSHROOM_BLOCK || block == Blocks.MUSHROOM_STEM ||
                    block == Blocks.BROWN_MUSHROOM || block == Blocks.RED_MUSHROOM ||
                    block == Blocks.POTTED_BROWN_MUSHROOM || block == Blocks.POTTED_RED_MUSHROOM) {
                diseaseChance.addAndGet(-10);
            }

            if (block == ModBlockUtils.diseased_wheat) {
                int current = diseaseChance.get();
                diseaseChance.set(current / 2);
            }
        });

        if (diseaseChance.get() <= 1 || random.nextInt(diseaseChance.get()) < 1) {
            worldIn.setBlockState(pos, ModBlockUtils.diseased_wheat.getDefaultState().with(getAgeProperty(), age));
        }
    }

    /**
     * Check for a possible mutation.
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     * @param oldAge The age of the crop before the tick update.
     * @param rarity The rarity of a mutation - a lower number means a higher chance of mutation.
     */
    protected void checkForMutation(World worldIn, BlockPos pos, Random random, int oldAge, int newAge, int rarity) {
        if (oldAge <= 2 && canMutate()) {

            AtomicBoolean mutateChance = new AtomicBoolean(false);
            Stream<BlockPos> blocks = BlockPos.getAllInBox(pos.east().north(), pos.west().south());
            blocks.forEach((p) -> {
                BlockState blockState = worldIn.getBlockState(p);
                Block block = blockState.getBlock();
                if (block.equals(mRequired)) {
                    mutateChance.set(true);
                }
            });

            if (mutateChance.get() && newAge >= 3 && random.nextInt(rarity) < 1) {
                worldIn.setBlockState(pos, getMutation(random).getDefaultState().with(getAgeProperty(), newAge));
            }
        }
    }

    /**
     * Get the crop this wheat will mutate into.
     * @param random The current RNG. Used by Wild Wheat.
     * @return The block this crop will mutate into.
     */
    protected WheatBlock getMutation(Random random) { return mMutation; }

    /**
     * Get whether or not this wheat can mutate.
     * @return True if the crop can mutate.
     */
    protected boolean canMutate() { return mCanMutate; }
}