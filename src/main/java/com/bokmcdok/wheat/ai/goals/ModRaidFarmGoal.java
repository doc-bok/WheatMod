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

public class ModRaidFarmGoal extends MoveToBlockGoal {
    private final CreatureEntity mEntity;
    private boolean mWantsToRaid;
    private boolean mCanRaid;

    public ModRaidFarmGoal(CreatureEntity entity) {
        super(entity, 0.7, 16);
        mEntity = entity;
    }

    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mEntity.world, mEntity)) {
                return false;
            }

            mCanRaid = false;
            mWantsToRaid = true;
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return mCanRaid && super.shouldContinueExecuting();
    }

    public void tick() {
        super.tick();
        mEntity.getLookController().setLookPosition((double)destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, (double)destinationBlock.getZ() + 0.5D, 10.0F, (float)mEntity.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            World world = mEntity.world;
            BlockPos blockpos = destinationBlock.up();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (mCanRaid && block instanceof ModCropsBlock) {
                Integer integer = blockstate.get(ModCropsBlock.AGE);
                if (integer == 0) {
                    world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                    world.destroyBlock(blockpos, true);
                } else {
                    world.setBlockState(blockpos, blockstate.with(ModCropsBlock.AGE, Integer.valueOf(integer - 1)), 2);
                    world.playEvent(2001, blockpos, Block.getStateId(blockstate));
                }
            }

            mCanRaid = false;
            runDelay = 10;
        }
    }

    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos position) {
        Block block = worldIn.getBlockState(position).getBlock();
        if ((block == Blocks.FARMLAND || block == Blocks.GRASS_BLOCK) && mWantsToRaid && !mCanRaid) {
            BlockPos up = position.up();
            BlockState blockstate = worldIn.getBlockState(up);
            block = blockstate.getBlock();
            if (block instanceof ModCropsBlock && ((ModCropsBlock)block).isMaxAge(blockstate)) {
                mCanRaid = true;
                return true;
            }
        }

        return false;
    }
}
