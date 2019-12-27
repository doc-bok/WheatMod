package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.animal.ModNestingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Optional;

public class ModCreateNestGoal extends MoveToBlockGoal {
    private final ModNestingEntity mOwner;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModCreateNestGoal(ModNestingEntity owner) {
        super(owner, 0.7, 16, 8);
        mOwner = owner;
    }

    /**
     * Should the goal be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mOwner.world, mOwner)) {
                return false;
            }
        }

        if (mOwner.getIsFertilized()) {
            return super.shouldExecute();
        }

        return false;
    }

    /**
     * Should the goal continue to be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (mOwner.getIsFertilized()) {
            return super.shouldContinueExecuting();
        }

        return false;
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
            BlockState blockstate = world.getBlockState(destinationBlock);
            Block block = blockstate.getBlock();
            if (mOwner.getIsFertilized() && block instanceof TallGrassBlock) {
                world.setBlockState(destinationBlock, ModBlockUtils.widowbird_nest.getDefaultState(), 2);
            }

            mOwner.setIsFertilized(false);
            mOwner.setNestPosition(Optional.of(destinationBlock));
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
        if (block instanceof TallGrassBlock && mOwner.getIsFertilized()) {
            return true;
        }

        return false;
    }
}
