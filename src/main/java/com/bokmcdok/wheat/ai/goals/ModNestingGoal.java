package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.animal.ModNestingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Optional;

public class ModNestingGoal extends MoveToBlockGoal {
    private final ModNestingEntity mOwner;

    /**
     * Construction
     * @param nestingEntity The entity that has the nesting behaviour.
     */
    public ModNestingGoal(ModNestingEntity nestingEntity) {
        super(nestingEntity, 0.7, 16, 8);
        mOwner = nestingEntity;
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0 && !net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mOwner.world, mOwner)) {
            return false;
        }

        return !mOwner.world.isDaytime() && mOwner.getHasNest() && super.shouldExecute();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return !mOwner.world.isDaytime() && mOwner.getHasNest() && super.shouldExecute();
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
            BlockState blockstate = world.getBlockState(destinationBlock);
            Block block = blockstate.getBlock();
            if (mOwner.getHasNest() && block == ModBlockUtils.widowbird_nest && world.getCurrentMoonPhaseFactor() > 0.9f) {
                world.setBlockState(destinationBlock, Blocks.AIR.getDefaultState(), 2);
                world.playEvent(2001, destinationBlock, Block.getStateId(blockstate));

                //  Spawn 1-3 babies
                int numSpawn = world.getRandom().nextInt(3) + 1;
                for (int i = 0; i < numSpawn; i++) {
                    AgeableEntity child = mOwner.createChild(null);
                    child.setGrowingAge(-24000);
                    child.setLocationAndAngles(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ(), 0.0f, 0.0f);
                    world.addEntity(child);
                }
            }

            mOwner.setNestPosition(Optional.empty());
            runDelay = 10;
        }
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
            if (block != ModBlockUtils.widowbird_nest) {
                mOwner.setNestPosition(Optional.empty());
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
        if (block != ModBlockUtils.widowbird_nest) {
            mOwner.setNestPosition(Optional.empty());
            return false;
        }

        return true;
    }
}
