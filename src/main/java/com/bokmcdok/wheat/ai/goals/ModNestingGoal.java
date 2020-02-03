package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModBlock;
import com.bokmcdok.wheat.entity.creature.animal.ModNestingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ModNestingGoal extends MoveToBlockGoal {
    private final ModNestingEntity mOwner;
    private final ModBlock mNestBlock;

    /**
     * Construction
     * @param nestingEntity The entity that has the nesting behaviour.
     */
    public ModNestingGoal(ModNestingEntity nestingEntity, ModBlock nestBlock, double moveSpeed, int radius, int height) {
        super(nestingEntity, moveSpeed, radius, height);
        mOwner = nestingEntity;
        mNestBlock = nestBlock;
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        return !mOwner.world.isDaytime() && mOwner.getHasNest() && searchForDestination();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return !mOwner.world.isDaytime() && mOwner.getHasNest() && searchForDestination();
    }

    /**
     * Update the goal and pollinate the crop if it can.
     */
    public void tick() {
        super.tick();
        mOwner.getLookController().setLookPosition(
                (double)destinationBlock.getX() + 0.5D,
                destinationBlock.getY(),
                (double)destinationBlock.getZ() + 0.5D,
                10.0F, (float)mOwner.getVerticalFaceSpeed());
    }

    /**
     * The animal remembers where its nest is.
     * @return TRUE if the animal still has a nest.
     */
    protected boolean searchForDestination() {
        if (mOwner.getHasNest()) {
            destinationBlock = mOwner.getNestPosition();
            World world = mOwner.world;
            BlockState blockstate = world.getBlockState(destinationBlock);
            Block block = blockstate.getBlock();
            if (block != mNestBlock) {
                mOwner.resetNestPosition();
                runDelay = 10;
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Check if moving to a block will help achieve the goal.
     * @param world The current world.
     * @param position The position of the block.
     * @return TRUE if the block can be pollinated.
     */
    protected boolean shouldMoveTo(IWorldReader world, BlockPos position) {
        Block block = world.getBlockState(position).getBlock();
        if (!block.equals(mNestBlock)) {
            mOwner.resetNestPosition();
            runDelay = 10;
            return false;
        }

        return true;
    }
}
