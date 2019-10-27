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

    private WheatBlock mCommonMutation = null;

    /**
     * Construct a new wild wheat block.
     * @param seed  The type of seed used to grow this wheat block.
     * @param diseaseResistance The crop's resistance to disease.
     * @param registryName The name of the crop in the registry.
     */
    public WildWheatBlock(Item seed, int diseaseResistance, String registryName)
    {
        super(seed, diseaseResistance, registryName);
    }

    /**
     * Register potential mutations for this block.
     * @param commonMutation The main type of wheat that the crop will mutate into.
     * @param rareMutation The second type of crop the bock will mutate into, which is rarer.
     */
    public void registerMutations(WheatBlock commonMutation, WheatBlock rareMutation) {
        mCommonMutation = commonMutation;
        mMutation = rareMutation;
        mRequired = this;
        mCanMutate = true;
    }

    /**
     * Check for a possible mutation. Wild wheat can only mutate on farmland
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     * @param oldAge The age of the crop before the tick update.
     * @param rarity The rarity of a mutation - a lower number means a higher chance of mutation.
     */
    protected void checkForMutation(World worldIn, BlockPos pos, Random random, int oldAge, int rarity) {
        BlockPos groundPos = pos.down();
        if (worldIn.getBlockState(groundPos).getBlock() == Blocks.FARMLAND) {
            super.checkForMutation(worldIn, pos, random, oldAge, rarity);
        }
    }

    /**
     * Get the crop this wheat will mutate into.
     * @param random The current RNG. Used by Wild Wheat.
     * @return The block this crop will mutate into.
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
     * Allow wild wheat to also generate and grow on grass blocks.
     * @param state The block state of the ground
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @return
     */
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return state.getBlock() == Blocks.FARMLAND ||
               state.getBlock() == Blocks.GRASS_BLOCK;
    }
}
