package com.bokmcdok.wheat.ai.goals;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class ModAttractToLightGoal extends MoveToBlockGoal {
    private final CreatureEntity mOwner;
    private boolean mWantsToFollow;
    private boolean mCanFollow;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModAttractToLightGoal(CreatureEntity owner) {
        super(owner, 0.7, 16);
        mOwner = owner;
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0) {
            mCanFollow = false;
            mWantsToFollow = true;
        }

        return super.shouldExecute();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return mCanFollow && super.shouldContinueExecuting();
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
            mCanFollow = false;
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
        BlockState state = world.getBlockState(position);
        return state.getLightValue() > 10;
    }
}
