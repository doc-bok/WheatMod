package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.creature.animal.ModNestingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ModCreateNestGoal extends MoveToBlockGoal {
    private final ModNestingEntity mOwner;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModCreateNestGoal(ModNestingEntity owner, double moveSpeed, int radius, int height) {
        super(owner, moveSpeed, radius, height);
        mOwner = owner;
    }

    /**
     * Should the goal be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0 && !net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mOwner.world, mOwner)) {
            return false;
        }

        return mOwner.getIsFertilized() && searchForDestination();
    }

    /**
     * Should the goal continue to be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return mOwner.getIsFertilized() && searchForDestination();
    }

    /**
     * Update the goal and create the nest if it can.
     */
    @Override
    public void tick() {
        super.tick();
        mOwner.getLookController().setLookPosition(
                (double)destinationBlock.getX() + 0.5D,
                destinationBlock.getY() + 1,
                (double)destinationBlock.getZ() + 0.5D,
                10.0F, (float)mOwner.getVerticalFaceSpeed());
        if (getIsAboveDestination()) {
            World world = mOwner.world;
            BlockState blockstate = world.getBlockState(destinationBlock.up());
            Block block = blockstate.getBlock();
            if (mOwner.getIsFertilized() && block instanceof TallGrassBlock) {
                world.setBlockState(destinationBlock.up(), ModBlockUtils.widowbird_nest.getDefaultState(), 2);
            }

            mOwner.setIsFertilized(false);
            mOwner.setNestPosition(destinationBlock.up());
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
        Block block = world.getBlockState(position.up()).getBlock();
        return block instanceof TallGrassBlock && mOwner.getIsFertilized();
    }
}
