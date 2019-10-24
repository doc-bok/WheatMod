package com.bokmcdok.wheat.Block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Represents a wild wheat block that can be grown on grass as well as farmland. It will only mutate if cultivated on
 * farmland.
 */
public class WildWheatBlock extends WheatBlock {

    /**
     * Construct a new wild wheat block.
     * @param seed  The type of seed used to grow this wheat block.
     */
    public WildWheatBlock(Item seed, int diseaseResistance, String registryName)
    {
        super(seed, diseaseResistance, registryName);
    }

    /**
     * Register potential mutations for the wild wheat block.
     */
    public void registerMutations(WheatBlock commonMutation, WheatBlock rareMutation) {
        mCommonMutation = commonMutation;
        mMutation = rareMutation;
        mCanMutate = true;
    }

    /**
     * Handle wild wheat mutating into cultivated wheat.
     */
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        int oldAge = getAge(state);
        super.tick(state, worldIn, pos, random);

        //  If on farmland check for mutations.
        BlockPos groundPos = pos.down();
        if (worldIn.getBlockState(groundPos).getBlock() == Blocks.FARMLAND) {
            checkForMutation(worldIn, pos, random, oldAge, 10);
        }
    }

    /**
     * Also handle mutating into cultivated wheat. If bonemeal is used the chance for mutation is increased.
     */
    public void grow(World worldIn, Random random, BlockPos pos, BlockState state) {
        int oldAge = getAge(state);
        super.grow(worldIn, random, pos, state);

        //  If on farmland check for mutations.
        BlockPos groundPos = pos.down();
        if (worldIn.getBlockState(groundPos).getBlock() == Blocks.FARMLAND) {
            checkForMutation(worldIn, pos, random, oldAge, 8);
        }
    }

    /**
     * Get the mutation.
     */
    @Override
    protected WheatBlock getMutation(Random random) {
        if (random.nextInt(4) < 1) {
            return mMutation;
        } else {
            return mCommonMutation;
        }
    }


    /**
     * Wild Wheat can also be generated and grown on grass blocks.
     */
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return state.getBlock() == Blocks.FARMLAND ||
               state.getBlock() == Blocks.GRASS_BLOCK;
    }

    private WheatBlock mCommonMutation = null;
}
