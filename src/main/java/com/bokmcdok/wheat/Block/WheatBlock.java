package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Represents a generic wheat block.
 */
public class WheatBlock extends ModCropsBlock {

    /**
     * Construct a new wheat block.
     */
    public WheatBlock(Item seed, int diseaseResistance, String registryName)
    {
        super(seed, registryName);

        mMutation = null;
        mDiseaseResistance = diseaseResistance;
    }

    /**
     * Register potential mutations for the wheat block.
     */
    public void registerMutation(WheatBlock mutation, WheatBlock required) {
        mMutation = mutation;
        mRequired = required;
        mCanMutate = true;
    }

    /**
     * Handle wheat diseases and mutations.
     */
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        int oldAge = getAge(state);
        super.tick(state, worldIn, pos, random);

        int newAge = worldIn.getBlockState(pos).get(getAgeProperty());
        if (oldAge != newAge) {

            AtomicBoolean mutateChance = new AtomicBoolean(false);
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
                    block == Blocks.POTTED_BROWN_MUSHROOM || block == Blocks.POTTED_RED_MUSHROOM ) {
                    diseaseChance.addAndGet(-10);
                }

                if (block == ModBlocks.diseased_wheat) {
                    int current = diseaseChance.get();
                    diseaseChance.set(current / 2);
                }

                if (block == mRequired) {
                    mutateChance.set(true);
                }
            });

            if (diseaseChance.get() <= 1 || random.nextInt(diseaseChance.get()) < 1) {
                worldIn.setBlockState(pos, ModBlocks.diseased_wheat.getDefaultState().with(getAgeProperty(), newAge));
            }

            if (mutateChance.get()) {
                checkForMutation(worldIn, pos, random, oldAge, 15);
            }
        }
    }

    /**
     * When grown using bonemeal there is zero chance of disease and a higher chance of mutation.
     */
    public void grow(World worldIn, Random random, BlockPos pos, BlockState state) {
        int oldAge = getAge(state);
        super.grow(worldIn, random, pos, state);

        if (canMutate()) {
            int newAge = worldIn.getBlockState(pos).get(getAgeProperty());
            if (oldAge != newAge) {

                AtomicBoolean mutateChance = new AtomicBoolean(false);
                Stream<BlockPos> blocks = BlockPos.getAllInBox(pos.east().north(), pos.west().south());
                blocks.forEach((p) -> {
                    BlockState blockState = worldIn.getBlockState(p);
                    Block block = blockState.getBlock();
                    if (block == mRequired) {
                        mutateChance.set(true);
                    }
                });

                if (mutateChance.get()) {
                    checkForMutation(worldIn, pos, random, oldAge, 10);
                }
            }
        }
    }

    /**
     * Check for a mutation.
     */
    protected void checkForMutation(World worldIn, BlockPos pos, Random random, int oldAge, int rarity) {
        if (canMutate()) {

            //  Mutate between ages 2 and 3.
            int newAge = worldIn.getBlockState(pos).get(getAgeProperty());
            if (oldAge <= 2 && newAge >= 3) {
                if (random.nextInt(rarity) < 1) {
                    worldIn.setBlockState(pos, getMutation(random).getDefaultState().with(getAgeProperty(), newAge));
                }
            }
        }
    }

    /**
     * Get the mutation.
     */
    protected WheatBlock getMutation(Random random) { return mMutation; }

    /**
     * Get whether or not this wheat can mutate.
     */
    protected boolean canMutate() { return mCanMutate; }

    /**
     * The potential mutation (if any).
     */
    protected WheatBlock mMutation = null;
    protected WheatBlock mRequired = null;
    protected boolean mCanMutate = false;

    /**
     * This wheat's resistance to disease.
     */
    private final int mDiseaseResistance;
}