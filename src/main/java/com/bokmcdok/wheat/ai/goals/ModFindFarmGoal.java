package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModCropsBlock;
import com.bokmcdok.wheat.supplier.ModTagSupplier;
import com.bokmcdok.wheat.tag.ModTag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class ModFindFarmGoal extends MoveToBlockGoal {
    private final LazyValue<ModTag> mCropsToFind;
    private final CreatureEntity mEntity;
    private boolean mWantsToFind;
    private boolean mCanFind;

    /**
     * Construction
     * @param entity The entity that owns this goal
     */
    public ModFindFarmGoal(CreatureEntity entity, String blockTag, double moveSpeed, int radius, int height) {
        super(entity, moveSpeed, radius, height);
        mEntity = entity;
        mCropsToFind = new LazyValue<>(new ModTagSupplier(ModTagSupplier.TagType.BLOCK, blockTag));
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay <= 0) {
            mCanFind = false;
            mWantsToFind = true;
        }

        return super.shouldExecute();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return mCanFind && super.shouldContinueExecuting();
    }

    /**
     * Update the goal and eat the crop if it can.
     */
    public void tick() {
        super.tick();
        mEntity.getLookController().setLookPosition(
                (double)destinationBlock.getX() + 0.5D,
                destinationBlock.getY() + 1,
                (double)destinationBlock.getZ() + 0.5D,
                10.0F, (float)mEntity.getVerticalFaceSpeed());
        if (getIsAboveDestination()) {
            mCanFind = false;
            runDelay = 10;
        }
    }

    /**
     * Check if moving to a block will help achieve the goal.
     * @param world The current world.
     * @param position The position of the block.
     * @return TRUE if the block can be eaten.
     */
    protected boolean shouldMoveTo(IWorldReader world, BlockPos position) {
        Block block = world.getBlockState(position).getBlock();
        if ((block == Blocks.FARMLAND || block == Blocks.GRASS_BLOCK) && mWantsToFind && !mCanFind) {
            BlockPos up = position.up();
            BlockState blockstate = world.getBlockState(up);
            block = blockstate.getBlock();
            if (mCropsToFind.getValue().containsBlock(block) && block instanceof ModCropsBlock) {
                mCanFind = true;
                return true;
            }
        }

        return false;
    }
}
