package com.bokmcdok.wheat.ai.goals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ModPollinateGoal extends MoveToBlockGoal {
    private final CreatureEntity mOwner;
    private boolean mWantsToPollinate;
    private boolean mCanPollinate;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModPollinateGoal(CreatureEntity owner, double moveSpeed, int radius, int height) {
        super(owner, moveSpeed, radius, height);
        mOwner = owner;
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mOwner.world, mOwner)) {
                return false;
            }

            mCanPollinate = false;
            mWantsToPollinate = true;
        }

        return super.shouldExecute();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return mCanPollinate && super.shouldContinueExecuting();
    }

    /**
     * Update the goal and pollinate the crop if it can.
     */
    public void tick() {
        super.tick();
        mOwner.getLookController().setLookPosition(
                (double)destinationBlock.getX() + 0.5D,
                destinationBlock.getY() + 1,
                (double)destinationBlock.getZ() + 0.5D,
                10.0F, (float)mOwner.getVerticalFaceSpeed());
        if (getIsAboveDestination()) {
            World world = mOwner.world;
            BlockPos blockpos = destinationBlock.up();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (mCanPollinate && block instanceof CropsBlock) {
                CropsBlock crop = (CropsBlock)block;
                Integer integer = blockstate.get(crop.getAgeProperty());
                if (integer < crop.getMaxAge()) {
                    world.setBlockState(blockpos, blockstate.with(crop.getAgeProperty(), Integer.valueOf(integer + 1)), 2);
                    world.playEvent(2001, blockpos, Block.getStateId(blockstate));
                }
            }

            mCanPollinate = false;
            runDelay = 10;
        }
    }

    /**
     * Check if moving to a block will help achieve the goal.
     * @param world The current world.
     * @param position The position of the block.
     * @return TRUE if the block can be pollinated.
     */
    protected boolean shouldMoveTo(IWorldReader world, BlockPos position) {
        Block block = world.getBlockState(position).getBlock();
        if ((block == Blocks.FARMLAND || block == Blocks.GRASS_BLOCK) && mWantsToPollinate && !mCanPollinate) {
            BlockPos up = position.up();
            BlockState blockstate = world.getBlockState(up);
            block = blockstate.getBlock();
            if (block instanceof CropsBlock && !((CropsBlock)block).isMaxAge(blockstate)) {
                mCanPollinate = true;
                return true;
            }
        }

        return false;
    }
}
