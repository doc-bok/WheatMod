package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.block.ModCropsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Set;

public class ModDiseaseFarmGoal extends MoveToBlockGoal {
    private final Set<Block> mCropsToDisease;
    private final CreatureEntity mEntity;
    private boolean mWantsToRaid;
    private boolean mCanRaid;

    /**
     * Construction
     * @param entity The entity that owns this goal
     */
    public ModDiseaseFarmGoal(CreatureEntity entity, Set<Block> cropsToRaid) {
        super(entity, 0.7, 16);
        mEntity = entity;
        mCropsToDisease = cropsToRaid;
    }

    /**
     * Check if the owner should execute this goal.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay <= 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mEntity.world, mEntity)) {
                return false;
            }

            mCanRaid = false;
            mWantsToRaid = true;
        }

        return super.shouldExecute();
    }

    /**
     * Check if this goal should continue executing.
     * @return TRUE if the goal should continue.
     */
    public boolean shouldContinueExecuting() {
        return mCanRaid && super.shouldContinueExecuting();
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
            World world = mEntity.world;
            BlockPos blockpos = destinationBlock.up();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (mCanRaid && mCropsToDisease.contains(block) && block instanceof ModCropsBlock) {
                ModCropsBlock crop = (ModCropsBlock)block;
                crop.diseaseCrop(world, blockpos, blockstate);
            }

            mCanRaid = false;
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
        if ((block == Blocks.FARMLAND || block == Blocks.GRASS_BLOCK) && mWantsToRaid && !mCanRaid) {
            BlockPos up = position.up();
            BlockState blockstate = world.getBlockState(up);
            block = blockstate.getBlock();
            if (mCropsToDisease.contains(block) && block instanceof ModCropsBlock) {
                mCanRaid = true;
                return true;
            }
        }

        return false;
    }
}
